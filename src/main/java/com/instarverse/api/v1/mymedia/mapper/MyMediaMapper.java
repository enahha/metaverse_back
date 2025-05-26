package com.instarverse.api.v1.mymedia.mapper;

import java.util.List;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.mymedia.vo.MyMediaVo;
 
public interface MyMediaMapper {
	// 마이미디어 리스트 조회
	public int selectMyMediaListLastPageNum(CommonVo commonVo) throws Exception;
	public List<MyMediaVo> selectMyMediaList(CommonVo commonVo) throws Exception;
	
	// 마이미디어 조회
	public MyMediaVo selectMyMedia(MyMediaVo myMediaVo) throws Exception;
	
	// 마이미디어 등록
	public int insertMyMedia(MyMediaVo myMediaVo) throws Exception;
	
	// 마이미디어 수정
	public int updateMyMedia(MyMediaVo myMediaVo) throws Exception;
	
	// 마이미디어 삭제
	public int deleteMyMedia(MyMediaVo myMediaVo) throws Exception;	
}
