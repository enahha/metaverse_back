package com.instarverse.api.v1.project.vo;

import com.instarverse.api.v1.media.vo.MediaInfoVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ProjectExhibitionHallVo {
	private String seq;
	private String title;
	private String status_cd;
	private String subtitle;
	private String nickname;
	private String artist_details;
	private String email;
	private String instargram;
	private String twitter;
	private String discord;
	private String telegram;
	private String symbol;
	private String postar_url;
	private String banner_url;
	private String description;
	private String contract_address;
	private String collection_url;
	private String media_url_prefix;
	private String display_start_time;
	private String display_end_time;
	private int view;
	private String production_background;
	private String tag;
	
	private int exhibition_seq;
	private String exhibition_name;
	private String exhibition_type;
	private int display_maximum;
	private String exhibition_description;
	private String exhibition_url;
	
	private String uid;

}
