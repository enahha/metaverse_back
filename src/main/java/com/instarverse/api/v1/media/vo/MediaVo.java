package com.instarverse.api.v1.media.vo;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.mymedia.vo.MyMediaVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MediaVo extends CommonVo {
	private int seq;
	private int project_seq;
	private int my_media_seq;
	private String nft_id;
	private int order_no;
	private String sale_yn;
	private String price;
	private String del_yn;
}
