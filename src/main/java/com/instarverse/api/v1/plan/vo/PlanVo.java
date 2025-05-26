package com.instarverse.api.v1.plan.vo;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.payment.vo.PayBaseInfoMobileVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PlanVo extends CommonVo {
    private int seq;
    private int type;
    private String name;
    private int price;
    private int storage;
    private int exhibition_count;
    private int order_no;
    private int period;
    private String detail_storage;
    private String detail_exhibition_count;
    private String image_url;
    private int discount_rate;
    private String display_yn;
    private String del_yn;
}
