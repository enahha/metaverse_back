package com.instarverse.api.v1.faq.vo;

import com.instarverse.api.v1.common.vo.CommonVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class FaqVo extends CommonVo {
	private int seq;
	private String title;
	private String title_ko;
	private String contents;
	private String contents_ko;
	private String display_yn;
//	private String reg_id;
//	private String reg_time;
//	private String mod_id;
//	private String mod_time;
//	private String del_id;
//	private String del_time;
	
	
}