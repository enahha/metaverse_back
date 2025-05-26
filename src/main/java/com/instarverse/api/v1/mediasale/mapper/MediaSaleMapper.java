package com.instarverse.api.v1.mediasale.mapper;

import java.util.List;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.mediasale.vo.MediaSaleVo;
import com.instarverse.api.v1.mediasale.vo.MyMediaSaleVo;

public interface MediaSaleMapper {	
	// 작품 판매 판매 조회
	public int selectMediaSaleListLastPageNum(CommonVo commonVo) throws Exception;
	public List<MediaSaleVo> selectMediaSaleList(CommonVo commonVo) throws Exception;
	
	// 작품 판매 조회
	public MediaSaleVo selectMediaSale(MediaSaleVo mediaSaleVo) throws Exception;
	
	// 작품 판매 등록
	public int insertMediaSale(MediaSaleVo mediaSaleVo) throws Exception;
	
	// 작품 판매 수정
	public int updateMediaSale(MediaSaleVo mediaSaleVo) throws Exception;
	
	// 작품 판매 삭제
	public int deleteMediaSale(MediaSaleVo mediaSaleVo) throws Exception;
	
	// 나의 작품 판매 리스트 조회
	public int selectMyMediaSaleListLastPageNum(CommonVo commonVo) throws Exception;
	public List<MyMediaSaleVo> selectMyMediaSaleList(CommonVo commonVo) throws Exception;

	// 정산 예정 금액 조회
	public int selectMyMediaSaleSettleIn(MediaSaleVo mediaSaleVo) throws Exception;
	
	// 정산 내역 조회
	public int selectMyMediaSaleSettlOutLastPageNum(CommonVo commonVo) throws Exception;
	public List<MediaSaleVo> selectMyMediaSaleSettlOutList(CommonVo commonVo) throws Exception;
}
