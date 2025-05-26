package com.instarverse.api.v1.log.mapper;

import com.instarverse.api.v1.log.vo.KpiLogVo;

public interface KpiLogMapper {

	// kpi_log 등록
	public int insertKpiLog(KpiLogVo kpiLogVo) throws Exception;
	
}

