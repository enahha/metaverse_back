package com.instarverse.api.v1.item.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.common.util.CommonUtil;
import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.item.mapper.ItemMapper;
import com.instarverse.api.v1.item.vo.ItemVo;

//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class ItemController {
	
	// private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
	
	@Autowired
	private ItemMapper itemMapper;
	
	/**
	 * 아이템 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/item/selectItemListLastPageNum")
	public int selectItemListLastPageNum(
			@RequestParam String uid
			, @RequestParam int pageSize
			, @RequestParam String keyword
			, @RequestParam(required = false)String type
			, @RequestParam(required = false)String priceType) throws Exception {
		ItemVo itemVo = new ItemVo();
		itemVo.setKeyword(keyword);
		itemVo.setPageSize(pageSize);
		// 타입별 검색
		itemVo.setType(type); 	
		itemVo.setPrice_type(priceType);	// FREE / PAID
		return this.itemMapper.selectItemListLastPageNum(itemVo);
	}
	
	/**
	 * 아이템 요청  리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/item/selectItemList")
	public List<ItemVo> selectItemList(
			@RequestParam String uid
			, @RequestParam int pageNum
			, @RequestParam int pageSize
			, @RequestParam String keyword
			, @RequestParam(required = false)String type
			, @RequestParam(required = false)String priceType) throws Exception {
		// 페이징 처리
		ItemVo itemVo = new ItemVo();
		itemVo.setKeyword(keyword);
		itemVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		itemVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		// 타입별 검색
		itemVo.setType(type); 	
		itemVo.setPrice_type(priceType);	// FREE / PAID
		return this.itemMapper.selectItemList(itemVo);
	}
	
	/**
	 * 아이템 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/item/selectItem")
	public ItemVo selectItem(@RequestParam String uid, @RequestParam int seq) throws Exception {
		ItemVo itemVo = new ItemVo();
//		itemVo.setUid(uid);
		itemVo.setSeq(seq);
		return this.itemMapper.selectItem(itemVo);
	}
	
	/**
	 * 아이템 등록
	 * 
	 * @param itemVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/item/insertItem")
	public CommonVo insertItem(@RequestBody ItemVo itemVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
//			itemVo.setDisplay_yn("Y");
			itemVo.setReg_id(itemVo.getUid());
			int resultCount = this.itemMapper.insertItem(itemVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("insertItem failed.");
				return commonVo;
			}
		} catch (Exception e) {
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
	
	/**
	 * 아이템 수정
	 * 
	 * @param itemVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/item/updateItem")
	public CommonVo updateItem(@RequestBody ItemVo itemVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			itemVo.setMod_id(itemVo.getUid());
			int resultCount = this.itemMapper.updateItem(itemVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateItem failed.");
				return commonVo;
			}
		} catch (Exception e) {
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
	
	/**
	 * 아이템 삭제
	 * 
	 * @param itemVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/item/deleteItem")
	public CommonVo deleteItem(@RequestBody ItemVo itemVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			itemVo.setDel_id(itemVo.getUid());
			int resultCount = this.itemMapper.deleteItem(itemVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("deleteItem failed.");
				return commonVo;
			}
		} catch (Exception e) {
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
}
