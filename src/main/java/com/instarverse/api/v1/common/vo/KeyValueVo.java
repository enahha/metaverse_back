package com.instarverse.api.v1.common.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class KeyValueVo extends CommonVo {
	private int seq;
	private String key;
	private String value;
}
