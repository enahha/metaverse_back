package com.instarverse.api.v1.instagram.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class InstagramMediaVo {
	
	private int seq;
	private String pk;
	private String id;
	private String media_type;
	private String thumbnail_url;
	private String url;
	private List<InstagramMediaVo> carousel_media_list;
	private String[] carousel_media_ids;
	
//	private String reg_id;
//	private String reg_time;
//	private String mod_id;
//	private String mod_time;
//	private String del_id;
//	private String del_time;
}
