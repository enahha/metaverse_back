package com.instarverse.api.v1.log.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class KpiLogVo {
	private String uid;
	private int action_no;
	private String action_cd;
	private String params;
	private String action_time;
	private String reg_id;
	
}
