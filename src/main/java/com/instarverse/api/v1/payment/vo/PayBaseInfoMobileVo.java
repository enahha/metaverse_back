package com.instarverse.api.v1.payment.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PayBaseInfoMobileVo {
	private String resultCd;
	private String resultMsg;
	
	private String payCode; // 결제 코드 - CREATE_TOKEN, ...
	private String device; // 디바이스 - P: 데스크탑, M: 모바일웹, android: 안드로이드 앱, ios: 아이폰 앱
	private int seq;	// item seq
	
	private String p_ini_payment;
	private String p_mid;
	private String p_oid;
	private String p_amt;
	private String p_goods;
	private String p_uname;
	private String p_next_url;
	private String p_noti_url;
	private String p_hpp_method;
	private String p_mobile;
	private String p_email;
	private String p_mname;
	private String p_charset;
	private String p_offer_period;
	private String p_quotabase;
	private String p_tax;
	private String p_taxfree;
	private String p_card_option;
	private String p_only_cardcode;
	private String p_vbank_dt;
	private String p_vbank_tm;
	private String p_noti;
	private String p_reserved;
}
