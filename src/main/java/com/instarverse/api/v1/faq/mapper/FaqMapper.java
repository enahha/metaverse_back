package com.instarverse.api.v1.faq.mapper;

import java.util.List;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.faq.vo.FaqVo;

public interface FaqMapper {
	// Faq 리스트 조회
	public int selectFaqListLastPageNum(CommonVo commonVo) throws Exception;
	public List<FaqVo> selectFaqList(CommonVo commonVo) throws Exception;
	
	// Faq 조회
	public FaqVo selectFaq(FaqVo faqVo) throws Exception;
	
	// Faq 등록
	public int insertFaq(FaqVo faqVo) throws Exception;
	
	// Faq 수정
	public int updateFaq(FaqVo faqVo) throws Exception;
	
	// Faq 삭제
	public int deleteFaq(FaqVo faqVo) throws Exception;
}
