package com.instarverse.api.v1.mymedia.rest;

import java.util.List;
import java.util.Map;

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
import com.instarverse.api.v1.mymedia.mapper.MyMediaMapper;
import com.instarverse.api.v1.mymedia.vo.MyMediaVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class MyMediaController {
	
	// private static final Logger logger = LoggerFactory.getLogger(MyMediaController.class);
	
	@Autowired
	private MyMediaMapper myMediaMapper;
	
	/**
	 * 마이미디어 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/mymedia/selectMyMediaListLastPageNum")
	public int selectMyMediaListLastPageNum(@RequestParam String uid, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		MyMediaVo myMediaVo = new MyMediaVo();
		myMediaVo.setKeyword(keyword);
		myMediaVo.setReg_id(uid);
		myMediaVo.setUid(uid);
		myMediaVo.setPageSize(pageSize);
		return this.myMediaMapper.selectMyMediaListLastPageNum(myMediaVo);
	}
	
	/**
	 * 마이미디어 리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/mymedia/selectMyMediaList")
	public List<MyMediaVo> selectMyMediaList(@RequestParam String uid, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		// 페이징 처리
		MyMediaVo myMediaVo = new MyMediaVo();
		myMediaVo.setKeyword(keyword);
		myMediaVo.setUid(uid);
		myMediaVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		myMediaVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		return this.myMediaMapper.selectMyMediaList(myMediaVo);
	}
	
	/**
	 * 마이미디어 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/mymedia/selectMyMedia")
	public MyMediaVo selectMyMedia(@RequestParam String uid, @RequestParam int seq) throws Exception {
		MyMediaVo myMediaVo = new MyMediaVo();
		myMediaVo.setSeq(seq);
		return this.myMediaMapper.selectMyMedia(myMediaVo);
	}
	
	/**
	 * 마이미디어 등록
	 * 
	 * @param myMediaVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/mymedia/insertMyMedia")
	public CommonVo insertMyMedia(@RequestBody MyMediaVo myMediaVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			myMediaVo.setReg_id(myMediaVo.getUid());
			myMediaVo.setDel_yn("N");
			
			// 태그리스트 변환
			List<String> tagList = myMediaVo.getTag_list();
			if (tagList != null || !tagList.isEmpty()) {
				String tag = String.join(",", tagList);
				myMediaVo.setTag(tag);
			}
			
			int resultCount = this.myMediaMapper.insertMyMedia(myMediaVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("insertMyMedia failed.");
				return commonVo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
	
	/**
	 * 마이미디어 등록(리스트)
	 * 
	 * @param myMediaVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/mymedia/insertMyMediaList")
	public CommonVo insertMyMediaList(@RequestBody List<Map<String, Object>> itemList) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			for(Map<String, Object> item : itemList) {
				MyMediaVo myMediaVo = new MyMediaVo();
				myMediaVo.setReg_id((String)item.get("uid"));
				myMediaVo.setUid((String)item.get("uid"));
				myMediaVo.setTitle((String)item.get("id"));
				myMediaVo.setUrl((String)item.get("media_url"));
				myMediaVo.setDescription((String)item.get("caption"));
				myMediaVo.setType((String)item.get("media_type"));
				// 년도만 추출
				String year = ((String)item.get("timestamp")).substring(0, 4);
				myMediaVo.setCreated_at(year);
				
				// 그 외 기본설정
				myMediaVo.setSale_yn("N");
				myMediaVo.setDel_yn("N");
				
				int resultCount = this.myMediaMapper.insertMyMedia(myMediaVo);
				if (resultCount == 0) {
					commonVo.setResultCd("FAIL");
					commonVo.setResultMsg("insertMyMedia failed.");
					return commonVo;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
	
	/**
	 * 마이미디어 수정
	 * 
	 * @param myMediaVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/mymedia/updateMyMedia")
	public CommonVo updateMyMedia(@RequestBody MyMediaVo myMediaVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			myMediaVo.setMod_id(myMediaVo.getUid());
			
			// 태그리스트 변환
			List<String> tagList = myMediaVo.getTag_list();
			if (tagList != null || !tagList.isEmpty()) {
				String tag = String.join(",", tagList);
				myMediaVo.setTag(tag);
			}
			
			int resultCount = this.myMediaMapper.updateMyMedia(myMediaVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateMyMedia failed.");
				return commonVo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
	
	/**
	 * 마이미디어 삭제
	 * 
	 * @param myMediaVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/mymedia/deleteMyMedia")
	public CommonVo deleteMyMedia(@RequestBody MyMediaVo myMediaVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			myMediaVo.setDel_id(myMediaVo.getUid());
			int resultCount = this.myMediaMapper.deleteMyMedia(myMediaVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("deleteMyMedia failed.");
				return commonVo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
}
