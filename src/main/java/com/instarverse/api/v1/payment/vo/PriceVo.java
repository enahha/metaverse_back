package com.instarverse.api.v1.payment.vo;

import com.instarverse.api.v1.common.vo.CommonVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PriceVo extends CommonVo {
	private int seq;
	private String pay_cd;
	private String pay_type;
	private int price;
	private int price_kstar;
	private String use_yn;
	private String userMobileNo;
}
