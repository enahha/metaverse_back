package com.instarverse.api.v1.item.vo;

import com.instarverse.api.v1.common.vo.CommonVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ItemVo extends CommonVo {
	private int seq;
	private String name;
	private String type;
	private String price;
	private String price_type;
	private int display_maximum;
	private String description;
	private String description_ko;
	private String url;
	private String del_yn;
}
