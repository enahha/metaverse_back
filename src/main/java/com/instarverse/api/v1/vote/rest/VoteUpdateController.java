package com.instarverse.api.v1.vote.rest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.ContractMethod;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.methods.response.TransactionReceipt.TransactionReceiptData;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;

import com.instarverse.api.v1.common.Constant;
import com.instarverse.api.v1.common.util.StringUtil;
import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.contract.ContractUtil;
import com.instarverse.api.v1.fee.mapper.FeeRateMapper;
import com.instarverse.api.v1.fee.vo.FeeRateVo;
import com.instarverse.api.v1.project.mapper.ProjectMapper;
import com.instarverse.api.v1.project.vo.ProjectVo;
import com.instarverse.api.v1.scan.rest.HolderSelectController;
import com.instarverse.api.v1.vote.mapper.VoteHolderMapper;
import com.instarverse.api.v1.vote.mapper.VoteMapper;
import com.instarverse.api.v1.vote.vo.VoteHolderVo;
import com.instarverse.api.v1.vote.vo.VoteVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class VoteUpdateController {
	
	private static final Logger logger = LoggerFactory.getLogger(VoteUpdateController.class);
	
	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private VoteMapper voteMapper;
	
	@Autowired
	private VoteHolderMapper voteHolderMapper;
	
	@Autowired
	private FeeRateMapper feeRateMapper;
	
	@Autowired
	private HolderSelectController holderSelectController;
	
	@Autowired
	private VoteSelectController voteSelectController;
	
	@Value("${klaytn.platform-contract-abi}")
	private String klaytnPlatformContractAbi;
	
	/**
	 * 투표 수정
	 * 
	 * @param voteVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/vote/updateVote")
	public CommonVo updateVote(@RequestBody VoteVo voteVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			String uid = voteVo.getUid();
//			int voteSeq = voteVo.getSeq();
			
			voteVo.setMod_id(uid);
			
			// 1. 수수료 계산
			// 실제 수수료 처리는 컨트랙트 생성시 설정되지만
			// 시스템용 이력을 별도로 남기기 위해 계산해 둠.
			
			// 수수료율 조회
			int feeRate100x = 100; // withdraw 시 수수료율
			FeeRateVo feeRateVo = new FeeRateVo();
			feeRateVo.setFee_cd("WITHDRAWAL");
			feeRateVo = this.feeRateMapper.selectFeeRate(feeRateVo);
			if (feeRateVo != null) {
				feeRate100x = feeRateVo.getFee_rate();
			}
			
			// 수수료 계산
			int requestAmount = voteVo.getRequest_amount();
			double feeRate = (double) feeRate100x / 10000; // 0.01
			double platformFeeDouble = (int)requestAmount * feeRate;
			int platformFee = (int) platformFeeDouble;
			int withdrawalAmount = requestAmount - platformFee;
			
			// vo에 수수료 설정
			voteVo.setPlatform_fee(platformFee);
			voteVo.setWithdrawal_amount(withdrawalAmount);
			
			// 2. 투표 수정
			int resultCount = this.voteMapper.updateVote(voteVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateVote failed.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
	
	/**
	 * 투표 수정 - 확정
	 * 
	 * @param voteVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/vote/updateVoteStatusCdFixed")
	public CommonVo updateVoteStatusCdFixed(@RequestBody VoteVo voteVo) throws Exception {
		VoteVo returnVoteVo = new VoteVo();
//		CommonVo commonVo = new CommonVo();
		returnVoteVo.setResultCd("SUCCESS");
		try {
			
			String uid = voteVo.getUid();
			
			// 1. 투표 정보 조회
			voteVo = voteMapper.selectVote(voteVo);
			if (voteVo == null) {
				returnVoteVo.setResultCd("FAIL");
				returnVoteVo.setResultMsg("No Vote in DB.");
				return returnVoteVo;
			}
			voteVo.setUid(uid); // 홀더 리스트 등록시 사용
			
			// 2. 홀더 리스트 등록
			// 홀더 리스트 조회
			ArrayList<VoteHolderVo> voteHolderList = this.holderSelectController.selectHolderList(
				uid
				, voteVo.getSeq()
				, voteVo.getProject_mainnet()
				, voteVo.getProject_type()
				, voteVo.getProject_token_contract_address()
			);
			voteVo.setVoteHolderList(voteHolderList);
			
			// 홀더 리스트 등록
			int resultCount = this.voteHolderMapper.insertVoteHolderList(voteVo);
			if (resultCount == 0) {
				returnVoteVo.setResultCd("FAIL");
				returnVoteVo.setResultMsg("insertVoteHolderList failed.");
			}
			
			// 3. LP 홀더 리스트 조회 및 등록
			// 3.1 프로젝트 정보 조회
			ProjectVo projectVo = new ProjectVo();
			projectVo.setSeq(voteVo.getProject_seq());
			projectVo = this.projectMapper.selectProject(projectVo);
			
			// 3.2 LP 홀더 리스트 조회 및 등록 (lp_contract_address_1 ~ 10)
			if (StringUtil.isNotEmpty(projectVo.getLp_contract_address_1())) {
				voteHolderList = this.holderSelectController.selectHolderList(
						uid
						, voteVo.getSeq()
						, voteVo.getProject_mainnet()
						, voteVo.getProject_type()
						, projectVo.getLp_contract_address_1()
						);
				if (voteHolderList != null && voteHolderList.size() > 0) {
					voteVo.setVoteHolderList(voteHolderList);
					this.voteHolderMapper.insertVoteLpHolderList(voteVo);
				}
			}
			
			if (StringUtil.isNotEmpty(projectVo.getLp_contract_address_2())) {
				voteHolderList = this.holderSelectController.selectHolderList(
						uid
						, voteVo.getSeq()
						, voteVo.getProject_mainnet()
						, voteVo.getProject_type()
						, projectVo.getLp_contract_address_2()
						);
				if (voteHolderList != null && voteHolderList.size() > 0) {
					voteVo.setVoteHolderList(voteHolderList);
					this.voteHolderMapper.insertVoteLpHolderList(voteVo);
				}
			}
			
			if (StringUtil.isNotEmpty(projectVo.getLp_contract_address_3())) {
				voteHolderList = this.holderSelectController.selectHolderList(
						uid
						, voteVo.getSeq()
						, voteVo.getProject_mainnet()
						, voteVo.getProject_type()
						, projectVo.getLp_contract_address_3()
						);
				if (voteHolderList != null && voteHolderList.size() > 0) {
					voteVo.setVoteHolderList(voteHolderList);
					this.voteHolderMapper.insertVoteLpHolderList(voteVo);
				}
			}
			
			if (StringUtil.isNotEmpty(projectVo.getLp_contract_address_4())) {
				voteHolderList = this.holderSelectController.selectHolderList(
						uid
						, voteVo.getSeq()
						, voteVo.getProject_mainnet()
						, voteVo.getProject_type()
						, projectVo.getLp_contract_address_4()
						);
				if (voteHolderList != null && voteHolderList.size() > 0) {
					voteVo.setVoteHolderList(voteHolderList);
					this.voteHolderMapper.insertVoteLpHolderList(voteVo);
				}
			}
			
			if (StringUtil.isNotEmpty(projectVo.getLp_contract_address_5())) {
				voteHolderList = this.holderSelectController.selectHolderList(
						uid
						, voteVo.getSeq()
						, voteVo.getProject_mainnet()
						, voteVo.getProject_type()
						, projectVo.getLp_contract_address_5()
						);
				if (voteHolderList != null && voteHolderList.size() > 0) {
					voteVo.setVoteHolderList(voteHolderList);
					this.voteHolderMapper.insertVoteLpHolderList(voteVo);
				}
			}
			
			if (StringUtil.isNotEmpty(projectVo.getLp_contract_address_6())) {
				voteHolderList = this.holderSelectController.selectHolderList(
						uid
						, voteVo.getSeq()
						, voteVo.getProject_mainnet()
						, voteVo.getProject_type()
						, projectVo.getLp_contract_address_6()
						);
				if (voteHolderList != null && voteHolderList.size() > 0) {
					voteVo.setVoteHolderList(voteHolderList);
					this.voteHolderMapper.insertVoteLpHolderList(voteVo);
				}
			}
			
			if (StringUtil.isNotEmpty(projectVo.getLp_contract_address_7())) {
				voteHolderList = this.holderSelectController.selectHolderList(
						uid
						, voteVo.getSeq()
						, voteVo.getProject_mainnet()
						, voteVo.getProject_type()
						, projectVo.getLp_contract_address_7()
						);
				if (voteHolderList != null && voteHolderList.size() > 0) {
					voteVo.setVoteHolderList(voteHolderList);
					this.voteHolderMapper.insertVoteLpHolderList(voteVo);
				}
			}
			
			if (StringUtil.isNotEmpty(projectVo.getLp_contract_address_8())) {
				voteHolderList = this.holderSelectController.selectHolderList(
						uid
						, voteVo.getSeq()
						, voteVo.getProject_mainnet()
						, voteVo.getProject_type()
						, projectVo.getLp_contract_address_8()
						);
				if (voteHolderList != null && voteHolderList.size() > 0) {
					voteVo.setVoteHolderList(voteHolderList);
					this.voteHolderMapper.insertVoteLpHolderList(voteVo);
				}
			}
			
			if (StringUtil.isNotEmpty(projectVo.getLp_contract_address_9())) {
				voteHolderList = this.holderSelectController.selectHolderList(
						uid
						, voteVo.getSeq()
						, voteVo.getProject_mainnet()
						, voteVo.getProject_type()
						, projectVo.getLp_contract_address_9()
						);
				if (voteHolderList != null && voteHolderList.size() > 0) {
					voteVo.setVoteHolderList(voteHolderList);
					this.voteHolderMapper.insertVoteLpHolderList(voteVo);
				}
			}
			
			if (StringUtil.isNotEmpty(projectVo.getLp_contract_address_10())) {
				voteHolderList = this.holderSelectController.selectHolderList(
						uid
						, voteVo.getSeq()
						, voteVo.getProject_mainnet()
						, voteVo.getProject_type()
						, projectVo.getLp_contract_address_10()
						);
				if (voteHolderList != null && voteHolderList.size() > 0) {
					voteVo.setVoteHolderList(voteHolderList);
					this.voteHolderMapper.insertVoteLpHolderList(voteVo);
				}
			}
			
//			// 4. HOLDER_NO 수정
//			// LP 홀더 등록 후 최종처리로 HOLDER_NO를 정렬 수정할 때 첫 SEQ에서 1을 뺀 값을 SEQ에서 빼기.
//			// e.g. 첫 SEQ가 13이면 13 - 12 = 1 이 값을 HOLDER_NO로 설정.
//			// 두번째 SEQ는 14이고 14 - 12 = 2 이므로 HOLDER_NO는 2가 됨.
//			VoteHolderVo voteHolderVo = new VoteHolderVo();
//			voteHolderVo.setVote_seq(voteVo.getSeq());
//			
//			// 4.1 가장 첫 SEQ 조회
//			// SELECT MIN(SEQ) FROM vote_holder WHERE VOTE_SEQ = #{seq}
//			int firstSeqMinusOne = voteHolderMapper.selectVoteHolderFirstSeqMinusOne(voteHolderVo);
//			
//			// 4.2 투표 홀더 수정 - HOLDER_NO 정렬 수정
//			if (firstSeqMinusOne > -1) {
//				voteHolderVo.setFirst_seq_minus_one(firstSeqMinusOne);
//				voteHolderMapper.updateVoteHolderHolderNo(voteHolderVo);
//			}
			
			
			// 5. 투표 정보 수정 - 확정
			voteVo.setMod_id(uid); // 투표 수정시 사용
			voteVo.setStatus_cd(Constant.VOTE_STATUS_CD_FIXED);
			returnVoteVo.setStatus_cd(Constant.VOTE_STATUS_CD_FIXED);
			resultCount = this.voteMapper.updateVoteStatusCdFixed(voteVo);
			if (resultCount == 0) {
				returnVoteVo.setResultCd("FAIL");
				returnVoteVo.setResultMsg("updateVoteFixed failed.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// 결과코드 : 실패
			returnVoteVo.setResultCd("FAIL");
			returnVoteVo.setResultMsg(e.toString());
			return returnVoteVo;
		}
		
		return returnVoteVo;
	}
	
	/**
	 * 투표 마감 처리
	 * 
	 * @param voteVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/vote/updateVoteStatusCdClosed")
	public CommonVo updateVoteStatusCdClosed(@RequestBody VoteVo voteVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			// 투표 마감 처리
			// 투표 상태 수정: status_cd -> 40 (마감)
			voteVo.setStatus_cd(Constant.VOTE_STATUS_CD_CLOSED);
			voteVo.setMod_id(voteVo.getUid());
			
			int resultCount = this.voteMapper.updateVoteStatusCdClosed(voteVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateVoteStatusCdClosed failed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
	
	/**
	 * 투표 수정 - 출금
	 * 
	 * @param voteVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/vote/updateVoteWithdraw")
	public CommonVo updateVoteWithdraw(@RequestBody VoteVo voteVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			String modId = voteVo.getUid();
			
			// 1. 투표 정보 조회
			voteVo = voteMapper.selectVote(voteVo);
			if (voteVo == null) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("No Vote in DB.");
				return commonVo;
			}
			
			// 2. 출금 요건 충족여부 체크
			// 투표권자의 25% 이상 참여 and 그 중 과반수 이상이 반대해야만 반려
			CommonVo resultVo = voteSelectController.selectVoteApprovedYn(voteVo);
			if ("N".equals(resultVo.getResultCd())) {
				commonVo.setResultCd("NOT_ALLOWED");
				commonVo.setResultMsg("You didn't get approval.");
				return commonVo;
			}
			
			// 3. 출금 처리 - 컨트랙트 호출
			String txid = "";
			if (Constant.MAINNET_KLAYTN.equals(voteVo.getProject_mainnet())) {
				Caver caver = new Caver(Constant.KLAYTN_MAINNET_URL);
				SingleKeyring deployer = KeyringFactory.createFromKlaytnWalletKey(Constant.KLAYTN_WALLET_KEY_CREATE_TOKEN);
				caver.wallet.add(deployer);
				Contract contract = caver.contract.create(this.klaytnPlatformContractAbi, voteVo.getProject_platform_contract_address());
				
				// SendOption 설정
				SendOptions sendOptions = new SendOptions();
				sendOptions.setFrom(deployer.getAddress());
				sendOptions.setGas(BigInteger.valueOf(ContractUtil.KLAYSTAR_SEND_OPTIONS_GAS));
				
				
				// 출금액
				String totalAmount = String.valueOf(voteVo.getWithdrawal_amount());
				
				// 0.123456 -> amountArray[1].length() = 6
				// 1234.1234567 -> amountArray[1].length() = 7
				// 1.12345678901 -> amountArray[1].length() = 11
				String[] amountArray = totalAmount.split("\\.");
				int decimalLength = 0; // 소수 자릿수
				if (amountArray.length > 1) {
					decimalLength = amountArray[1].length(); // 소수 자릿수
					totalAmount = totalAmount.replace(".", ""); // 소수점 삭제
				}
				///////////////////////////////////////////////////////////////////
				// 소수점을 삭제했으므로 소수 자릿수 만큼 빼기
				///////////////////////////////////////////////////////////////////
				// 1. 더하기 케이스
				// 0.123456 -> 0123456 (18 token_decimals 에서 6을 빼기 = 12)
				// 0123456 + "0" + "0" + "0" + "0" + "0" + "0" = 0123456000000
				///////////////////////////////////////////////////////////////////
				// 1. 빼기 케이스
				// 0.123456 -> 0123456 (3 token_decimals 에서 6을 빼기 = -3)
				// "0123456". 
				///////////////////////////////////////////////////////////////////
				int decimals = 18 - decimalLength; // 18 하드코딩 -> 추후 변경 필요
				if (decimals > 0) {
					// decimals가 0보다 크면, 뒤에 "0"을 추가
					for (int i = 0; i < decimals; i++) {
						// multipleDecimals += "0";
						totalAmount += "0";
					}
				} else {
					// decimals가 0보다 작으면, 뒤에 숫자 삭제
					// 123.123456789 -> 6 token_decimals 에서 9를 빼면 -3
					// 123123456789 -> 0 부터, 12 + (-3) = 9 까지 -> 123123456
					totalAmount = totalAmount.substring(0, totalAmount.length() + decimals);
				}
				
				Object[] paramsWithdraw = {
					new BigInteger(totalAmount)
				};
				
				ContractMethod methodWithdraw = contract.getMethod("withdraw");
				TransactionReceiptData receiptWithdraw = methodWithdraw.send(Arrays.asList(paramsWithdraw), sendOptions);
				
				txid = receiptWithdraw.getTransactionHash();
				logger.debug("txid: " + txid);
			} else if (Constant.MAINNET_ETHEREUM.equals(voteVo.getProject_mainnet())) {
				// TODO: 이더리움용 처리 추가
			} else {
				// 지원하지 않는 메인넷
			}
			
			
			// 4. 투표 정보 수정
			voteVo.setMod_id(modId);
			voteVo.setWithdrawal_txid(txid);
			int resultCount = this.voteMapper.updateVoteWithdraw(voteVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateVoteWithdraw failed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
}
