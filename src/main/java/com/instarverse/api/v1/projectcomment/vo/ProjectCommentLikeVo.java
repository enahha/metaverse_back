package com.instarverse.api.v1.projectcomment.vo;

import com.instarverse.api.v1.common.vo.CommonVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ProjectCommentLikeVo extends CommonVo {
//	private int seq;
	private int project_comment_seq;
	private String like_cd;
	private String reg_id;
//	private String reg_time;
//	private String mod_id;
//	private String mod_time;
//	private String del_id;
//	private String del_time;
	
	
}
