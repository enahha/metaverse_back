package com.instarverse.api.v1.vote.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.vote.mapper.VoteHolderMapper;
import com.instarverse.api.v1.vote.vo.VoteVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class VoteHolderInsertController {
	
	// private static final Logger logger = LoggerFactory.getLogger(VoteHolderInsertController.class);
	
	@Autowired
	private VoteHolderMapper voteHolderMapper;
	
	/**
	 * 투표 홀더 리스트 등록
	 * 
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/voteHolder/insertVoteHolderList")
	public CommonVo insertVoteHolderList(VoteVo voteVo) throws Exception {
		// 결과코드 : 성공
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			// 1. 투표 홀더 리스트 등록
			int resultCount = this.voteHolderMapper.insertVoteHolderList(voteVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("insertVoteHolderList failed.");
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
