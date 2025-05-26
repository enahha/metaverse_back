package com.instarverse.api.v1.payment.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PayBaseInfoVo {
	private String resultCd;
	private String resultMsg;
	
	private String payCode; // 결제 코드 - CREATE_TOKEN, ...
	private int seq;	// item seq
	
	private String goodname;
	private String buyername;
	private String buyertel;
	private String buyeremail;
	private String price;
	private String mid;
	private String gopaymethod; // PC: Card, Mobile: wcard
//	private String mKey;
	private String mkeyNew;
	private String signature;
	private String verification;
	private String oid;
	private String timestamp;
	private String version;
	private String currency;
	private String acceptmethod;
	private String returnUrl;
	private String closeUrl;
}
