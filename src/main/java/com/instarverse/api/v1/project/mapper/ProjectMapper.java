package com.instarverse.api.v1.project.mapper;

import java.util.List;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.project.vo.ProjectExhibitionHallVo;
import com.instarverse.api.v1.project.vo.ProjectVo;

public interface ProjectMapper {
	
	// 프로젝트 등록
	public int insertProject(ProjectVo projectVo) throws Exception;
	
	// 프로젝트 삭제
	public int deleteProject(ProjectVo projectVo) throws Exception;
	
	// My 프로젝트 리스트 조회
	public int selectMyProjectListLastPageNum(CommonVo commonVo) throws Exception;
	public List<ProjectVo> selectMyProjectList(CommonVo commonVo) throws Exception;
	
	// 인기순위 프로젝트 리스트 조회
	public List<ProjectVo> selectProjectListAsView(int limit) throws Exception;
	
	// 신규전시 프로젝트 리스트 조회
	public List<ProjectVo> selectProjectListAsRegTime(int limit) throws Exception;
	
	// 프로젝트 리스트 조회
	public int selectProjectListLastPageNum(CommonVo commonVo) throws Exception;
	public List<ProjectVo> selectProjectList(CommonVo commonVo) throws Exception;
	
	// 프로젝트 조회(전체 컬럼)
	public ProjectVo selectProject(ProjectVo projectVo) throws Exception;
	
	// 닉네임으로 프로젝트 조회
	public ProjectVo selectProjectByNickName(ProjectVo projectVo) throws Exception;
	
	// 조회수 증가
	public int updateProjectView(int seq) throws Exception;
	
	// 프로젝트 수정
	public int updateProject(ProjectVo projectVo) throws Exception;
	
	// contract address 업데이트
	public int updateProjectContractAddress(ProjectVo projectVo) throws Exception;
	
	// contract address 업데이트
	public int updateProjectCollectionUrl(ProjectVo projectVo) throws Exception;
	
	// 프로젝트 수정 - 잔고
	public int updateProjectBalance(ProjectVo projectVo) throws Exception;
	
	// 프로젝트 수정 - 상태
	public int updateProjectStatusCd(ProjectVo projectVo) throws Exception;
	
	// 프로젝트 수정 - 메인 홀 전처리
	public int updateMainHallPre(ProjectVo projectVo) throws Exception;
	
	// 프로젝트 수정 - 메인 홀
	public int updateMainHall(ProjectVo projectVo) throws Exception;
	
	// 프로젝트 상태 결제완료 체크
	public int checkProjectStatusCdPaid(ProjectVo projectVo) throws Exception;
	
	// 프로젝트와 전시회 데이터 조회
	public ProjectExhibitionHallVo selectProjectAndExhibitionHall(ProjectVo projectVo) throws Exception;
}
