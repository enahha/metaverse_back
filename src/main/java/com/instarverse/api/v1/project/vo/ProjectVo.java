package com.instarverse.api.v1.project.vo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.instarverse.api.v1.common.vo.CommonVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ProjectVo extends CommonVo {
	
	private String keyword;
	
	private MultipartFile file;
	
//	private String add_total_supply_amount; // 토큰 총 발행량 증가분 수치
//	private String burn_amount; // 토큰 소각 갯수
	
    private int seq;
    private String title;
    private String title_ko;
    private String status_cd;
    private String main_hall;
    private String symbol;
    private String subtitle;
    private String exhibitionhall_seq;
    private String banner_url;
    private String postar_url;
    private String description;
    private String description_ko;
    private String display_start_time;
    private String display_end_time;
    private String representative_sns_id;
    private String artist_details;
    private String email;
    private String instargram;
    private String twitter;
    private String discord;
    private String telegram;
    private int view;
    private String production_background;
    private String nickname;
    private String contract_status_cd;
    private String logo_image;
    private String docs;
    private String blog;
    private String medium;
    private String github;
    private String meta;
    private String mainnet;
    private String tag;
    private String summary;
    private String summary_ko;
    private String display_cd;
    private String type;
    private String wallet_address;
    private String official_website;
    private String official_email;
    private String lp_contract_address_1;
    private String lp_contract_address_2;
    private String lp_contract_address_3;
    private String lp_contract_address_4;
    private String lp_contract_address_5;
    private String lp_contract_address_6;
    private String lp_contract_address_7;
    private String lp_contract_address_8;
    private String lp_contract_address_9;
    private String lp_contract_address_10;
    private String contract_address;
    private String collection_url;
    private String media_url_prefix;
    private String platform_deposit_balance;
    private String platform_yield;
    private String platform_total_balance;
    private String del_yn;
    
    private List<String> tag_list;
	
}
