package com.instarverse.api.v1.fee.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class FeeRateVo {
	private int seq;
	private String fee_cd;
	private int fee_rate;
	private String use_yn;
	private String reg_id;
	private String reg_time;
	private String mod_id;
	private String mod_time;
	private String del_id;
	private String del_time;
}
