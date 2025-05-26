package com.instarverse.api.v1.vote.vo;

import com.instarverse.api.v1.common.vo.CommonVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class VoteHolderVo extends CommonVo {
	private int seq;
	private int vote_seq;
	private int holder_no;
	private String uid;
	private String address;
	private String amount;
	private String agree_yn;
	
//	/**
//	 * LP 홀더 등록 후 최종처리로 HOLDER_NO를 정렬 수정할 때 첫 SEQ에서 1을 뺀 값을 SEQ에서 빼기.
//	 * e.g. 첫 SEQ가 13이면 13 - 12 = 1 이 값을 HOLDER_NO로 설정.
//	 * 두번째 SEQ는 14이고 14 - 12 = 2 이므로 HOLDER_NO는 2가 됨.
//	 */
//	private int first_seq_minus_one;
	
//	private String vote_time;
//	private String reg_id;
//	private String reg_time;
//	private String mod_id;
//	private String mod_time;
//	private String del_id;
//	private String del_time;
}
