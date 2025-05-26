package com.instarverse.api.v1.instagram.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class InstagramResultVo {
	
	// private int seq;
	// private String url;
	
	private List<InstagramMediaVo> media_list;
	private String num_results;
	private String more_available;
	private String next_max_id;
	
//	private String reg_id;
//	private String reg_time;
//	private String mod_id;
//	private String mod_time;
//	private String del_id;
//	private String del_time;
	
	private String resultCd;
	private String resultMsg;
}
