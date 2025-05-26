package com.instarverse.api.v1.mediasale.rest;

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
import com.instarverse.api.v1.mediasale.mapper.MediaSaleMapper;
import com.instarverse.api.v1.mediasale.vo.MediaSaleVo;
import com.instarverse.api.v1.mediasale.vo.MyMediaSaleVo;

@RestController
@Transactional
@RequestMapping(value = "/api")
public class MediaSaleController {
	
	@Autowired
	private MediaSaleMapper mediaSaleMapper;
	
	/**
	 * 작품 판매 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/mediaSale/selectMediaSaleListLastPageNum")
	public int selectMediaSaleListLastPageNum(@RequestParam String uid, @RequestParam int pageSize, @RequestParam String keyword, @RequestParam boolean done) throws Exception {
		MediaSaleVo mediaSaleVo = new MediaSaleVo();
		mediaSaleVo.setKeyword(keyword);
		mediaSaleVo.setPageSize(pageSize);
		mediaSaleVo.setDone(done);
		return this.mediaSaleMapper.selectMediaSaleListLastPageNum(mediaSaleVo);
	}
	
	/**
	 * 작품 판매 요청  리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/mediaSale/selectMediaSaleList")
	public List<MediaSaleVo> selectMediaSaleList(@RequestParam String uid, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String keyword, @RequestParam boolean done) throws Exception {
		// 페이징 처리
		MediaSaleVo mediaSaleVo = new MediaSaleVo();
		mediaSaleVo.setKeyword(keyword);
		mediaSaleVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		mediaSaleVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		mediaSaleVo.setDone(done);
		return this.mediaSaleMapper.selectMediaSaleList(mediaSaleVo);
	}
	
	/**
	 * 작품 판매 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/mediaSale/selectMediaSale")
	public MediaSaleVo selectMediaSale(@RequestParam String uid, @RequestParam int seq) throws Exception {
		MediaSaleVo mediaSaleVo = new MediaSaleVo();
//		mediaSaleVo.setUid(uid);
		mediaSaleVo.setSeq(seq);
		return this.mediaSaleMapper.selectMediaSale(mediaSaleVo);
	}
	
	/**
	 * 작품 판매 등록
	 * 
	 * @param mediaSaleVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/mediaSale/insertMediaSale")
	public CommonVo insertMediaSale(@RequestBody MediaSaleVo mediaSaleVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			System.out.println("MediaSaleVo");
			System.out.println(mediaSaleVo);
			
			mediaSaleVo.setReg_id(mediaSaleVo.getUid());
			mediaSaleVo.setDel_yn("N");
			int resultCount = this.mediaSaleMapper.insertMediaSale(mediaSaleVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("insertMediaSale failed.");
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
	 * 작품 판매 수정
	 * 
	 * @param mediaSaleVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/mediaSale/updateMediaSale")
	public CommonVo updateMediaSale(@RequestBody MediaSaleVo mediaSaleVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			mediaSaleVo.setMod_id(mediaSaleVo.getUid());
			int resultCount = this.mediaSaleMapper.updateMediaSale(mediaSaleVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateMediaSale failed.");
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
	 * 작품 판매 삭제
	 * 
	 * @param mediaSaleVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/mediaSale/deleteMediaSale")
	public CommonVo deleteMediaSale(@RequestBody MediaSaleVo mediaSaleVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			mediaSaleVo.setDel_id(mediaSaleVo.getUid());
			int resultCount = this.mediaSaleMapper.deleteMediaSale(mediaSaleVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("deleteMediaSale failed.");
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
	 * 나의 작품 판매 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/mediaSale/selectMyMediaSaleListLastPageNum")
	public int selectMyMediaSaleListLastPageNum(@RequestParam String uid, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		MediaSaleVo mediaSaleVo = new MediaSaleVo();
		mediaSaleVo.setUid(uid);
		mediaSaleVo.setKeyword(keyword);
		mediaSaleVo.setPageSize(pageSize);
		return this.mediaSaleMapper.selectMyMediaSaleListLastPageNum(mediaSaleVo);
	}
	
	/**
	 * 나의 작품 판매 리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/mediaSale/selectMyMediaSaleList")
	public List<MyMediaSaleVo> selectMyMediaSaleList(@RequestParam String uid, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		// 페이징 처리
		MediaSaleVo mediaSaleVo = new MediaSaleVo();
		mediaSaleVo.setUid(uid);
		mediaSaleVo.setKeyword(keyword);
		mediaSaleVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		mediaSaleVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		return this.mediaSaleMapper.selectMyMediaSaleList(mediaSaleVo);
	}
	
	/**
	 * 정산 예정 금액 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/mediaSale/selectMyMediaSaleSettleIn")
	public int selectMyMediaSaleSettleIn(@RequestParam String uid) throws Exception {
		MediaSaleVo mediaSaleVo = new MediaSaleVo();
		mediaSaleVo.setUid(uid);
		return this.mediaSaleMapper.selectMyMediaSaleSettleIn(mediaSaleVo);
	}
	
	/**
	 * 나의 작품 판매 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/mediaSale/selectMyMediaSaleSettlOutLastPageNum")
	public int selectMyMediaSaleSettlOutLastPageNum(@RequestParam String uid, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		MediaSaleVo mediaSaleVo = new MediaSaleVo();
		mediaSaleVo.setUid(uid);
		mediaSaleVo.setKeyword(keyword);
		mediaSaleVo.setPageSize(pageSize);
		return this.mediaSaleMapper.selectMyMediaSaleSettlOutLastPageNum(mediaSaleVo);
	}
	
	/**
	 * 정산 내역 리스트 조회
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/mediaSale/selectMyMediaSaleSettlOutList")
	public List<MediaSaleVo> selectMyMediaSaleSettlOutList(@RequestParam String uid, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		MediaSaleVo mediaSaleVo = new MediaSaleVo();
		mediaSaleVo.setUid(uid);
		mediaSaleVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		mediaSaleVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		return this.mediaSaleMapper.selectMyMediaSaleSettlOutList(mediaSaleVo);
	}
}
