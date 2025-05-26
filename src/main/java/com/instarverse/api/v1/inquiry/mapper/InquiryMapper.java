package com.instarverse.api.v1.inquiry.mapper;

import java.util.List;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.inquiry.vo.InquiryVo;

public interface InquiryMapper {
	// 문의사항 리스트 조회
	public int selectInquiryListLastPageNum(CommonVo commonVo) throws Exception;
	public List<InquiryVo> selectInquiryList(CommonVo commonVo) throws Exception;
	
	// 문의사항 조회
	public InquiryVo selectInquiry(InquiryVo InquiryVo) throws Exception;
	
	// 문의사항 등록
	public int insertInquiry(InquiryVo InquiryVo) throws Exception;
	
	// 문의사항 수정
//	public int updateInquiry(InquiryVo InquiryVo) throws Exception;
	
	// 문의사항 삭제
//	public int deleteInquiry(InquiryVo InquiryVo) throws Exception;
}
