package com.instarverse.api.v1.votecomment.vo;

import com.instarverse.api.v1.common.vo.CommonVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class VoteCommentLikeVo extends CommonVo {
//	private int seq;
	private int vote_comment_seq;
	private String like_cd;
	private String reg_id;
//	private String reg_time;
//	private String mod_id;
//	private String mod_time;
//	private String del_id;
//	private String del_time;
	
	
}
