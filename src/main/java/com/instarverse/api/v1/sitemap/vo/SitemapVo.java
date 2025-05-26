package com.instarverse.api.v1.sitemap.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SitemapVo {
	
	// 사이트맵
	private String seq;
	private String loc;
	private String lastmod;
	private String changefreq;
	private String priority;
}
