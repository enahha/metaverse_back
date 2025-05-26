package com.instarverse.api.v1.mediasale.vo;

import com.instarverse.api.v1.common.vo.CommonVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MyMediaSaleVo extends CommonVo {
	private int seq;
    private String market_cd;
    private String market_name;
    private String sale_price;
    private String payment_currency;
    private String contract_address;
    private String nft_id;
    private String settle_in;
    private String settle_in_date;
    private String settle_out;
    private String settle_out_date;
    
    private int my_media_seq;
    private String uid;
    private String type;
    private String url;
    private int order_no;
    private String title;
    private String subtitle;
    private String description;
    private String sale_yn;
    private String price;
    private String created_at;
    private String size;
    private String materials;
    private String del_yn;
    private String project_title;
    private String project_subtitle;
    private String project_description;
}
