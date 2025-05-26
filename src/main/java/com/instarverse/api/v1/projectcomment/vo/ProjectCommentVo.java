package com.instarverse.api.v1.projectcomment.vo;

import com.instarverse.api.v1.common.vo.CommonVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ProjectCommentVo extends CommonVo {
	private int seq;
	private int project_seq;
	private int seq_parent1;
	private int seq_parent2;
	private int seq_parent3;
	private int seq_parent4;
	private int seq_parent5;
	private int group_order;
	private int group_layer;
	private String contents;
	private String reg_name;
	private String like_cd;
	private int like_cnt;
	private int dislike_cnt;
	private int reply_cnt1;
	private int reply_cnt2;
	private int reply_cnt3;
	private int reply_cnt4;
	private int reply_cnt5;
	private boolean visible_reply_input;
	private boolean visible_child;
	
	private String project_title;
	
	
//	private String reg_id;
//	private String reg_time;
//	private String mod_id;
//	private String mod_time;
//	private String del_id;
//	private String del_time;
	
	
}
