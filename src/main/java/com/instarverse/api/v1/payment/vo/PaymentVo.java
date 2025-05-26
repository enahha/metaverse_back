package com.instarverse.api.v1.payment.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PaymentVo {
	private String uid;
	private String userName;
	private String userMobileNo;
	
	private String order_no;
	private String status;
	private String from_name;
	private String from_mobile_no;
	private String from_email;
	private String to_name;
	private String to_mobile_no;
	private String to_post_no;
	private String to_address1;
	private String to_address2;
	private String to_message;
	private String pay_cd;
	private String pay_type;
	private String pay_card_cd;
	private String pay_card_nm;
	private String card_installment;
	private String pay_bank_cd;
	private String pay_bank_nm;
	private String bank_deadline;
	private String cash_receipt_type;
	private String deduction_type;
	private String deduction_mobile_no;
	private String deduction_card_no;
	private String business_no;
	private String pay_mobile_cd;
	private String pay_mobile_nm;
	private String total_price;
	private String total_reward_coin;
	private String order_product_cnt;
	private String delivery_done_time;
	private String review_done_yn;
	private String deposit_money;
	private String deposit_time;
	private String tid; // 결제거래ID
	private String seq_pay_cancel; // 결제취소 시컨스
	
}
