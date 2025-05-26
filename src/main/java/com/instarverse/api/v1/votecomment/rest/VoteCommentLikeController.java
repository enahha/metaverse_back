package com.instarverse.api.v1.votecomment.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.votecomment.mapper.VoteCommentLikeMapper;
import com.instarverse.api.v1.votecomment.vo.VoteCommentLikeVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class VoteCommentLikeController {
	
	// private static final Logger logger = LoggerFactory.getLogger(VoteCommentLikeController.class);
	
	@Autowired
	private VoteCommentLikeMapper voteCommentLikeMapper;
	
	/**
	 * 투표 댓들 좋아요/싫어요 등록
	 * like_type 0:NONE, 1:LIKE, 2:DISLIKE
	 * 
	 * @param voteCommentVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/votecomment/mergeVoteCommentLike")
	public CommonVo mergeVoteCommentLike(@RequestBody VoteCommentLikeVo voteCommentViewVo) throws Exception {
		// Y:LIKE, N:DISLIKE, NULL
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		
		voteCommentViewVo.setReg_id(voteCommentViewVo.getUid());
		voteCommentViewVo.setMod_id(voteCommentViewVo.getUid());
		
		int resultCount = this.voteCommentLikeMapper.mergeVoteCommentLike(voteCommentViewVo);
		if (resultCount == 0) {
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg("mergeVoteCommentLike failed.");
		}
		return commonVo;
	}
}
