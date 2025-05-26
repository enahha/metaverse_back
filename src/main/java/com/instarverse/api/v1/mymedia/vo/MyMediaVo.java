package com.instarverse.api.v1.mymedia.vo;

import java.util.List;

import com.instarverse.api.v1.common.vo.CommonVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MyMediaVo extends CommonVo {
    private int seq;
    private String uid;
    private String type;
    private String url;
    private String thumbnail_url;
    private int order_no;
    private String title;
    private String subtitle;
    private String description;
    private String sale_yn;
    private String price;
    private String created_at;
    private String size;
    private String materials;
    private String tag;
    private String del_yn;
    
    private List<String> tag_list;
    
    // media insert용 project_seq
    private int project_seq;
}
