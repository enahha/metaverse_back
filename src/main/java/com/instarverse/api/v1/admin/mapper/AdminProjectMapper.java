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
package com.instarverse.api.v1.admin.mapper;

import java.util.List;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.project.vo.ProjectVo;
 
public interface AdminProjectMapper {
	
	// 프로젝트  수정
	public int updateAdminProject(ProjectVo projectVo) throws Exception;
	
	// 프로젝트  수정
	public int updateAdminProjectContractStatusCd(ProjectVo projectVo) throws Exception;
	
	// 프로젝트 리스트 조회
	public int selectAdminMyProjectListLastPageNum(CommonVo commonVo) throws Exception;
	public List<ProjectVo> selectAdminMyProjectList(CommonVo commonVo) throws Exception;
}
