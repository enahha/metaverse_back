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
package com.instarverse.api.v1.projectcomment.mapper;

import java.util.List;

import com.instarverse.api.v1.projectcomment.vo.ProjectCommentVo;
 
public interface ProjectCommentMapper {
	// 프로젝트 댓글 등록
	public int insertProjectComment(ProjectCommentVo projectCommentVo) throws Exception;
	
	// 2.1 프로젝트 답글 등록 전 update
	public int updateProjectCommentGroupOrderPlusOne(ProjectCommentVo projectCommentVo) throws Exception;
	// 2.2 프로젝트 답글 등록
	public int insertProjectCommentReply(ProjectCommentVo projectCommentVo) throws Exception;
	
	// 프로젝트 댓글 수정
	public int updateProjectComment(ProjectCommentVo projectCommentVo) throws Exception;
	
	// 프로젝트 댓글 삭제
	public int deleteProjectComment(ProjectCommentVo projectCommentVo) throws Exception;
	
	// 프로젝트 댓글 리스트 조회
	public int selectProjectCommentListLastPageNum(ProjectCommentVo projectCommentVo) throws Exception;
	public List<ProjectCommentVo> selectProjectCommentList(ProjectCommentVo projectCommentVo) throws Exception;
	
	// MY 프로젝트 댓글 리스트 조회
	public int selectMyProjectCommentListLastPageNum(ProjectCommentVo projectCommentVo) throws Exception;
	public List<ProjectCommentVo> selectMyProjectCommentList(ProjectCommentVo projectCommentVo) throws Exception;
	
	// 프로젝트 댓글 조회
	public ProjectCommentVo selectProjectComment(ProjectCommentVo projectCommentVo) throws Exception;
	
}
