package com.instarverse.api.v1.alarm.vo;

import com.instarverse.api.v1.common.vo.CommonVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AlarmVo extends CommonVo {
	
	private String keyword;
	
	private int seq;
	private String target_address;
	private int target_seq;
	private String alarm_cd;
// 	private String uid;
	private String title;
	private String title_ko;
	private String target_title;
	private String target_title_ko;
	private String target_image;
	private String read_yn;
	private String del_yn;
//	private String reg_id;
//	private String reg_time;
//	private String mod_id;
//	private String mod_time;
//	private String del_id;
//	private String del_time;
}
