package com.instarverse.api.v1.scan.vo;

import com.instarverse.api.v1.common.vo.CommonVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class HolderVo extends CommonVo {
	private int seq;
	private int vote_seq;
	private int holder_no;
	private String uid;
	private String address;
	private String amount;
	private String agreeYn;
	private String vote_time;
//	private String reg_id;
//	private String reg_time;
//	private String mod_id;
//	private String mod_time;
//	private String del_id;
//	private String del_time;
}
