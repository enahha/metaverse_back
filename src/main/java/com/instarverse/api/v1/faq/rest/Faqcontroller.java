package com.instarverse.api.v1.faq.rest;

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
import com.instarverse.api.v1.faq.mapper.FaqMapper;
import com.instarverse.api.v1.faq.vo.FaqVo;

@RestController
@Transactional
@RequestMapping(value = "/api")
public class Faqcontroller {
	
	// private static final Logger logger = LoggerFactory.getLogger(FaqController.class);
	
	@Autowired
	private FaqMapper faqMapper;
	
	/**
	 * FAQ 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/faq/selectFaqListLastPageNum")
	public int selectFaqListLastPageNum(@RequestParam String uid, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		FaqVo faqVo = new FaqVo();
		faqVo.setKeyword(keyword);
		faqVo.setPageSize(pageSize);
		return this.faqMapper.selectFaqListLastPageNum(faqVo);
	}
	
	/**
	 * FAQ 요청  리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/faq/selectFaqList")
	public List<FaqVo> selectFaqList(@RequestParam String uid, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		// 페이징 처리
		FaqVo faqVo = new FaqVo();
		faqVo.setKeyword(keyword);
		faqVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		faqVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		return this.faqMapper.selectFaqList(faqVo);
	}
	
	/**
	 * FAQ 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/faq/selectFaq")
	public FaqVo selectFaq(@RequestParam String uid, @RequestParam int seq) throws Exception {
		FaqVo faqVo = new FaqVo();
//		FaqVo.setUid(uid);
		faqVo.setSeq(seq);
		return this.faqMapper.selectFaq(faqVo);
	}
	
	/**
	 * FAQ 등록
	 * 
	 * @param FaqVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/faq/insertFaq")
	public CommonVo insertFaq(@RequestBody FaqVo faqVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			faqVo.setDisplay_yn("Y");
			faqVo.setReg_id(faqVo.getUid());
			int resultCount = this.faqMapper.insertFaq(faqVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("insertFaq failed.");
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
	 * FAQ 수정
	 * 
	 * @param FaqVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/faq/updateFaq")
	public CommonVo updateFaq(@RequestBody FaqVo faqVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			faqVo.setMod_id(faqVo.getUid());
			int resultCount = this.faqMapper.updateFaq(faqVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateFaq failed.");
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
	 * FAQ 삭제
	 * 
	 * @param FaqVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/faq/deleteFaq")
	public CommonVo deleteFaq(@RequestBody FaqVo faqVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			faqVo.setDel_id(faqVo.getUid());
			int resultCount = this.faqMapper.deleteFaq(faqVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("deleteFaq failed.");
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
