package com.instarverse.api.v1.media.vo;

import com.instarverse.api.v1.mymedia.vo.MyMediaVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MediaInfoVo {
	private int seq;
	private int my_media_seq;
	private int project_seq;
	private String nft_id;
	private int order_no;
	private String sale_yn;
	private String price;
	private String del_yn;
	private String uid;
	private String type;
	private String url;
	private String thumbnail_url;
	private String title;
	private String subtitle;
	private String description;
	private String created_at;
	private String size;
	private String materials;
	
	private int last_nft_id;
	private String nft_market_url;
}
