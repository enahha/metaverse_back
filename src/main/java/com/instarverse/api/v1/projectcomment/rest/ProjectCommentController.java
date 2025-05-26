package com.instarverse.api.v1.projectcomment.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.instarverse.api.v1.common.util.CommonUtil;
import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.projectcomment.mapper.ProjectCommentMapper;
import com.instarverse.api.v1.projectcomment.vo.ProjectCommentVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class ProjectCommentController {
	
	// private static final Logger logger = LoggerFactory.getLogger(ProjectCommentController.class);
	
	@Autowired
	private ProjectCommentMapper projectCommentMapper;
	
	/**
	 * 프로젝트 댓글 등록
	 * 
	 * @param projectCommentVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/projectcomment/insertProjectComment")
	public CommonVo insertProjectComment(@RequestBody ProjectCommentVo projectCommentVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		
		projectCommentVo.setReg_id(projectCommentVo.getUid());
		
		int resultCount = this.projectCommentMapper.insertProjectComment(projectCommentVo);
		if (resultCount == 0) {
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg("insertProjectComment failed.");
		}
		return commonVo;
	}
	
	/**
	 * 프로젝트 답글 등록
	 * 
	 * @param projectCommentVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/projectcomment/insertProjectCommentReply")
	public CommonVo insertProjectCommentReply(@RequestBody ProjectCommentVo projectCommentVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		
		projectCommentVo.setMod_id(projectCommentVo.getUid());
		projectCommentVo.setReg_id(projectCommentVo.getUid());
		
		// 1. 파라미터 GROUP_ORDER 보다  큰 값들은 + 1씩 update 하고 그 사이에 현재 행을 삽입
		if (projectCommentVo.getGroup_layer() > 5) {
			this.projectCommentMapper.updateProjectCommentGroupOrderPlusOne(projectCommentVo);
			projectCommentVo.setGroup_order(projectCommentVo.getGroup_order() + 1);
		}
		
		// 2. 댓글 등록
		int resultCount = this.projectCommentMapper.insertProjectCommentReply(projectCommentVo);
		if (resultCount == 0) {
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg("insertProjectCommentReply failed.");
		}
		return commonVo;
	}
	
	/**
	 * 프로젝트 댓글 수정
	 * 
	 * @param projectCommentVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/projectcomment/updateProjectComment")
	public CommonVo updateProjectComment(@RequestBody ProjectCommentVo projectCommentVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		
		projectCommentVo.setMod_id(projectCommentVo.getUid());
		
		int resultCount = this.projectCommentMapper.updateProjectComment(projectCommentVo);
		if (resultCount == 0) {
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg("updateProjectComment failed.");
		}
		return commonVo;
	}

	/**
	 * 프로젝트 댓글 삭제
	 * 
	 * @param projectCommentVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/projectcomment/deleteProjectComment")
	public CommonVo deleteProjectComment(@RequestBody ProjectCommentVo projectCommentVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		
		projectCommentVo.setDel_id(projectCommentVo.getUid());
		
		int resultCount = this.projectCommentMapper.deleteProjectComment(projectCommentVo);
		if (resultCount == 0) {
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg("deleteProjectComment failed.");
		}
		return commonVo;
	}
	
	/**
	 * 프로젝트 댓글 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/projectcomment/selectProjectCommentListLastPageNum")
	public int selectProjectCommentListLastPageNum(@RequestParam String uid, @RequestParam int projectSeq, @RequestParam int pageSize) throws Exception {
		ProjectCommentVo projectCommentVo = new ProjectCommentVo();
		projectCommentVo.setProject_seq(projectSeq);
		projectCommentVo.setPageSize(pageSize);
		
		return this.projectCommentMapper.selectProjectCommentListLastPageNum(projectCommentVo);
	}
	
	/**
	 * 프로젝트 댓글 요청 리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return List<ProjectCommentVo>
	 * @throws Exception
	 */
	@GetMapping("/projectcomment/selectProjectCommentList")
	public List<ProjectCommentVo> selectProjectCommentList(@RequestParam String uid, @RequestParam int projectSeq, @RequestParam int pageNum, @RequestParam int pageSize) throws Exception {
		// 페이징 처리
		ProjectCommentVo projectCommentVo = new ProjectCommentVo();
		projectCommentVo.setUid(uid);
		projectCommentVo.setProject_seq(projectSeq);
		projectCommentVo.setPageSize(pageSize);
		projectCommentVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		projectCommentVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		return this.projectCommentMapper.selectProjectCommentList(projectCommentVo);
	}
	
	/**
	 * 프로젝트 댓글 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/projectcomment/selectProjectComment")
	public ProjectCommentVo selectProjectComment(@RequestParam String uid, @RequestParam int seq) throws Exception {
		ProjectCommentVo projectCommentVo = new ProjectCommentVo();
//		projectCommentVo.setUid(uid);
		projectCommentVo.setSeq(seq);
		return this.projectCommentMapper.selectProjectComment(projectCommentVo);
	}
	
	/**
	 * 나의 프로젝트 댓글 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/projectcomment/selectMyProjectCommentListLastPageNum")
	public int selectMyProjectCommentListLastPageNum(@RequestParam String uid, @RequestParam int pageSize) throws Exception {
		ProjectCommentVo projectCommentVo = new ProjectCommentVo();
		projectCommentVo.setPageSize(pageSize);
		
		projectCommentVo.setUid(uid);
		
		return this.projectCommentMapper.selectMyProjectCommentListLastPageNum(projectCommentVo);
	}
	
	/**
	 * 나의 프로젝트 댓글 요청 리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return List<ProjectCommentVo>
	 * @throws Exception
	 */
	@GetMapping("/projectcomment/selectMyProjectCommentList")
	public List<ProjectCommentVo> selectMyProjectCommentList(@RequestParam String uid, @RequestParam int pageNum, @RequestParam int pageSize) throws Exception {
		// 페이징 처리
		ProjectCommentVo projectCommentVo = new ProjectCommentVo();
		projectCommentVo.setPageSize(pageSize);
		projectCommentVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		projectCommentVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		
		projectCommentVo.setUid(uid);
		
		return this.projectCommentMapper.selectMyProjectCommentList(projectCommentVo);
	}
}
