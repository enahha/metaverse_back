package com.instarverse.api.v1.payment.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inicis.std.util.SignatureUtil;
import com.instarverse.api.v1.common.Constant;
import com.instarverse.api.v1.item.mapper.ItemMapper;
import com.instarverse.api.v1.payment.vo.PayBaseInfoMobileVo;
import com.instarverse.api.v1.payment.vo.PayBaseInfoVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class PaymentController {
	
	// private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
	@Autowired
	private ItemMapper itemMapper;
	
	/**
	 * 결제 사용 여부 조회
	 * 
	 * @param uid
	 * @param pay_cd
	 * @return
	 * @throws Exception
	 */
//	@GetMapping("/payment/selectPriceAll")
//	public PriceVo selectPaymentAll(@RequestParam String uid, @RequestParam String payCd) throws Exception {
//		return this.paymentMapper.selectPriceAll(payCd);
//	}
//	
	
	/**
	 * 결제 기본 정보 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/payment/selectPayBaseInfoMobile")
	public PayBaseInfoMobileVo selectPayBaseInfoMobile(@RequestBody PayBaseInfoMobileVo payBaseInfoMobileVo) throws Exception {
		// 결과코드 : 성공
		payBaseInfoMobileVo.setResultCd("SUCCESS");
		try {
//			String device = payBaseInfoMobileVo.getDevice();
//			if ("android".equals(device)) {
//				
//			} else if ("ios".equals(device)) {
//				
//			} else if ("M".equals(device)) {
//				
//			} else {
//				payBaseInfoMobileVo.setResultCd("FAIL");
//				payBaseInfoMobileVo.setResultMsg("Device information is wrong.");
//			}
			
			// 가격 정보 조회
			String price = this.itemMapper.selectPrice(payBaseInfoMobileVo.getSeq());
			
			String mid = Constant.PAYMENT_MID;
			String oid = mid + "_"+ SignatureUtil.getTimestamp();
			
			payBaseInfoMobileVo.setP_mid(mid);
			payBaseInfoMobileVo.setP_oid(oid);
			payBaseInfoMobileVo.setP_amt(price);
			payBaseInfoMobileVo.setP_next_url(Constant.PAYMENT_NEXT_URL);
//			payBaseInfoMobileVo.setP_noti_url("");
		} catch (Exception e) {
			// 결과코드 : 실패
			payBaseInfoMobileVo.setResultCd("FAIL");
			payBaseInfoMobileVo.setResultMsg(e.toString());
		}
		return payBaseInfoMobileVo;
	}
	
	/**
	 * 결제 기본 정보 조회 - 데스크탑
	 * 
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/payment/selectPayBaseInfo")
	public PayBaseInfoVo selectPayBaseInfo(@RequestBody PayBaseInfoVo payBaseInfoVo) throws Exception {
		// 결과코드 : 성공
//		CommonVo commonVo = new CommonVo();
//		commonVo.setResultCd("SUCCESS");
//		PayBaseInfoVo returnVo = new PayBaseInfoVo();
		payBaseInfoVo.setResultCd("SUCCESS");
		try {
//			String goodname = payBaseInfoVo.getGoodname();
//			String buyername = payBaseInfoVo.getBuyername();
//			String buyertel = payBaseInfoVo.getBuyertel();
//			String buyeremail = payBaseInfoVo.getBuyeremail();
			
			// 가격 정보 조회
			// String price = Constant.PAYMENT_CREATE_PRICE;
			
		    // USD는 1달러 -> 100으로 표기
		    String priceStr = this.itemMapper.selectPrice(payBaseInfoVo.getSeq());
		    double priceDouble = Double.parseDouble(priceStr) * 100;
		    int priceInt = (int) priceDouble;
		    String price = Integer.toString(priceInt);
			
			String mid = Constant.PAYMENT_MID;
			// String gopaymethod = payBaseInfoVo.getGopaymethod();
			// mKey=mid(상점아이디)와 매칭되는 signkey 를 SHA256으로 hash한 값 입니다.
			String mKey = SignatureUtil.hash(Constant.PAYMENT_SIGN_KEY, "SHA-256");
			String oid = mid + "_"+ SignatureUtil.getTimestamp();
			String timestamp = SignatureUtil.getTimestamp();
			String version = Constant.PAYMENT_VERSION;
			String currency = Constant.PAYMENT_CURRENCY;
			String acceptmethod = Constant.ACCEPT_METHOD;
			String returnUrl = Constant.PAYMENT_RETURN_URL;
			// String closeUrl = Constant.PAYMENT_CLOSE_URL;
			String closeUrl = "";
			
			Map<String, String> signParam = new HashMap<String, String>();
			signParam.put("oid", oid);
			signParam.put("price", price);
			signParam.put("timestamp", timestamp);
			// signature 데이터 생성 (모듈에서 자동으로 signParam을 알파벳 순으로 정렬후 NVP 방식으로 나열해 hash)
			String signature = SignatureUtil.makeSignature(signParam);
			
			Map<String, String> verificationParam = new HashMap<String, String>();
			verificationParam.put("oid", oid);
			verificationParam.put("price", price);
			verificationParam.put("signKey", Constant.PAYMENT_SIGN_KEY);
			verificationParam.put("timestamp", timestamp);
			// signature 데이터 생성 (모듈에서 자동으로 signParam을 알파벳 순으로 정렬후 NVP 방식으로 나열해 hash)
			String verification = SignatureUtil.makeSignature(verificationParam);
			
			payBaseInfoVo.setPrice(price);
			payBaseInfoVo.setMid(mid);
			
			// mKey가 front에서 mkey로 소문자로 오는 현상 때문에 mKeyNew 항목 추가
			// payBaseInfoVo.setMKey(mKey);
			payBaseInfoVo.setMkeyNew(mKey);
			
			payBaseInfoVo.setOid(oid);
			payBaseInfoVo.setTimestamp(timestamp);
			payBaseInfoVo.setSignature(signature);
			payBaseInfoVo.setVerification(verification);
			payBaseInfoVo.setVersion(version);
			payBaseInfoVo.setCurrency(currency);
			payBaseInfoVo.setAcceptmethod(acceptmethod);
			payBaseInfoVo.setReturnUrl(returnUrl);
			payBaseInfoVo.setCloseUrl(closeUrl);
			
			
			// 사용자 정보 수정
//			int resultCount = this.tokenMapper.insertToken(tokenVo);
//			if (resultCount == 0) {
//				commonVo.setResultCd("FAIL");
//				commonVo.setResultMsg("Insert token failed.");
//			}
		} catch (Exception e) {
			// 결과코드 : 실패
			payBaseInfoVo.setResultCd("FAIL");
			payBaseInfoVo.setResultMsg(e.toString());
		}
		return payBaseInfoVo;
	}
	
	/**
	 * 빌링 결제 기본 정보 조회 - 데스크탑 (테스트용)
	 * 
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/payment/selectPayBaseInfoBill")
	public PayBaseInfoVo selectPayBaseInfoINIBillTst(@RequestBody PayBaseInfoVo payBaseInfoVo) throws Exception {
		// 결과코드 : 성공
//		CommonVo commonVo = new CommonVo();
//		commonVo.setResultCd("SUCCESS");
//		PayBaseInfoVo returnVo = new PayBaseInfoVo();
		payBaseInfoVo.setResultCd("SUCCESS");
		try {
//			String goodname = payBaseInfoVo.getGoodname();
//			String buyername = payBaseInfoVo.getBuyername();
//			String buyertel = payBaseInfoVo.getBuyertel();
//			String buyeremail = payBaseInfoVo.getBuyeremail();
			
			// 가격 정보 조회
			// String price = Constant.PAYMENT_CREATE_PRICE;
			
		    // USD는 1달러 -> 100으로 표기
		    String priceStr = this.itemMapper.selectPrice(payBaseInfoVo.getSeq());
		    double priceDouble = Double.parseDouble(priceStr) * 100;
		    int priceInt = (int) priceDouble;
		    String price = Integer.toString(priceInt);
			
			String mid = Constant.PAYMENT_MID_BILL;
			// String gopaymethod = payBaseInfoVo.getGopaymethod();
			// mKey=mid(상점아이디)와 매칭되는 signkey 를 SHA256으로 hash한 값 입니다.
			String mKey = SignatureUtil.hash(Constant.PAYMENT_SIGN_KEY_BILL, "SHA-256");
			String oid = mid + "_"+ SignatureUtil.getTimestamp();
			String timestamp = SignatureUtil.getTimestamp();
			String version = Constant.PAYMENT_VERSION;
			String currency = Constant.PAYMENT_CURRENCY;
			String acceptmethod = Constant.ACCEPT_METHOD;
			String returnUrl = Constant.PAYMENT_RETURN_URL;
			// String closeUrl = Constant.PAYMENT_CLOSE_URL;
			String closeUrl = "";
			
			Map<String, String> signParam = new HashMap<String, String>();
			signParam.put("oid", oid);
			signParam.put("price", price);
			signParam.put("timestamp", timestamp);
			// signature 데이터 생성 (모듈에서 자동으로 signParam을 알파벳 순으로 정렬후 NVP 방식으로 나열해 hash)
			String signature = SignatureUtil.makeSignature(signParam);
			
			Map<String, String> verificationParam = new HashMap<String, String>();
			verificationParam.put("oid", oid);
			verificationParam.put("price", price);
			verificationParam.put("signKey", Constant.PAYMENT_SIGN_KEY_BILL);
			verificationParam.put("timestamp", timestamp);
			// signature 데이터 생성 (모듈에서 자동으로 signParam을 알파벳 순으로 정렬후 NVP 방식으로 나열해 hash)
			String verification = SignatureUtil.makeSignature(verificationParam);
			
			payBaseInfoVo.setPrice(price);
			payBaseInfoVo.setMid(mid);
			
			// mKey가 front에서 mkey로 소문자로 오는 현상 때문에 mKeyNew 항목 추가
			// payBaseInfoVo.setMKey(mKey);
			payBaseInfoVo.setMkeyNew(mKey);
			
			payBaseInfoVo.setOid(oid);
			payBaseInfoVo.setTimestamp(timestamp);
			payBaseInfoVo.setSignature(signature);
			payBaseInfoVo.setVerification(verification);
			payBaseInfoVo.setVersion(version);
			payBaseInfoVo.setCurrency(currency);
			payBaseInfoVo.setAcceptmethod(acceptmethod);
			payBaseInfoVo.setReturnUrl(returnUrl);
			payBaseInfoVo.setCloseUrl(closeUrl);
			
			
			// 사용자 정보 수정
//			int resultCount = this.tokenMapper.insertToken(tokenVo);
//			if (resultCount == 0) {
//				commonVo.setResultCd("FAIL");
//				commonVo.setResultMsg("Insert token failed.");
//			}
		} catch (Exception e) {
			// 결과코드 : 실패
			payBaseInfoVo.setResultCd("FAIL");
			payBaseInfoVo.setResultMsg(e.toString());
		}
		return payBaseInfoVo;
	}
}
