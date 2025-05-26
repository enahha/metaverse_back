/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.instarverse.api.v1.votecomment.mapper;

import java.util.List;

import com.instarverse.api.v1.votecomment.vo.VoteCommentVo;
 
public interface VoteCommentMapper {
	// 투표 댓글 등록
	public int insertVoteComment(VoteCommentVo voteCommentVo) throws Exception;
	
	// 2.1 투표 답글 등록 전 update
	public int updateVoteCommentGroupOrderPlusOne(VoteCommentVo voteCommentVo) throws Exception;
	// 2.2 투표 답글 등록
	public int insertVoteCommentReply(VoteCommentVo voteCommentVo) throws Exception;
	
	// 투표 댓글 수정
	public int updateVoteComment(VoteCommentVo voteCommentVo) throws Exception;
	
	// 투표 댓글 삭제
	public int deleteVoteComment(VoteCommentVo voteCommentVo) throws Exception;
	
	// 투표 댓글 리스트 조회
	public int selectVoteCommentListLastPageNum(VoteCommentVo voteCommentVo) throws Exception;
	public List<VoteCommentVo> selectVoteCommentList(VoteCommentVo voteCommentVo) throws Exception;
	
	// 투표 댓글 조회
	public VoteCommentVo selectVoteComment(VoteCommentVo voteCommentVo) throws Exception;
	
}
