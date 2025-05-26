package com.instarverse.api.v1.twitter.vo;

import com.instarverse.api.v1.common.vo.CommonVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TwitterVo extends CommonVo {
	
	private String user_id;
	private String tweet_id;
	private String following_yn;
	private String retweeted_yn;
	private String liking_yn;
	
}
