package com.instarverse.api.v1.media.mapper;

import java.util.List;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.media.vo.MediaInfoVo;
import com.instarverse.api.v1.media.vo.MediaVo;
 
public interface MediaMapper {
	
	// 전시회  seq로 미디어 리스트 조회
	public List<MediaInfoVo> selectMediaListByProjectSeq(MediaInfoVo mediaInfoVo) throws Exception;
	
	// json용 media List 조회
	public List<MediaInfoVo> selectJsonMediaInfoList(MediaInfoVo mediaInfoVo) throws Exception;
	
	// 미디어 리스트 조회(페이징)
	public int selectMediaListLastPageNumPaging(CommonVo commonVo) throws Exception;
	public List<MediaVo> selectMediaListPaging(CommonVo commonVo) throws Exception;
	
	// 미디어 리스트 조회
	public List<MediaVo> selectMediaList(CommonVo commonVo) throws Exception;
	
	// 미디어 조회
	public MediaVo selectMedia(MediaVo mediaVo) throws Exception;
	
	// 미디어 등록
	public int insertMedia(MediaVo mediaVo) throws Exception;
	
	// 미디어 수정
	public int updateMedia(MediaVo mediaVo) throws Exception;
	
	// nft_id 업데이트
	public int updateMediaNftId(MediaVo mediaVo) throws Exception;
	
	// 미디어 삭제
	public int deleteMedia(MediaVo mediaVo) throws Exception;
	
	// 미디어 재등록
	public int restoreMedia(MediaVo mediaVo) throws Exception;
	
	// 마이 미디어 seq로 삭제
	public int deleteMediaByMyMediaSeq(MediaVo mediaVo) throws Exception;
	
	// 미디어 리스트 order_no 수정
	public int updateMediaOrderNoList(List<MediaInfoVo> mediaInfoList) throws Exception;
	
	// 미디어 리스트 order_no 최대값 가져오기
	public int getMaxOrderNo(int projectSeq) throws Exception;
	
	// 미디어 재등록을 위한 전체 리스트 조회
	public List<MediaInfoVo> selectMediaListAll(MediaInfoVo mediaInfoVo) throws Exception;
}
