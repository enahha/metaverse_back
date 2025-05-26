package com.instarverse.api.v1.item.mapper;

import java.util.List;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.item.vo.ItemVo;

public interface ItemMapper {
	// 아이템 리스트 조회
	public int selectItemListLastPageNum(CommonVo commonVo) throws Exception;
	public List<ItemVo> selectItemList(CommonVo commonVo) throws Exception;
	
	// 아이템 조회
	public ItemVo selectItem(ItemVo itemVo) throws Exception;
	
	// 아이템 등록
	public int insertItem(ItemVo itemVo) throws Exception;
	
	// 아이템 수정
	public int updateItem(ItemVo itemVo) throws Exception;
	
	// 아이템 삭제
	public int deleteItem(ItemVo itemVo) throws Exception;
	
	// 가격 조회
	public String selectPrice(int seq) throws Exception;
}
