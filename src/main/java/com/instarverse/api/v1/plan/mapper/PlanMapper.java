package com.instarverse.api.v1.plan.mapper;

import java.util.List;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.plan.vo.PlanVo;

public interface PlanMapper {
	// 플랜 리스트 조회
	public List<PlanVo> selectPlanList() throws Exception;
	
	// 플랜 조회
	public PlanVo selectPlan(PlanVo planVo) throws Exception;
	
	// 플랜 등록
	public int insertPlan(PlanVo planVo) throws Exception;
	
	// 플랜 수정
	public int updatePlan(PlanVo planVo) throws Exception;
	
	// 플랜 삭제
	public int deletePlan(PlanVo planVo) throws Exception;
}
