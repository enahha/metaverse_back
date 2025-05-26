package com.instarverse.api.v1.user.rest;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.instarverse.api.v1.common.Constant;
import com.instarverse.api.v1.common.rest.LoginLogController;
import com.instarverse.api.v1.common.util.StringUtil;
import com.instarverse.api.v1.common.util.SystemUtil;
import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.user.mapper.UserMapper;
import com.instarverse.api.v1.user.vo.UserVo;

import jakarta.servlet.http.HttpServletRequest;

//import kr.oneon.sellerz.coin.mapper.CoinMapper;
//import kr.oneon.sellerz.coin.vo.CoinVo;
//import kr.oneon.sellerz.common.util.StringUtil;
//import kr.oneon.sellerz.common.util.UserUtil;
//import kr.oneon.sellerz.follow.mapper.FollowMapper;
//import kr.oneon.sellerz.follow.vo.FollowVo;
//import kr.oneon.sellerz.seller.mapper.SellerMapper;
//import kr.oneon.sellerz.seller.vo.SellerVo;
//import kr.oneon.sellerz.sellermanager.mapper.SellerManagerMapper;
//import kr.oneon.sellerz.sellermanager.vo.SellerManagerVo;
//import springfox.documentation.annotations.ApiIgnore;

//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class UserController {
	
	// private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private LoginLogController logController;
	
	/**
	 * 사용자 정보 존재 유무 체크
	 * @param wallet_address
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/user/selectUserByWalletAddress")
	public UserVo selectUserByWalletAddress(@RequestParam String wallet_address) throws Exception {
		System.out.println("selectUserByWalletAddress called");
		
		if (StringUtil.isEmpty(wallet_address)) {
			return null;
		}
		
		UserVo userVo = this.userMapper.selectUserByWalletAddress(wallet_address);
		if (userVo != null) {
			userVo.setPwd(null);
		}
		// System.out.println(userVo);
		return userVo;
	}
	
//	@CrossOrigin(origins="*")
	@GetMapping("/user/selectUser")
	public UserVo selectUser(@RequestParam String uid) throws Exception {
		System.out.println("selectUser called");
		
		if (StringUtil.isEmpty(uid)) {
			return new UserVo();
		}
		
		UserVo userVo = this.userMapper.selectUser(uid);
		if (userVo != null) {
			userVo.setPwd(null);
			// System.out.println(userVo);
		}
		return userVo;
	}

	/**
	 * 회원정보 등록 (회원가입)
	 * 
	 * @param userVo
	 * @return userVo
	 * @throws Exception
	 */
	@PostMapping("/user/insertUser")
	public CommonVo insertUser(@RequestBody UserVo userVo, HttpServletRequest request) throws Exception {
		UserVo returnUserVo = new UserVo();
		
		// 1. 회원 등록
		// 프로필 이미지 디폴트 설정
//		if (StringUtil.isEmpty(userVo.getProfile_image())) {
//			userVo.setProfile_image(Constant.DEFAULT_PROFILE_IMAGE_URL);
//			userVo.setThumbnail_image(Constant.DEFAULT_PROFILE_IMAGE_URL); // 썸네일 이미지
//		}
		
		// 1.2 자동로그인용 32자리 인증키 생성
		String authKey = UUID.randomUUID().toString().replace("-", "");
		userVo.setAuth_key(authKey);
		
		// 사용자 등록
		int resultCount = this.userMapper.insertUser(userVo);
		if (resultCount == 0) {
			returnUserVo.setResultCd("FAIL"); // 회원 등록 에러
			returnUserVo.setResultMsg("Registration failed.");
		} else {
			// 로그인 로그 등록
			int count = this.logController.insertLoginLog(userVo.getUid(), Constant.LOGIN_LOG_CD_JOIN, SystemUtil.getRealIp(request));
			if (count < 1) {
				userVo.setResultMsg("Login log insert failed.");
			}
			
			// 회원정보 조회
			returnUserVo = this.userMapper.selectUser(userVo.getUid());
			returnUserVo.setResultCd("SUCCESS");
		}
		return returnUserVo;
	}
	
	/**
	 * 비밀번호 변경
	 * 
	 * @param userVo
	 * @return userVo
	 * @throws Exception
	 */
	@PostMapping("/user/updateUserPwd")
	public CommonVo updateUserPwd(@RequestBody UserVo userVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		// 1.2 자동로그인용 32자리 인증키 생성
		String authKey = UUID.randomUUID().toString().replace("-", "");
		userVo.setAuth_key(authKey);
		
		// 비밀번호 변경
		int resultCount = this.userMapper.updateUserPwd(userVo);
		if (resultCount == 0) {
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg("Change password failed.");
		}
		return commonVo;
	}
	
