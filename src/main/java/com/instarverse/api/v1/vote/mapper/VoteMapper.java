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
package com.instarverse.api.v1.vote.mapper;

import java.util.List;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.vote.vo.VoteVo;

public interface VoteMapper {
	
	// 투표 등록
	public int insertVote(VoteVo voteVo) throws Exception;
	
	// 투표 삭제
	public int deleteVote(VoteVo voteVo) throws Exception;
	
	// My 투표 리스트 조회
	public int selectMyVoteListLastPageNum(CommonVo commonVo) throws Exception;
	public List<VoteVo> selectMyVoteList(CommonVo commonVo) throws Exception;
	
	// 투표 리스트 조회
	public int selectVoteListLastPageNum(CommonVo commonVo) throws Exception;
	public List<VoteVo> selectVoteList(CommonVo commonVo) throws Exception;
	
	// 투표 조회(전체 컬럼)
	public VoteVo selectVote(VoteVo voteVo) throws Exception;

	// 투표 수정
	public int updateVote(VoteVo voteVo) throws Exception;
	
	// 투표 수정 - 확정
	public int updateVoteStatusCdFixed(VoteVo voteVo) throws Exception;
	
	// 투표 수정 - 마감
	public int updateVoteStatusCdClosed(VoteVo voteVo) throws Exception;
	
	// 투표 수정 - 출금
	public int updateVoteWithdraw(VoteVo voteVo) throws Exception;
}
