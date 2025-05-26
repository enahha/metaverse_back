package com.instarverse.api.v1.vote.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.common.Constant;
import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.fee.mapper.FeeRateMapper;
import com.instarverse.api.v1.fee.vo.FeeRateVo;
import com.instarverse.api.v1.vote.mapper.VoteMapper;
import com.instarverse.api.v1.vote.vo.VoteVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class VoteInsertController {
	
	// private static final Logger logger = LoggerFactory.getLogger(VoteInsertController.class);
	
	@Autowired
	private VoteMapper voteMapper;
	
	@Autowired
	private FeeRateMapper feeRateMapper;
	
	
	/**
	 * 투표 등록
	 * 
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/vote/insertVote")
	public CommonVo insertVote(@RequestBody VoteVo voteVo) throws Exception {
		// 결과코드 : 성공
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			// 1. 수수료 계산
			// 실제 수수료 처리는 컨트랙트 생성시 설정되지만
			// 시스템용 이력을 별도로 남기기 위해 계산해 둠.
			
			// 수수료율 조회
			int feeRate100x = 100; // withdraw 시 수수료율
			FeeRateVo feeRateVo = new FeeRateVo();
			feeRateVo.setFee_cd(Constant.FEE_RATE_CD_WITHDRAW);
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
			
			// 2. 투표 등록
			voteVo.setReg_id(voteVo.getUid());
			voteVo.setDel_yn("N"); // 삭제플래그 N
			voteVo.setStatus_cd(Constant.VOTE_STATUS_CD_REGISTERED);
			
			int resultCount = this.voteMapper.insertVote(voteVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("insertVote failed.");
				return commonVo;
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
