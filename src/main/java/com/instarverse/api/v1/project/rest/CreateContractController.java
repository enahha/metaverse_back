package com.instarverse.api.v1.project.rest;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.blockchain.rest.MetadataController;
import com.instarverse.api.v1.common.Constant;
import com.instarverse.api.v1.common.vo.CommonResultVo;
import com.instarverse.api.v1.contract.ContractUtil;
import com.instarverse.api.v1.fee.mapper.FeeRateMapper;
import com.instarverse.api.v1.fee.vo.FeeRateVo;
import com.instarverse.api.v1.media.mapper.MediaMapper;
import com.instarverse.api.v1.media.vo.MediaInfoVo;
import com.instarverse.api.v1.media.vo.MediaVo;
import com.instarverse.api.v1.project.mapper.ProjectMapper;
import com.instarverse.api.v1.project.vo.ProjectVo;
import com.klaytn.caver.Caver;
import com.klaytn.caver.abi.datatypes.Type;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.ContractDeployParams;
import com.klaytn.caver.contract.ContractMethod;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;

//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class CreateContractController {
	
	private static final Logger logger = LoggerFactory.getLogger(CreateContractController.class);
	
	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private FeeRateMapper feeRateMapper;
	
	@Autowired
	private MediaMapper mediaMapper;
	
	@Autowired
	private MetadataController metadataController;
	
	@Value("${klaytn.contract-minting-abi}")
	private String klaytnContractMintingAbi;
	
	@Value("${klaytn.contract-minting-bin}")
	private String klaytnContractMintingBin;
	
	@Value("${klaytn.admin-wallet-address}")
	private String klaytnAdminWalletAddress;
	
	@Value("${file.server-ip}")
	private String fileServerIp;
	
	@Value("${file.uploaded-path-json}")
	private String fileUploadedPathJson;
	
	
	/**
	 * 컨트렉트 배포 및 민팅
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/project/mintKlaytnNft")
	public void mintKlaytnNft(@RequestParam int seq) throws Exception {
		// 결과코드 : 성공
//		CommonResultVo commonResultVo = new CommonResultVo();
//		commonResultVo.setResultCd("SUCCESS");
//		commonResultVo.setReturnCd("0");
		try {
			System.out.println("mintKlaytnNft 시작...");
			System.out.println("1. project 정보 조회   ►►►►►►►►►►►►►►►  ");
			// ◆ project 정보 조회
			ProjectVo projectVo = new ProjectVo();
			projectVo.setSeq(seq);
			projectVo = this.projectMapper.selectProject(projectVo);
			
			System.out.println("2. 아미 생성된 contract_address 존재시 PASS   ►►►►►►►►►►►►►►►  ");
			// ◆ 아미 생성된 contract_address 존재시 PASS
			String contractAddress = projectVo.getContract_address();
			System.out.println("contarct address:    ►►►►►►►►►►►►►►►  " + contractAddress);
			// contract가 생성이 안되있을 때 생성
			if(contractAddress == null || contractAddress.isEmpty()) {
				System.out.println("3. contract 생성 및 프로젝트 테이블 contract_address 업데이트   ►►►►►►►►►►►►►►►  ");
				// ◆ contract 생성 및 프로젝트 테이블 contract_address 업데이트
				String newContractAddress = this.createContract(projectVo);
				projectVo.setContract_address(newContractAddress);
				if (newContractAddress != null) {
					System.out.println("4. 프로젝트 컨트렉트 생성 완료 status_cd(30) 업데이트   ►►►►►►►►►►►►►►►  ");
					// 실패 - 프로젝트 컨트렉트 생성 성공코드 status_cd(30) 업데이트
					projectVo.setStatus_cd(Constant.PROJECT_STATUS_CD_CONTRACT);
					this.projectMapper.updateProjectStatusCd(projectVo);
				} else {
					System.out.println("컨트렉트 생성 실패 return   ►►►►►►►►►►►►►►►  ");
					return;
				}
			}
			// 미디어 정보 리스트 조회
			System.out.println("미디어 정보 리스트 조회   ►►►►►►►►►►►►►►►  ");
			MediaInfoVo mediaInfoVo = new MediaInfoVo();
			mediaInfoVo.setProject_seq(seq);
			
			List<MediaInfoVo> mediaInfoList = this.mediaMapper.selectJsonMediaInfoList(mediaInfoVo);
			if(mediaInfoList.isEmpty()) {
				System.out.println("미디어 정보 없음 return   ►►►►►►►►►►►►►►►  ");
				return;
			}
			
			int lastNftId = mediaInfoList.get(0).getLast_nft_id();
			System.out.println("lastNftId  :   " + lastNftId);
			System.out.println(mediaInfoList.get(0));
			for (int i = 0; i < mediaInfoList.size(); i++) {
				// ◆ json 생성
				System.out.println("5. json 생성   ►►►►►►►►►►►►►►►  ");
				CommonResultVo createJsonFileResult = this.metadataController.createJsonFile(projectVo.getSeq()
																							, projectVo.getTitle()
																							, projectVo.getSubtitle()
																							, mediaInfoList.get(i).getUrl()
																							, ++lastNftId);
				
				try {
				    // 3초 지연
				    Thread.sleep(2000); 
				} catch (InterruptedException e) {
				    e.printStackTrace();
				}
				
				// ◆ 민팅 및 nft_id 업데이트
				System.out.println("6. 민팅 및 nft_id 업데이트   ►►►►►►►►►►►►►►►  ");
				CommonResultVo mintNftResult = this.mintNft(projectVo.getContract_address()
													, mediaInfoList.get(i).getSeq());
				
				if (mintNftResult.getResultCd() == "FAIL" && createJsonFileResult.getResultMsg() == "FAIL") {
					return;
				}
			}
			
//			System.out.println("7. mint 완료 status_cd(40) 업데이트   ►►►►►►►►►►►►►►►  ");
			// mint 완료 status_cd(40) 업데이트
			projectVo.setStatus_cd(Constant.PROJECT_STATUS_CD_MINT);
			this.projectMapper.updateProjectStatusCd(projectVo);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			// 결과코드 : 실패
		}
	}

	
	/**
	 * 프로젝트 컨트랙트 생성
	 * 
	 * @return
	 * @throws Exception
	 */
	public String createContract(ProjectVo projectVo) throws Exception {
		String newContractAddress = null;
		System.out.println("createContract 실행 >>>>>>>>>>>>>>> ");
		try {
			String uid = projectVo.getUid();
			projectVo.setMod_id(uid);
			
			if (Constant.MAINNET_KLAYTN.equals(projectVo.getMainnet())) {
				// 컨트랙트 생성
				// 1. 지갑 설정
				Caver caver = new Caver(Constant.KLAYTN_MAINNET_URL);
				SingleKeyring deployer = KeyringFactory.createFromKlaytnWalletKey(Constant.KLAYTN_WALLET_KEY_CREATE_TOKEN);
				caver.wallet.add(deployer);
				
				
				// 2. 컨트랙트 파라미터로 사용할 변수 설정
				int seq = projectVo.getSeq();
				String name = projectVo.getTitle();
//				String projectTitleKo = projectVo.getTitle_ko();
//				String projectType = projectVo.getType();
				String symbol = projectVo.getSymbol();
//				String baseURI = "https://galleryx.io/nft/json/{seq}/";		// 예시 데이터
				String baseURI = "https://" + this.fileServerIp + this.fileUploadedPathJson + "/" + seq + "/";
				boolean revealed = true;
//				String adminAddress = "0x6Afa10d8563c9227c3660A8B506f455De445db00";
//				String projectWalletAddress = projectVo.getWallet_address(); // withdraw 시 수수료 수신용 지갑
//				String projectTokenContractAddress = projectVo.getContract_address();
				// NFT 프로젝트인 경우, Token Contract Address가 null이므로 임시로 아무 값이나 파라미터로 설정
				// ※ 파라미터가 null이면 deploy 전에 에러 떨어짐..
//				if (StringUtil.isEmpty(projectTokenContractAddress)) {
//					projectTokenContractAddress = "0x0";
//				}
				
//				if (projectTitle == null) {
//					projectTitle = "";
//				}
//				if (projectTitleKo == null) {
//					projectTitleKo = "";
//				}
				// 2.1 수수료 계산
				// 2.1.1 수수료율 조회
				int feeRate100x = 100; // withdraw 시 수수료율
				FeeRateVo feeRateVo = new FeeRateVo();
				feeRateVo.setFee_cd(Constant.FEE_RATE_CD_WITHDRAW);
				feeRateVo = this.feeRateMapper.selectFeeRate(feeRateVo);
				if (feeRateVo != null) {
					feeRate100x = feeRateVo.getFee_rate();
				}
				// 2.1.2 수수료 계산
				double feeRate = (double) feeRate100x / 100; // 1 %
				int feeRateInt = (int) feeRate;
				
				// 3 컨트랙트 설정
				Contract contract = caver.contract.create(this.klaytnContractMintingAbi);
				// 3.1 SendOption 설정
				SendOptions sendOptions = new SendOptions();
				sendOptions.setFrom(deployer.getAddress());
				sendOptions.setGas(BigInteger.valueOf(ContractUtil.KLAYSTAR_SEND_OPTIONS_GAS));
				
				System.out.println("name: " + name);
				System.out.println("symbol: " + symbol);
				System.out.println("baseURI: " + baseURI);
				System.out.println("feeRateInt: " + feeRateInt);
				System.out.println("revealed: " + revealed);
				System.out.println("adminAddress: " + klaytnAdminWalletAddress);
				// 3.2 컨트랙트 파라미터 설정
				Object[] paramsDeploy = {
						name
					, symbol
					, baseURI
					, new BigInteger(String.valueOf(feeRateInt))
					, revealed
					, klaytnAdminWalletAddress
				};
				ContractDeployParams deployParam = new ContractDeployParams(this.klaytnContractMintingBin, paramsDeploy);
				// 3.3 프로젝트 컨트랙트 배포 - constructor에서 생성
				Contract newContract = contract.deploy(deployParam, sendOptions);
				logger.debug("newContract.getContractAddress()➯➯➯➯➯➯➯➯➯➯➯➯➯➯➯ " + newContract.getContractAddress());
				
				
				// 4. 프로젝트 컨트랙트 어드레스 설정
				newContractAddress = newContract.getContractAddress();
			} else if (Constant.MAINNET_ETHEREUM.equals(projectVo.getMainnet())) {
				// TODO: 이더리움용 코드 생성
			} else {
				// 지원하지 않는 메인넷.
			}
			
			
			// 5. 프로젝트 테이블 수정 (contract_address, media_url_prefix)
			String prefixUrl = Constant.OPENSEA_MEDIA_PREFIX_URL + newContractAddress + "/";	//https://opensea.io/assets/klaytn/0x69534f1fede5c216ae789e3069e28c4aab9fc377/
			projectVo.setMedia_url_prefix(prefixUrl);
			projectVo.setContract_address(newContractAddress);
			int resultCount = this.projectMapper.updateProjectContractAddress(projectVo);
			if (resultCount == 0) {
				System.err.println("Failed to update project table for contract address or media url prefix: " + newContractAddress);
				return newContractAddress;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return newContractAddress;
		}
		return newContractAddress;
	}
	
	/**
	 * mint nft
	 * 
	 * @return
	 * @throws Exception
	 */
	public CommonResultVo mintNft(String contractAddress
								, int mediaSeq) throws Exception {
		// 결과코드 : 성공
		CommonResultVo commonResultVo = new CommonResultVo();
		commonResultVo.setResultCd("SUCCESS");
		commonResultVo.setReturnCd("0");
		BigInteger nftId = null;
		try {
			// 컨트랙트 생성
			// 1. 지갑 설정
			Caver caver = new Caver(Constant.KLAYTN_MAINNET_URL);
			SingleKeyring Keyring = KeyringFactory.createFromKlaytnWalletKey(Constant.KLAYTN_WALLET_KEY_CREATE_TOKEN);
			caver.wallet.add(Keyring);
			
			// 2 컨트랙트 설정
			Contract contract = caver.contract.create(this.klaytnContractMintingAbi, contractAddress);
			
			// 2.1 SendOption 설정
			SendOptions sendOptions = new SendOptions();
			sendOptions.setFrom(Keyring.getAddress());
			sendOptions.setGas(BigInteger.valueOf(ContractUtil.KLAYSTAR_SEND_OPTIONS_GAS));
			
			// 2.2 컨트랙트 파라미터 설정 (리스트로 변환)
			List<Object> paramsDeploy = Arrays.asList(
				klaytnAdminWalletAddress
			);
			
			TransactionReceipt.TransactionReceiptData receipt = contract.getMethod("mintNft").send(paramsDeploy, sendOptions);
			
			try {
			    // 3초 지연
			    Thread.sleep(2000); 
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
			
			ContractMethod method = contract.getMethod("getLatestTokenId");
			List<Type> result = method.call(Collections.emptyList());
			nftId = (BigInteger) result.get(0).getValue();
			System.out.println("NFT ID  ➯➯➯➯➯➯➯➯➯➯➯➯➯➯➯ " + nftId.toString());
			
			MediaVo mediaVo = new MediaVo();
			mediaVo.setNft_id(nftId.toString());
			mediaVo.setSeq(mediaSeq);
			
			// media 테이블 업데이트
			int resultLogCount = this.mediaMapper.updateMediaNftId(mediaVo);
			if (resultLogCount == 0) {
				commonResultVo.setResultCd("FAIL"); // 비정상처리
				commonResultVo.setResultMsg("updateMediaNftId failed.");
				commonResultVo.setReturnCd("1");
				return commonResultVo;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			commonResultVo.setResultCd("FAIL"); // 비정상처리
			commonResultVo.setResultMsg("mintNft failed.");
			commonResultVo.setReturnCd("1");
			return commonResultVo;
		}
		return commonResultVo;
	}
	
}
