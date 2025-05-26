package com.instarverse.api.v1.ai.vo;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AiProjectVo {
	
	private String keyword;
	
	private MultipartFile file;
	
//	private String add_total_supply_amount; // 토큰 총 발행량 증가분 수치
//	private String burn_amount; // 토큰 소각 갯수
	
	private int seq;
	private String uid;
	private String mainnet;
	private String type;
	private String status_cd;
	private String title;
	private String title_ko;
	private String summary;
	private String summary_ko;
	private String description;
	private String description_ko;
	private String official_website;
	private String official_email;
	private String logo_image;
	private String docs;
	private String blog;
	private String medium;
	private String telegram;
	private String twitter;
	private String github;
	private String meta;
	private String discord;
//	private String display_cd;
//	private String wallet_address;
//	private String token_contract_address;
//	private String lp_contract_address_1;
//	private String lp_contract_address_2;
//	private String lp_contract_address_3;
//	private String lp_contract_address_4;
//	private String lp_contract_address_5;
//	private String lp_contract_address_6;
//	private String lp_contract_address_7;
//	private String lp_contract_address_8;
//	private String lp_contract_address_9;
//	private String lp_contract_address_10;
//	private String platform_contract_address;
//	private String platform_deposit_balance;
//	private String platform_yield;
//	private String platform_total_balance;
//	private String del_yn;
//	private String reg_id;
//	private String reg_time;
//	private String mod_id;
//	private String mod_time;
//	private String del_id;
//	private String del_time;
	
//	private String status_comment; // myList에서 계약 검증 상태에 따른 회신 내용
	
}
