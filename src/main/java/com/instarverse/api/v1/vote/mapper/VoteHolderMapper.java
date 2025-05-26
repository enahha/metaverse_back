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
import com.instarverse.api.v1.vote.vo.VoteHolderVo;
import com.instarverse.api.v1.vote.vo.VoteVo;

public interface VoteHolderMapper {
	
	// 투표 홀더 리스트 등록
	public int insertVoteHolderList(VoteVo voteVo) throws Exception;
	
	// 투표 LP 홀더 리스트 등록
	public int insertVoteLpHolderList(VoteVo voteVo) throws Exception;
	
//	// 투표 홀더 MIN(SEQ)-1 조회
//	public int selectVoteHolderFirstSeqMinusOne(VoteHolderVo voteHolderVo) throws Exception;
//	
//	// 투표 홀더 수정 - HOLDER_NO 정렬 수정
//	public int updateVoteHolderHolderNo(VoteHolderVo voteHolderVo) throws Exception;
	
	// 투표 홀더 삭제
//	public int deleteVoteHolder(VoteHolderVo voteHolderVo) throws Exception;
	
	// 투표 홀더 리스트 조회
	public int selectVoteHolderListLastPageNum(CommonVo commonVo) throws Exception;
	public List<VoteHolderVo> selectVoteHolderList(CommonVo commonVo) throws Exception;
	
	// 투표 홀더 조회
	public VoteHolderVo selectVoteHolderByAddress(VoteHolderVo voteHolderVo) throws Exception;
	
	// 투표 홀더 수정 - 투표하기
	public int updateVoteHolder(VoteHolderVo voteHolderVo) throws Exception;
}
