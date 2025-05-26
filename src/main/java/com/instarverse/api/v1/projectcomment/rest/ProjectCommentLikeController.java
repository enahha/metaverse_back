package com.instarverse.api.v1.projectcomment.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.projectcomment.mapper.ProjectCommentLikeMapper;
import com.instarverse.api.v1.projectcomment.vo.ProjectCommentLikeVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class ProjectCommentLikeController {
	
	// private static final Logger logger = LoggerFactory.getLogger(ProjectCommentLikeController.class);
	
	@Autowired
	private ProjectCommentLikeMapper projectCommentLikeMapper;
	
	/**
	 * 프로젝트 댓들 좋아요/싫어요 등록
	 * like_type 0:NONE, 1:LIKE, 2:DISLIKE
	 * 
	 * @param projectCommentVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/projectcomment/mergeProjectCommentLike")
	public CommonVo mergeProjectCommentLike(@RequestBody ProjectCommentLikeVo projectCommentViewVo) throws Exception {
		// Y:LIKE, N:DISLIKE, NULL
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		
		projectCommentViewVo.setReg_id(projectCommentViewVo.getUid());
		projectCommentViewVo.setMod_id(projectCommentViewVo.getUid());
		
		int resultCount = this.projectCommentLikeMapper.mergeProjectCommentLike(projectCommentViewVo);
		if (resultCount == 0) {
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg("mergeProjectCommentLike failed.");
		}
		return commonVo;
	}
}
