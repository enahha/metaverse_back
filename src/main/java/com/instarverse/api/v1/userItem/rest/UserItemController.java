package com.instarverse.api.v1.userItem.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.common.Constant;
import com.instarverse.api.v1.common.util.CommonUtil;
import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.userItem.mapper.UserItemMapper;
import com.instarverse.api.v1.userItem.vo.UserItemVo;

//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class UserItemController {
	
	// private static final Logger logger = LoggerFactory.getLogger(UserItemController.class);
	
	@Autowired
	private UserItemMapper userItemMapper;
	
	/**
	 * 소유 아이템 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/userItem/selectUserItemListLastPageNum")
	public int selectUserItemListLastPageNum(@RequestParam String uid, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		UserItemVo userItemVo = new UserItemVo();
		userItemVo.setKeyword(keyword);
		userItemVo.setPageSize(pageSize);
		return this.userItemMapper.selectUserItemListLastPageNum(userItemVo);
	}
	
	/**
	 * 소유 아이템 요청  리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/userItem/selectUserItemList")
	public List<UserItemVo> selectUserItemList(@RequestParam String uid, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		// 페이징 처리
		UserItemVo userItemVo = new UserItemVo();
		userItemVo.setKeyword(keyword);
		userItemVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		userItemVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		return this.userItemMapper.selectUserItemList(userItemVo);
	}
	
	/**
	 * 소유 아이템 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/userItem/selectUserItem")
	public UserItemVo selectUserItem(@RequestParam String uid, @RequestParam int seq) throws Exception {
		UserItemVo userItemVo = new UserItemVo();
//		userItemVo.setUid(uid);
		userItemVo.setSeq(seq);
		return this.userItemMapper.selectUserItem(userItemVo);
	}
	
	/**
	 * 유저별 소유 아이템 요청  리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/userItem/selectUserItemListByUid")
	public List<UserItemVo> selectUserItemListByUid(@RequestParam String uid) throws Exception {
		// 페이징 처리
		UserItemVo userItemVo = new UserItemVo();
		userItemVo.setUid(uid);
		return this.userItemMapper.selectUserItemListByUid(userItemVo);
	}
	
	/**
	 * 소유 아이템 등록
	 * 
	 * @param userItemVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/userItem/insertUserItem")
	public CommonVo insertUserItem(@RequestBody UserItemVo userItemVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			userItemVo.setReg_id(Constant.SYSTEM_ID_FOR_DB);
			userItemVo.setDel_yn("N");
			userItemVo.setIn_use(0);
			userItemVo.setUid(userItemVo.getUid());
			 
			int resultCount = this.userItemMapper.insertUserItem(userItemVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("insertUserItem failed.");
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
	 * 소유 아이템 수정
	 * 
	 * @param userItemVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/userItem/updateUserItem")
	public CommonVo updateUserItem(@RequestBody UserItemVo userItemVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			userItemVo.setMod_id(userItemVo.getUid());
			int resultCount = this.userItemMapper.updateUserItem(userItemVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateUserItem failed.");
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
	 * 소유 아이템 삭제
	 * 
	 * @param userItemVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/userItem/deleteUserItem")
	public CommonVo deleteUserItem(@RequestBody UserItemVo userItemVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			userItemVo.setDel_id(userItemVo.getUid());
			int resultCount = this.userItemMapper.deleteUserItem(userItemVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("deleteUserItem failed.");
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
