package com.instarverse.api.v1.inquiry.rest;

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
import com.instarverse.api.v1.inquiry.mapper.InquiryMapper;
import com.instarverse.api.v1.inquiry.vo.InquiryVo;

@RestController
@Transactional
@RequestMapping(value = "/api")
public class InquiryController {
	
	// private static final Logger logger = LoggerFactory.getLogger(InquiryController.class);
	
	@Autowired
	private InquiryMapper inquiryMapper ;
	
	/**
	 * 문의사항 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/inquiry/selectInquiryListLastPageNum")
	public int selectInquiryListLastPageNum(@RequestParam String uid, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		InquiryVo inquiryVo = new InquiryVo();
		inquiryVo.setKeyword(keyword);
		inquiryVo.setPageSize(pageSize);
		return this.inquiryMapper.selectInquiryListLastPageNum(inquiryVo);
	}
	
	/**
	 * 문의사항 요청  리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/inquiry/selectInquiryList")
	public List<InquiryVo> selectInquiryList(@RequestParam String uid, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		// 페이징 처리
		InquiryVo inquiryVo = new InquiryVo();
		inquiryVo.setKeyword(keyword);
		inquiryVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		inquiryVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		return this.inquiryMapper.selectInquiryList(inquiryVo);
	}
	
	/**
	 * 문의사항 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/inquiry/selectInquiry")
	public InquiryVo selectInquiry(@RequestParam String uid, @RequestParam int seq) throws Exception {
		InquiryVo inquiryVo = new InquiryVo();
//		inquiryVo.setUid(uid);
		inquiryVo.setSeq(seq);
		return this.inquiryMapper.selectInquiry(inquiryVo);
	}
	
	/**
	 * 문의사항 등록
	 * 
	 * @param inquiryVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/inquiry/insertInquiry")
	public CommonVo insertInquiry(@RequestBody InquiryVo inquiryVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
//			inquiryVo.setDisplay_yn("Y");
			inquiryVo.setReg_id(inquiryVo.getUid());
			int resultCount = this.inquiryMapper.insertInquiry(inquiryVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("insertInquiry failed.");
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
	 * 문의사항 수정
	 * 
	 * @param inquiryVo
	 * @return commonVo
	 * @throws Exception
	 */
//	@PostMapping("/inquiry/updateInquiry")
//	public CommonVo updateInquiry(@RequestBody InquiryVo inquiryVo) throws Exception {
//		CommonVo commonVo = new CommonVo();
//		commonVo.setResultCd("SUCCESS");
//		try {
//			inquiryVo.setMod_id(inquiryVo.getUid());
//			int resultCount = this.inquiryMapper.updateInquiry(inquiryVo);
//			if (resultCount == 0) {
//				commonVo.setResultCd("FAIL");
//				commonVo.setResultMsg("updateInquiry failed.");
//				return commonVo;
//			}
//		} catch (Exception e) {
//			// 결과코드 : 실패
//			commonVo.setResultCd("FAIL");
//			commonVo.setResultMsg(e.toString());
//		}
//		return commonVo;
//	}
	
	/**
	 * 문의사항 삭제
	 * 
	 * @param inquiryVo
	 * @return commonVo
	 * @throws Exception
	 */
//	@PostMapping("/inquiry/deleteInquiry")
//	public CommonVo deleteInquiry(@RequestBody InquiryVo inquiryVo) throws Exception {
//		CommonVo commonVo = new CommonVo();
//		commonVo.setResultCd("SUCCESS");
//		try {
//			inquiryVo.setDel_id(inquiryVo.getUid());
//			int resultCount = this.inquiryMapper.deleteInquiry(inquiryVo);
//			if (resultCount == 0) {
//				commonVo.setResultCd("FAIL");
//				commonVo.setResultMsg("deleteInquiry failed.");
//				return commonVo;
//			}
//		} catch (Exception e) {
//			// 결과코드 : 실패
//			commonVo.setResultCd("FAIL");
//			commonVo.setResultMsg(e.toString());
//		}
//		return commonVo;
//	}
}
