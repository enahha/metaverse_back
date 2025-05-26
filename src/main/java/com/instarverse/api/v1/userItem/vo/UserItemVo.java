package com.instarverse.api.v1.userItem.vo;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.notice.vo.NoticeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserItemVo extends CommonVo {
	private int seq;
	private String uid;
	private int in_use;
	private int item_seq;
	private int plan_seq;
	private String use_start_time;
	private String use_end_time;
	private String del_yn;
}
