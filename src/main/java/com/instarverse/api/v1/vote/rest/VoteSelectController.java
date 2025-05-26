package com.instarverse.api.v1.vote.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.instarverse.api.v1.common.util.CommonUtil;
import com.instarverse.api.v1.common.util.StringUtil;
import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.vote.mapper.VoteMapper;
import com.instarverse.api.v1.vote.vo.VoteVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class VoteSelectController {
	
	// private static final Logger logger = LoggerFactory.getLogger(VoteSelectController.class);
	
	@Autowired
	private VoteMapper voteMapper;
	
	/**
	 * 투표 승인 여부 조회
	 * 
	 * @param voteVo
	 * @return true or false
	 * @throws Exception
	 */
	@GetMapping("/vote/selectVoteApprovedYn")
	public CommonVo selectVoteApprovedYn(@RequestBody VoteVo voteVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("Y");
		try {
			// 1. 투표 정보 조회
			// voteVo가 이미 조회된 상태인 경우에는 조회하지 않음
			if (StringUtil.isEmpty(voteVo.getReg_id())) {
				voteVo = voteMapper.selectVote(voteVo);
				if (voteVo == null) {
					commonVo.setResultCd("N");
					commonVo.setResultMsg("No Vote in DB.");
					return commonVo;
				}
			}
			
			// 2. 출금 요건 충족여부 체크
			// 투표권자의 25% 이상 참여 and 그 중 과반수 이상이 반대해야만 반려
			try {
				int total = voteVo.getTotal(); // 총 투표권자 수
				int total_agree = voteVo.getTotal_agree(); // 총 찬성 수
				int total_disagree = voteVo.getTotal_disagree(); // 총 반대 수
				
				int numOfVoter = total_agree + total_disagree; // 투표자 수
				int majority = (int) Math.floor((double) numOfVoter / 2); // 과반수
				int participationRate = (numOfVoter * 100 / total); // 투표율
				
				if (participationRate > 25 && total_disagree > majority) {
					// 투표율이 25% 이상이면서 반대수가 과반수 이상일 경우
					commonVo.setResultCd("N");
					commonVo.setResultMsg("You didn't get approval.");
					return commonVo;
				}
			} catch (Exception e) {
				commonVo.setResultCd("N");
				commonVo.setResultMsg("Error in allowance check.");
				return commonVo;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			// 결과코드 : 실패
			commonVo.setResultCd("N");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
	
	/**
	 * 투표 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/vote/selectVote")
	public VoteVo selectVote(@RequestParam String uid, @RequestParam int seq) throws Exception {
		VoteVo voteVo = new VoteVo();
		voteVo.setUid(uid);
		voteVo.setSeq(seq);
		return this.voteMapper.selectVote(voteVo);
	}
	
	/**
	 * My 투표 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/vote/selectMyVoteListLastPageNum")
	public int selectMyVoteListLastPageNum(@RequestParam String uid, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		VoteVo voteVo = new VoteVo();
		voteVo.setKeyword(keyword);
		voteVo.setPageSize(pageSize);
		voteVo.setReg_id(uid);
		return this.voteMapper.selectMyVoteListLastPageNum(voteVo);
	}
	
	/**
	 * My 투표 리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/vote/selectMyVoteList")
	public List<VoteVo> selectMyVoteList(@RequestParam String uid, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		// 페이징 처리
		VoteVo voteVo = new VoteVo();
		voteVo.setKeyword(keyword);
		voteVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		voteVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		voteVo.setReg_id(uid);
		return this.voteMapper.selectMyVoteList(voteVo);
	}
	
	/**
	 * 투표 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/vote/selectVoteListLastPageNum")
	public int selectVoteListLastPageNum(@RequestParam String uid, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		VoteVo voteVo = new VoteVo();
		voteVo.setKeyword(keyword);
		voteVo.setPageSize(pageSize);
		voteVo.setUid(uid);
		return this.voteMapper.selectVoteListLastPageNum(voteVo);
	}
	
	/**
	 * 투표 리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/vote/selectVoteList")
	public List<VoteVo> selectVoteList(@RequestParam String uid, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		// 페이징 처리
		VoteVo voteVo = new VoteVo();
		voteVo.setKeyword(keyword);
		voteVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		voteVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		voteVo.setUid(uid);
		return this.voteMapper.selectVoteList(voteVo);
	}
}
