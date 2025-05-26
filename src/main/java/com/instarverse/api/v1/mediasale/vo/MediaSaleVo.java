package com.instarverse.api.v1.mediasale.vo;

import com.instarverse.api.v1.common.vo.CommonVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MediaSaleVo extends CommonVo {
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
    private String del_yn;
    
    // 정산 완료(Y) || 정산 미완료(N)
    private boolean Done;
}
