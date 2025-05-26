package com.instarverse.api.v1.userItem.mapper;

import java.util.List;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.userItem.vo.UserItemVo;

public interface UserItemMapper {
	
	// 소유 아이템 리스트 조회
	public int selectUserItemListLastPageNum(CommonVo commonVo) throws Exception;
	public List<UserItemVo> selectUserItemList(CommonVo commonVo) throws Exception;
	
	// 유저별 소유 아이템 리스트 조회
	public List<UserItemVo> selectUserItemListByUid(UserItemVo UserItemVo) throws Exception;
	
	// 소유 아이템 조회
	public UserItemVo selectUserItem(UserItemVo userItemVo) throws Exception;
	
	// 소유 아이템 등록
	public int insertUserItem(UserItemVo userItemVo) throws Exception;
	
	// 소유 아이템 수정
	public int updateUserItem(UserItemVo userItemVo) throws Exception;
	
	// 소유 아이템 삭제
	public int deleteUserItem(UserItemVo userItemVo) throws Exception;

}
