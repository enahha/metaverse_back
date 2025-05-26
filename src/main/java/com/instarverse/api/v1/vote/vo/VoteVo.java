package com.instarverse.api.v1.vote.vo;

import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

import com.instarverse.api.v1.common.vo.CommonVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class VoteVo extends CommonVo {
	
	private String keyword;
	
	private MultipartFile file;
	
//	private String add_total_supply_amount; // 토큰 총 발행량 증가분 수치
//	private String burn_amount; // 토큰 소각 갯수
	
	private int seq;
	private String uid;
	private int project_seq;
	private String project_mainnet;
	private String project_type;
	private String project_title;
	private String project_title_ko;
	private String project_wallet_address;
	private String project_token_contract_address;
	private String project_platform_contract_address;
	private String status_cd;
	private String title;
	private String title_ko;
	private String description;
	private String description_ko;
	private String main_image;
	private String start_time;
	private String end_time;
	private String fixed_time;
	private int request_amount;
	private int platform_fee;
	private int withdrawal_amount;
	private String close_off_time;
	private String withdrawal_time;
	private String withdrawal_txid;
	private String del_yn;
//	private String reg_id;
//	private String reg_time;
//	private String mod_id;
//	private String mod_time;
//	private String del_id;
//	private String del_time;
	
	private String project_logo_image;
	
	// 투표율 표시 항목
	private int total;
	private int total_agree;
	private int total_disagree;
	private double rate_agree;
	private double rate_disagree;
	
	private ArrayList<VoteHolderVo> voteHolderList;
	
}
