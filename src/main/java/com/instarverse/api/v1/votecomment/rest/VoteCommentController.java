package com.instarverse.api.v1.votecomment.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.instarverse.api.v1.common.util.CommonUtil;
import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.votecomment.mapper.VoteCommentMapper;
import com.instarverse.api.v1.votecomment.vo.VoteCommentVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class VoteCommentController {
	
	// private static final Logger logger = LoggerFactory.getLogger(VoteCommentController.class);
	
	@Autowired
	private VoteCommentMapper voteCommentMapper;
	
	/**
	 * 투표 댓글 등록
	 * 
	 * @param voteCommentVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/votecomment/insertVoteComment")
	public CommonVo insertVoteComment(@RequestBody VoteCommentVo voteCommentVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		
		voteCommentVo.setReg_id(voteCommentVo.getUid());
		
		int resultCount = this.voteCommentMapper.insertVoteComment(voteCommentVo);
		if (resultCount == 0) {
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg("insertVoteComment failed.");
		}
		return commonVo;
	}
	
	/**
	 * 투표 답글 등록
	 * 
	 * @param voteCommentVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/votecomment/insertVoteCommentReply")
	public CommonVo insertVoteCommentReply(@RequestBody VoteCommentVo voteCommentVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		
		voteCommentVo.setMod_id(voteCommentVo.getUid());
		voteCommentVo.setReg_id(voteCommentVo.getUid());
		
		// 1. 파라미터 GROUP_ORDER 보다  큰 값들은 + 1씩 update 하고 그 사이에 현재 행을 삽입
		if (voteCommentVo.getGroup_layer() > 5) {
			this.voteCommentMapper.updateVoteCommentGroupOrderPlusOne(voteCommentVo);
			voteCommentVo.setGroup_order(voteCommentVo.getGroup_order() + 1);
		}
		
		// 2. 댓글 등록
		int resultCount = this.voteCommentMapper.insertVoteCommentReply(voteCommentVo);
		if (resultCount == 0) {
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg("insertVoteCommentReply failed.");
		}
		return commonVo;
	}
	
	/**
	 * 투표 댓글 수정
	 * 
	 * @param voteCommentVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/votecomment/updateVoteComment")
	public CommonVo updateVoteComment(@RequestBody VoteCommentVo voteCommentVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		
		voteCommentVo.setMod_id(voteCommentVo.getUid());
		
		int resultCount = this.voteCommentMapper.updateVoteComment(voteCommentVo);
		if (resultCount == 0) {
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg("updateVoteComment failed.");
		}
		return commonVo;
	}

	/**
	 * 투표 댓글 삭제
	 * 
	 * @param voteCommentVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/votecomment/deleteVoteComment")
	public CommonVo deleteVoteComment(@RequestBody VoteCommentVo voteCommentVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		
		voteCommentVo.setDel_id(voteCommentVo.getUid());
		
		int resultCount = this.voteCommentMapper.deleteVoteComment(voteCommentVo);
		if (resultCount == 0) {
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg("deleteVoteComment failed.");
		}
		return commonVo;
	}
	
	/**
	 * 투표 댓글 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/votecomment/selectVoteCommentListLastPageNum")
	public int selectVoteCommentListLastPageNum(@RequestParam String uid, @RequestParam int voteSeq, @RequestParam int pageSize) throws Exception {
		VoteCommentVo voteCommentVo = new VoteCommentVo();
		voteCommentVo.setVote_seq(voteSeq);
		voteCommentVo.setPageSize(pageSize);
		
		return this.voteCommentMapper.selectVoteCommentListLastPageNum(voteCommentVo);
	}
	
	/**
	 * 투표 댓글 요청 리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return List<VoteCommentVo>
	 * @throws Exception
	 */
	@GetMapping("/votecomment/selectVoteCommentList")
	public List<VoteCommentVo> selectVoteCommentList(@RequestParam String uid, @RequestParam int voteSeq, @RequestParam int pageNum, @RequestParam int pageSize) throws Exception {
		// 페이징 처리
		VoteCommentVo voteCommentVo = new VoteCommentVo();
		voteCommentVo.setUid(uid);
		voteCommentVo.setVote_seq(voteSeq);
		voteCommentVo.setPageSize(pageSize);
		voteCommentVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		voteCommentVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		return this.voteCommentMapper.selectVoteCommentList(voteCommentVo);
	}
	
	/**
	 * 투표 댓글 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/votecomment/selectVoteComment")
	public VoteCommentVo selectVoteComment(@RequestParam String uid, @RequestParam int seq) throws Exception {
		VoteCommentVo voteCommentVo = new VoteCommentVo();
//		voteCommentVo.setUid(uid);
		voteCommentVo.setSeq(seq);
		return this.voteCommentMapper.selectVoteComment(voteCommentVo);
	}
	
	/**
	 * 나의 투표 댓글 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/votecomment/selectMyVoteCommentListLastPageNum")
	public int selectMyVoteCommentListLastPageNum(@RequestParam String uid, @RequestParam int voteSeq, @RequestParam int pageSize) throws Exception {
		VoteCommentVo voteCommentVo = new VoteCommentVo();
		voteCommentVo.setVote_seq(voteSeq);
		voteCommentVo.setPageSize(pageSize);
		
		voteCommentVo.setReg_id(uid);
		
		return this.voteCommentMapper.selectVoteCommentListLastPageNum(voteCommentVo);
	}
	
	/**
	 * 나의 투표 댓글 요청 리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return List<VoteCommentVo>
	 * @throws Exception
	 */
	@GetMapping("/votecomment/selectMyVoteCommentList")
	public List<VoteCommentVo> selectMyVoteCommentList(@RequestParam String uid, @RequestParam int voteSeq, @RequestParam int pageNum, @RequestParam int pageSize) throws Exception {
		// 페이징 처리
		VoteCommentVo voteCommentVo = new VoteCommentVo();
		voteCommentVo.setVote_seq(voteSeq);
		voteCommentVo.setPageSize(pageSize);
		voteCommentVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		voteCommentVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		
		voteCommentVo.setReg_id(uid);
		
		return this.voteCommentMapper.selectVoteCommentList(voteCommentVo);
	}
}
