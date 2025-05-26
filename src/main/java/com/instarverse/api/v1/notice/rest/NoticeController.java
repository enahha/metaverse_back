package com.instarverse.api.v1.notice.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.instarverse.api.v1.common.util.CommonUtil;
import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.notice.mapper.NoticeMapper;
import com.instarverse.api.v1.notice.vo.NoticeVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class NoticeController {
	
	// private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);
	
	@Autowired
	private NoticeMapper noticeMapper;
	
	/**
	 * 공지사항 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/notice/selectNoticeListLastPageNum")
	public int selectNoticeListLastPageNum(@RequestParam String uid, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		NoticeVo noticeVo = new NoticeVo();
		noticeVo.setKeyword(keyword);
		noticeVo.setPageSize(pageSize);
		return this.noticeMapper.selectNoticeListLastPageNum(noticeVo);
	}
	
	/**
	 * 공지사항 요청  리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/notice/selectNoticeList")
	public List<NoticeVo> selectNoticeList(@RequestParam String uid, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		// 페이징 처리
		NoticeVo noticeVo = new NoticeVo();
		noticeVo.setKeyword(keyword);
		noticeVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		noticeVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		return this.noticeMapper.selectNoticeList(noticeVo);
	}
	
	/**
	 * 공지사항 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/notice/selectNotice")
	public NoticeVo selectNotice(@RequestParam String uid, @RequestParam int seq) throws Exception {
		NoticeVo noticeVo = new NoticeVo();
//		noticeVo.setUid(uid);
		noticeVo.setSeq(seq);
		return this.noticeMapper.selectNotice(noticeVo);
	}
	
	/**
	 * 공지사항 등록
	 * 
	 * @param noticeVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/notice/insertNotice")
	public CommonVo insertNotice(@RequestBody NoticeVo noticeVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			noticeVo.setDisplay_yn("Y");
			noticeVo.setReg_id(noticeVo.getUid());
			int resultCount = this.noticeMapper.insertNotice(noticeVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("insertNotice failed.");
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
	 * 공지사항 수정
	 * 
	 * @param noticeVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/notice/updateNotice")
	public CommonVo updateNotice(@RequestBody NoticeVo noticeVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			noticeVo.setMod_id(noticeVo.getUid());
			int resultCount = this.noticeMapper.updateNotice(noticeVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateNotice failed.");
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
	 * 공지사항 삭제
	 * 
	 * @param noticeVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/notice/deleteNotice")
	public CommonVo deleteNotice(@RequestBody NoticeVo noticeVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			noticeVo.setDel_id(noticeVo.getUid());
			int resultCount = this.noticeMapper.deleteNotice(noticeVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("deleteNotice failed.");
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
