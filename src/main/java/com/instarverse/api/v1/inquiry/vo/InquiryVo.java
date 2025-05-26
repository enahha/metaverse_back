package com.instarverse.api.v1.inquiry.vo;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.notice.vo.NoticeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class InquiryVo extends CommonVo {
    private int seq;
    private String nickname;
    private String email;
    private String contents;
}
