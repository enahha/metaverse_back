package com.instarverse.api.v1.vote.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.common.util.CommonUtil;
import com.instarverse.api.v1.vote.mapper.VoteHolderMapper;
import com.instarverse.api.v1.vote.vo.VoteHolderVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class VoteHolderSelectController {
	
	// private static final Logger logger = LoggerFactory.getLogger(VoteHolderSelectController.class);
	
	@Autowired
	private VoteHolderMapper voteHolderMapper;
	
	/**
	 * 투표 홀더 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/vote/selectVoteHolderByAddress")
	public VoteHolderVo selectVoteHolderByAddress(@RequestParam String uid, @RequestParam int seq) throws Exception {
		VoteHolderVo voteHolderVo = new VoteHolderVo();
		voteHolderVo.setUid(uid);
		voteHolderVo.setSeq(seq);
		return this.voteHolderMapper.selectVoteHolderByAddress(voteHolderVo);
	}
	
	/**
	 * 투표 홀더 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/vote/selectVoteHolderListLastPageNum")
	public int selectVoteHolderListLastPageNum(@RequestParam String uid, @RequestParam int pageSize, @RequestParam String keyword, @RequestParam int voteSeq) throws Exception {
		VoteHolderVo voteHolderVo = new VoteHolderVo();
		voteHolderVo.setKeyword(keyword);
		voteHolderVo.setPageSize(pageSize);
		voteHolderVo.setVote_seq(voteSeq);
		// voteHolderVo.setUid(uid);
		return this.voteHolderMapper.selectVoteHolderListLastPageNum(voteHolderVo);
	}
	
	/**
	 * 투표 홀더 리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/vote/selectVoteHolderList")
	public List<VoteHolderVo> selectVoteHolderList(@RequestParam String uid, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String keyword, @RequestParam int voteSeq) throws Exception {
		// 페이징 처리
		VoteHolderVo voteHolderVo = new VoteHolderVo();
		voteHolderVo.setKeyword(keyword);
		voteHolderVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		voteHolderVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		voteHolderVo.setVote_seq(voteSeq);
		// voteHolderVo.setUid(uid);
		return this.voteHolderMapper.selectVoteHolderList(voteHolderVo);
	}
}
