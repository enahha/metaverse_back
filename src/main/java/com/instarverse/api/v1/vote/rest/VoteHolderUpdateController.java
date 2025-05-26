package com.instarverse.api.v1.vote.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.vote.mapper.VoteHolderMapper;
import com.instarverse.api.v1.vote.vo.VoteHolderVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class VoteHolderUpdateController {
	
//	private static final Logger logger = LoggerFactory.getLogger(VoteHolderUpdateController.class);

//	@Autowired
//	private VoteHolderMapper voteMapper;
	
	@Autowired
	private VoteHolderMapper voteHolderMapper;
	
	/**
	 * 투표 홀더 수정 - 투표하기
	 * 
	 * @param voteHolderVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/vote/updateVoteHolder")
	public CommonVo updateVoteHolder(@RequestBody VoteHolderVo voteHolderVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			// 투표하기
			voteHolderVo.setMod_id(voteHolderVo.getUid());
			
			int resultCount = this.voteHolderMapper.updateVoteHolder(voteHolderVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateVoteHolder failed.");
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
