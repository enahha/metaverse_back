package com.instarverse.api.v1.vote.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.vote.mapper.VoteMapper;
import com.instarverse.api.v1.vote.vo.VoteVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class VoteDeleteController {
	
	// private static final Logger logger = LoggerFactory.getLogger(VoteDeleteController.class);
	
	@Autowired
	private VoteMapper voteMapper;
	
	/**
	 * 투표 삭제
	 * 
	 * @param voteVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/vote/deleteVote")
	public CommonVo deleteVote(@RequestBody VoteVo voteVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			voteVo.setDel_id(voteVo.getUid());
			voteVo.setDel_yn("Y");
			int resultCount = this.voteMapper.deleteVote(voteVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("deleteVote failed.");
				return commonVo;
			}
		} catch (Exception e) {
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
}
