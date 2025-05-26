package com.instarverse.api.v1.payment.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PayLogErrVo {
	// private String uid;
	private String code;
	private String message;
	private String params;
	
	private String seq;
	
	private String reg_id;
	private String reg_time;
	private String mod_id;
	private String mod_time;
	private String del_id;
	private String del_time;
	
}