//	
//	/**
//	 * 프로필 이미지 수정
//	 * 
//	 * @param userVo
//	 * @return userVo
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "/user/updateUserProfileImage" , method = {RequestMethod.POST})
//	public int updateUserProfileImage(@RequestBody UserVo userVo) throws Exception {
//		// 프로필 이미지 수정
//		return this.userMapper.updateUserProfileImage(userVo);
//	}
//	
	/**
	 * 회원정보 수정
	 * 
	 * @param userVo
	 * @return userVo
	 * @throws Exception
	 */
	@PostMapping("/user/updateUser")
	public UserVo updateUser(@RequestBody UserVo userVo) throws Exception {
		userVo.setResultCd("SUCCESS");
//		if (StringUtil.isNotEmpty(userVo.getPwd())) { // 비밀번호가 있는 경우
//			// 비밀번호 암호화
//			userVo.setPwd(SecurityUtil.getSHA512(userVo.getPwd()));
//		}
		userVo.setMod_id(userVo.getUid());
		
		// 회원정보 수정
		int resultCount = this.userMapper.updateUser(userVo);
		if (resultCount == 0) {
			userVo.setResultCd("FAIL"); // 비정상처리
			userVo.setResultMsg("updateUser failed.");
		}
		return userVo;
	}
	
	/**
	 * 회원정보 수정 - 계좌 정보
	 * 
	 * @param userVo
	 * @return userVo
	 * @throws Exception
	 */
	@PostMapping("/user/updateUserAccount")
	public UserVo updateUserAccount(@RequestBody UserVo userVo) throws Exception {
		userVo.setResultCd("SUCCESS");
//		if (StringUtil.isNotEmpty(userVo.getPwd())) { // 비밀번호가 있는 경우
//			// 비밀번호 암호화
//			userVo.setPwd(SecurityUtil.getSHA512(userVo.getPwd()));
//		}
		userVo.setMod_id(userVo.getUid());
		
		// 회원정보 수정
		int resultCount = this.userMapper.updateUserAccount(userVo);
		if (resultCount == 0) {
			userVo.setResultCd("FAIL"); // 비정상처리
			userVo.setResultMsg("updateUser failed.");
		}
		return userVo;
	}
//	
//	@RequestMapping(value = "/user/selectUser" , method = {RequestMethod.POST})
//	public UserVo selectUser(@RequestBody UserVo userVo) throws Exception {
//		return this.userMapper.selectUser(userVo.getUid());
//	}
//	
//	// 사용자 홈 정보 조회
//	@RequestMapping(value = "/user/selectUserHome" , method = {RequestMethod.GET})
//	public UserVo selectUserHome(@RequestParam String uid) throws Exception {
//		// 1. 유저정보 조회
//		UserVo userVo = this.userMapper.selectUser(uid);
//		if (userVo == null) {
//			return userVo;
//		}
//		// 2. 각종 카운트 조회
//		// 상품수, 게시물수, 모임수, 팔로워수, 팔로잉수, 오늘방문자수, 전체방문자수 조회
//		UserVo countVo = this.userMapper.selectUserCounts(uid);
//		
//		// 3. 코인랭킹 조회
//		CoinVo coinVo = this.coinMapper.selectCoinRanking(uid);
//		
//		// 4. 팔로우 여부 조회
//		if (!uid.equals(UserUtil.getUidFromSession())) {
//			FollowVo followVo = new FollowVo();
//			followVo.setUid_from(UserUtil.getUidFromSession());
//			followVo.setUid_to(uid);
//			userVo.setFollow_yn(this.followMapper.selectFollowYn(followVo));
//		}
//		
//		// 5. 유저 정보 설정
//		userVo.setCount_contents(countVo.getCount_contents()); // 2의 카운트들 설정
//		userVo.setCount_product(countVo.getCount_product());
//		userVo.setCount_circle(countVo.getCount_circle());
//		userVo.setCount_follow_from(countVo.getCount_follow_from());
//		userVo.setCount_follow_to(countVo.getCount_follow_to());
//		userVo.setCoin(coinVo.getCoin()); // 3의 코인
//		userVo.setRanking(coinVo.getRanking()); // 3의 코인랭킹
//		
//		return userVo;
//	}
//	
//	/*
//	 * 새로고침(F5) 리로드시 사용자 정보 조회
//	 */
//	@RequestMapping(value = "/user/reload" , method = {RequestMethod.POST})
//	public UserVo reload(@RequestBody UserVo userVo, HttpServletRequest request) throws Exception {
//		
//		// 사용자 조회
//		// UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.SESSION_USER_INFO);
//		userVo = this.userMapper.selectUser(userVo.getUid());
//		if (userVo != null && StringUtil.isNotEmpty(userVo.getSeq())) {
//			// 세션에 사용자 정보 저장
//			request.getSession().setAttribute(Constant.SESSION_USER_INFO, userVo);
//			// 결과코드 : 성공
//			userVo.setResultCd("SUCCESS");
//		}
//		return userVo;
//	}
}
