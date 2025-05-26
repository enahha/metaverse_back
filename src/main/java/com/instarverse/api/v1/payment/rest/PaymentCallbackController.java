package com.instarverse.api.v1.payment.rest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.inicis.std.util.HttpUtil;
import com.inicis.std.util.ParseUtil;
import com.inicis.std.util.SignatureUtil;
import com.instarverse.api.v1.common.Constant;
import com.instarverse.api.v1.common.rest.PayLogController;
import com.instarverse.api.v1.common.util.StringUtil;
import com.instarverse.api.v1.payment.vo.PayLogVo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Transactional
@RequestMapping(value = "/api")
@Controller
public class PaymentCallbackController implements ErrorController {
	
	// private static final Logger logger = LoggerFactory.getLogger(PaymentCallbackController.class);
	
	@Autowired
	private PayLogController payLogController;
	
	/**
	 * 결제 returnUrl (PC)
	 * 
	 * @param  request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "payment/paymentReturnUrl" , method = {RequestMethod.POST, RequestMethod.GET})
	public String paymentReturnUrl(HttpServletRequest request, HttpSession session, Model model) throws Exception {
		request.setCharacterEncoding("UTF-8");
//		String authToken = request.getParameter("authToken");
//		logger.debug("authToken: " + authToken);
//		
//		request.setCharacterEncoding("UTF-8");
//		Map<String,String> paramMap = new Hashtable<String,String>();
//		Enumeration elems = request.getParameterNames();
//		String temp = "";
//		while(elems.hasMoreElements())
//		{
//		temp = (String) elems.nextElement();
//		paramMap.put(temp, request.getParameter(temp));
//		}
		
		// 가맹점 관리데이터 수신
		String merchantData = request.getParameter("merchantData");
		String[] customData = null;
		if (!StringUtil.isEmpty(merchantData)) {
			customData = merchantData.split("\\|");
			if (customData != null) {
				model.addAttribute("payCode", customData[0]); // 결제구분 - CREATE_TOKEN, ...
				model.addAttribute("redirectUrl", customData[1]); // paySuccessUrl, 에러시 catch에서 payFailUrl로 설정
				if (customData.length > 3) {
					model.addAttribute("key", customData[3]); // key ex) token seq
				}
			}
		}
	
		try {
			//#############################
			// 인증결과 파라미터 일괄 수신
			//#############################
			Map<String,String> paramMap = new Hashtable<String,String>();
			Enumeration<String> elems = request.getParameterNames();
			String temp = "";
			while (elems.hasMoreElements()) {
				temp = (String) elems.nextElement();
				paramMap.put(temp, request.getParameter(temp));
			}
			System.out.println("paramMap : " + paramMap.toString());
			
			// 결제 인증 결과 로그 등록
			this.insertPayLogAuthRes(paramMap);
			
			//#####################
			// 인증이 성공일 경우만
			//#####################
			if ("0000".equals(paramMap.get("resultCode"))) {
				
				System.out.println("####인증성공/승인요청####");
				System.out.println("<br/>");
				System.out.println("####인증성공/승인요청####");
				
				//############################################
				// 1.전문 필드 값 설정(***가맹점 개발수정***)
				//############################################
				String mid			= paramMap.get("mid");					// 가맹점 ID 수신 받은 데이터로 설정
				// String signKey		= "SU5JTElURV9UUklQTEVERVNfS0VZU1RS";	// 가맹점에 제공된 키(이니라이트키) (가맹점 수정후 고정) !!!절대!! 전문 데이터로 설정금지
				String timestamp	= SignatureUtil.getTimestamp();			// util에 의해서 자동생성
				String charset		= "UTF-8";								// 리턴형식[UTF-8,EUC-KR](가맹점 수정후 고정)
				String format		= "JSON";								// 리턴형식[XML,JSON,NVP](가맹점 수정후 고정)
				String authToken	= paramMap.get("authToken");			// 취소 요청 tid에 따라서 유동적(가맹점 수정후 고정)
				String authUrl		= paramMap.get("authUrl");				// 승인요청 API url(수신 받은 값으로 설정, 임의 세팅 금지)
				String netCancel	= paramMap.get("netCancelUrl");			// 망취소 API url(수신 받은 값으로 설정, 임의 세팅 금지)
				// String ackUrl		= paramMap.get("checkAckUrl");			// 가맹점 내부 로직 처리후 최종 확인 API URL(수신 받은 값으로 설정, 임의 세팅 금지)
				// String merchantData	= paramMap.get("merchantData");			// 가맹점 관리데이터 수신
				
				//#####################
				// 2.signature 생성
				//#####################
				Map<String, String> signParam = new HashMap<String, String>();
				signParam.put("authToken",	authToken);		// 필수
				signParam.put("timestamp",	timestamp);		// 필수
				
				// signature 데이터 생성 (모듈에서 자동으로 signParam을 알파벳 순으로 정렬후 NVP 방식으로 나열해 hash)
				String signature = SignatureUtil.makeSignature(signParam);
				
				// verification 생성
				Map<String, String> verificationParam = new HashMap<String, String>();
				verificationParam.put("authToken",	authToken);		// 필수
				verificationParam.put("signKey",	Constant.PAYMENT_SIGN_KEY);		// 필수
				verificationParam.put("timestamp",	timestamp);		// 필수
				
				// signature 데이터 생성 (모듈에서 자동으로 signParam을 알파벳 순으로 정렬후 NVP 방식으로 나열해 hash)
				String verification = SignatureUtil.makeSignature(verificationParam);
				
				// String price = ""; // 가맹점에서 최종 결제 가격 표기 (필수입력아님)
				
				// 1. 가맹점에서 승인시 주문번호가 변경될 경우 (선택입력) 하위 연결.  
				// String oid = "";
				
				// 결제 승인 요청 로그 등록
				this.insertPayLogReq(mid, timestamp, charset, format, verification, signature);
				
				//#####################
				// 3.API 요청 전문 생성
				//#####################
				Map<String, String> authMap = new Hashtable<String, String>();
				authMap.put("mid"				, mid);			// 필수
				authMap.put("authToken"			, authToken);	// 필수
				authMap.put("signature"			, signature);	// 필수
				authMap.put("verification"		, verification);	// 필수
				authMap.put("timestamp"			, timestamp);	// 필수
				authMap.put("charset"			, charset);		// default=UTF-8
				authMap.put("format"			, format);		// default=XML
				//authMap.put("price" 			, price);		// 가격위변조체크기능 (선택사용)
				System.out.println("##승인요청 API 요청##");
				
				HttpUtil httpUtil = new HttpUtil();
				try {
					//#####################
					// 4.API 통신 시작
					//#####################
					String authResultString = "";
					authResultString = httpUtil.processHTTP(authMap, authUrl);
					
					//############################################################
					//5.API 통신결과 처리(***가맹점 개발수정***)
					//############################################################
					System.out.println("## 승인 API 결과 ##");
					
					String test = authResultString.replace(",", "&").replace(":", "=").replace("\"", "").replace(" ","").replace("\n", "").replace("}", "").replace("{", "");
					//out.println("<pre>"+authResultString.replaceAll("<", "&lt;").replaceAll(">", "&gt;")+"</pre>");
					
					Map<String, String> resultMap = new HashMap<String, String>();
					resultMap = ParseUtil.parseStringToMap(test); // 문자열을 MAP형식으로 파싱
					
					System.out.println("resultMap == " + resultMap);
//					System.out.println("<pre>");
//					System.out.println("<table width='565' border='0' cellspacing='0' cellpadding='0'>");
					
					// 결제 승인 결과 로그 등록
					this.insertPayLogRes(resultMap);
					
					
					/*************************  결제보안 강화 2016-05-18 START ****************************/ 
					Map<String , String> secureMap = new HashMap<String, String>();
					secureMap.put("mid"			, mid);							// mid
					secureMap.put("tstamp"		, timestamp);					// timestemp
					secureMap.put("MOID"		, resultMap.get("MOID"));		// MOID
					secureMap.put("TotPrice"	, resultMap.get("TotPrice"));	// TotPrice
					
					// signature 데이터 생성 
					String secureSignature = SignatureUtil.makeSignatureAuth(secureMap);
					/*************************  결제보안 강화 2016-05-18 END ****************************/
					
					if ("0000".equals(resultMap.get("resultCode")) && secureSignature.equals(resultMap.get("authSignature")) ){ //결제보안 강화 2016-05-18
						/*****************************************************************************
						여기에 가맹점 내부 DB에 결제 결과를 반영하는 관련 프로그램 코드를 구현한다.  
						
						[중요!]
						승인내용에 이상이 없음을 확인한 뒤 가맹점 DB에 해당건이 정상처리 되었음을 반영함.
						처리중 에러 발생시 망취소를 한다.
						******************************************************************************/	
						
//						System.out.println("<tr><th class='td01'><p>거래 성공 여부</p></th>");
//						System.out.println("<td class='td02'><p>성공</p></td></tr>");
//						
//						//결과정보
//						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
//						System.out.println("<tr><th class='td01'><p>결과 코드</p></th>");
//						System.out.println("<td class='td02'><p>" + resultMap.get("resultCode") + "</p></td></tr>");
//						System.out.println("<tr><th class='td01'><p>결과 내용</p></th>");
//						System.out.println("<td class='td02'><p>" + resultMap.get("resultMsg") + "</p></td></tr>");
						
						/////////////////////////////////////////////////////
						// 결제 성공 - jsp 파라미터들 model에 세팅
						/////////////////////////////////////////////////////
						model.addAttribute("resultApiCode", "SUCCESS");
						model.addAttribute("resultCode", resultMap.get("resultCode"));
						model.addAttribute("resultMsg", resultMap.get("resultMsg"));
						model.addAttribute("orderId", resultMap.get("MOID"));
						/////////////////////////////////////////////////////
						// 결제정보 등록
						
						
					} else {
						/////////////////////////////////////////////////////
						// 결제 실패 - jsp 파라미터들 model에 세팅
						/////////////////////////////////////////////////////
						model.addAttribute("redirectUrl", customData[2]); // payFailUrl - 결제실패시 돌아갈 front 화면
						model.addAttribute("resultApiCode", "FAIL");
						model.addAttribute("resultCode", resultMap.get("resultCode"));
						model.addAttribute("resultMsg", resultMap.get("resultMsg"));
						/////////////////////////////////////////////////////
						// 실패 결제정보 등록
						
						
						System.out.println("<tr><th class='td01'><p>거래 성공 여부</p></th>");
						System.out.println("<td class='td02'><p>실패</p></td></tr>");
						System.out.println("<tr><th class='td01'><p>결과 코드</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("resultCode")+"</p></td></tr>");
						System.out.println("<tr><th class='td01'><p>결과 내용</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("resultMsg")+"</p></td></tr>");
						
						//결제보안키가 다른 경우
						if (!secureSignature.equals(resultMap.get("authSignature")) && "0000".equals(resultMap.get("resultCode"))) {
							//결과정보
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>결과 내용</p></th>");
							System.out.println("<td class='td02'><p>* 데이터 위변조 체크 실패</p></td></tr>");	
							
							//망취소
							if ("0000".equals(resultMap.get("resultCode"))) {
								throw new Exception("데이터 위변조 체크 실패");
							}
						}
					}
						
					//공통 부분만
					System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
					System.out.println("<tr><th class='td01'><p>거래 번호</p></th>");
					System.out.println("<td class='td02'><p>" +resultMap.get("tid")+"</p></td></tr>");
					System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
					System.out.println("<tr><th class='td01'><p>결제방법(지불수단)</p></th>");
					System.out.println("<td class='td02'><p>" +resultMap.get("payMethod")+"</p></td></tr>");
					System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
					System.out.println("<tr><th class='td01'><p>결제완료금액</p></th>");
					System.out.println("<td class='td02'><p>" +resultMap.get("TotPrice")+"원</p></td></tr>");
					System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
					System.out.println("<tr><th class='td01'><p>주문 번호</p></th>");
					System.out.println("<td class='td02'><p>" +resultMap.get("MOID")+"</p></td></tr>");
					System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
					System.out.println("<tr><th class='td01'><p>승인날짜</p></th>");
					System.out.println("<td class='td02'><p>" +resultMap.get("applDate")+"</p></td></tr>");
					System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
					System.out.println("<tr><th class='td01'><p>승인시간</p></th>");
					System.out.println("<td class='td02'><p>" +resultMap.get("applTime")+"</p></td></tr>");
					System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
					
					if("VBank".equals(resultMap.get("payMethod"))){ //가상계좌
						
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>입금 계좌번호</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("VACT_Num")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>입금 은행코드</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("VACT_BankCode")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>입금 은행명</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("vactBankName")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>예금주 명</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("VACT_Name")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>송금자 명</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("VACT_InputName")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>송금 일자</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("VACT_Date")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>송금 시간</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("VACT_Time")+"</p></td></tr>");
						
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");	
						
					} else if("DirectBank".equals(resultMap.get("payMethod"))){ // 실시간계좌이체
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>은행코드</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("ACCT_BankCode")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>현금영수증 발급결과코드</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("CSHR_ResultCode")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>현금영수증 발급구분코드</p> <font color=red><b>(0 - 소득공제용, 1 - 지출증빙용)</b></font></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("CSHR_Type")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						 
					} else if("iDirectBank".equals(resultMap.get("payMethod"))){ // 실시간계좌이체
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>은행코드</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("ACCT_BankCode")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>현금영수증 발급결과코드</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("CSHRResultCode")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>현금영수증 발급구분코드</p> <font color=red><b>(0 - 소득공제용, 1 - 지출증빙용)</b></font></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("CSHR_Type")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						 
					} else if("HPP".equals(resultMap.get("payMethod"))){ // 휴대폰
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>통신사</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("HPP_Corp")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>결제장치</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("payDevice")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>휴대폰번호</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("HPP_Num")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						
					} else if("DGCL".equals(resultMap.get("payMethod"))){ // 게임문화상품권
						// String sum="0",sum2="0",sum3="0",sum4="0",sum5="0",sum6="0";
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>게임문화상품권승인금액</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("GAMG_ApplPrice")+"원</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>사용한 카드수</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("GAMG_Cnt")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>사용한 카드번호</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("GAMG_Num1")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>카드잔액</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("GAMG_Price1")+"원</p></td></tr>");
						
						if(!"".equals(resultMap.get("GAMG_Num2"))){
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>사용한 카드번호</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("GAMG_Num2")+"</p></td></tr>");
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>카드잔액</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("GAMG_Price2")+"원</p></td></tr>");
						}
						if(!"".equals(resultMap.get("GAMG_Num3"))){
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>사용한 카드번호</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("GAMG_Num3")+"</p></td></tr>");
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>카드잔액</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("GAMG_Price3")+"원</p></td></tr>");
						}
						if(!"".equals(resultMap.get("GAMG_Num4"))){
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>사용한 카드번호</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("GAMG_Num4")+"</p></td></tr>");
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>카드잔액</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("GAMG_Price4")+"원</p></td></tr>");
						}
						if(!"".equals(resultMap.get("GAMG_Num5"))){
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>사용한 카드번호</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("GAMG_Num5")+"</p></td></tr>");
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>카드잔액</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("GAMG_Price5")+"원</p></td></tr>");
						}
						if(!"".equals(resultMap.get("GAMG_Num6"))){
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>사용한 카드번호</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("GAMG_Num6")+"</p></td></tr>");
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>카드잔액</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("GAMG_Price6")+"원</p></td></tr>");
						}
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						
					} else if("OCBPoint".equals(resultMap.get("payMethod"))){ // 오케이 캐쉬백
					
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>지불구분</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("PayOption")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
//	 					System.out.println("<tr><th class='td01'><p>결제완료금액</p></th>");
//	 					System.out.println("<td class='td02'><p>" +resultMap.get("applPrice")+"원</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>OCB 카드번호</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("OCB_Num")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>적립 승인번호</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("OCB_SaveApplNum")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>사용 승인번호</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("OCB_PayApplNum")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>OCB 지불 금액</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("OCB_PayPrice")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						
					} else if("GSPT".equals(resultMap.get("payMethod"))){ // GSPoint
					
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>지불구분</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("PayOption")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>GS 포인트 승인금액</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("GSPT_ApplPrice")+"원</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>GS 포인트 적립금액</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("GSPT_SavePrice")+"원</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>GS 포인트 지불금액</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("GSPT_PayPrice")+"원</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						
					} else if("UPNT".equals(resultMap.get("payMethod"))){ // U-포인트
						
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>U포인트 카드번호</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("UPoint_Num")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>가용포인트</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("UPoint_usablePoint")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");			
						System.out.println("<tr><th class='td01'><p>포인트지불금액</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("UPoint_ApplPrice")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						
					} else if("KWPY".equals(resultMap.get("payMethod"))){ // 뱅크월렛 카카오
						System.out.println("<tr><th class='td01'><p>결제방법</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("payMethod")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>결과 코드</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("resultCode")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>결과 내용</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("resultMsg")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>거래 번호</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("tid")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>주문 번호</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("MOID")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>결제완료금액</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("price")+"원</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>사용일자</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("applDate")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>사용시간</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("applTime")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						
					} else if("Culture".equals(resultMap.get("payMethod"))){ // 문화 상품권
						System.out.println("<tr><th class='td01'><p>컬처랜드 아이디</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("CULT_UserID")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						
					} else if("TEEN".equals(resultMap.get("payMethod"))){ // 틴캐시
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>틴캐시 승인번호</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("TEEN_ApplNum")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>틴캐시아이디</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("TEEN_UserID")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>틴캐시승인금액</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("TEEN_ApplPrice")+"원</p></td></tr>");	
											
					} else if("Bookcash".equals(resultMap.get("payMethod"))){ // 도서문화상품권
						
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>도서상품권 승인번호</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("BCSH_ApplNum")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>도서상품권 사용자ID</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("BCSH_UserID")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>도서상품권 승인금액</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("BCSH_ApplPrice")+"원</p></td></tr>");
						
					} else if("PhoneBill".equals(resultMap.get("payMethod"))){ // 폰빌전화결제
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>승인전화번호</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("PHNB_Num")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						
						
					} else if("Bill".equals(resultMap.get("payMethod"))){ // 빌링결제
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>빌링키</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("CARD_BillKey")+"</p></td></tr>");
					} else if("Auth".equals(resultMap.get("payMethod"))){ // 빌링결제
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>빌링키</p></th>");
						if ("BILL_CARD".equalsIgnoreCase(resultMap.get("payMethodDetail"))) {
							System.out.println("<td class='td02'><p>" +resultMap.get("CARD_BillKey")+"</p></td></tr>");
						} else if ("BILL_HPP".equalsIgnoreCase(resultMap.get("payMethodDetail"))) {
							System.out.println("<td class='td02'><p>" +resultMap.get("HPP_BillKey")+"</p></td></tr>");
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>통신사</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("HPP_Corp")+"</p></td></tr>");
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>결제장치</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("payDevice")+"</p></td></tr>");
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>휴대폰번호</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("HPP_Num")+"</p></td></tr>");
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>상품명</p></th>"); //상품명
							System.out.println("<td class='td02'><p>" +resultMap.get("goodName")+"</p></td></tr>");
						} else {
							//
						}
					} else if("HPMN".equals(resultMap.get("payMethod"))){ // 해피머니
						
					} else { // 카드
						int quota=Integer.parseInt(resultMap.get("CARD_Quota"));
						if (resultMap.get("EventCode") != null) {
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>이벤트 코드</p></th>");					
							System.out.println("<td class='td02'><p>" +resultMap.get("EventCode")+"</p></td></tr>");
						}
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>카드번호</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("CARD_Num")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>승인번호</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("applNum")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>할부기간</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("CARD_Quota")+"</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						
						if ("1".equals(resultMap.get("CARD_Interest")) || "1".equals(resultMap.get("EventCode"))) {
							System.out.println("<tr><th class='td01'><p>할부 유형</p></th>");
							System.out.println("<td class='td02'><p>무이자</p></td></tr>");	
						} else if(quota > 0 && !"1".equals(resultMap.get("CARD_Interest"))) {
							System.out.println("<tr><th class='td01'><p>할부 유형</p></th>");
							System.out.println("<td class='td02'><p>유이자 <font color='red'> *유이자로 표시되더라도 EventCode 및 EDI에 따라 무이자 처리가 될 수 있습니다.</font></p></td></tr>");
						}
						
						if("1".equals(resultMap.get("point"))) {
							System.out.println("<td class='td02'><p></p></td></tr>");
							System.out.println("<tr><th class='td01'><p>포인트 사용 여부</p></th>");
							System.out.println("<td class='td02'><p>사용</p></td></tr>");
						} else {
							System.out.println("<td class='td02'><p></p></td></tr>");
							System.out.println("<tr><th class='td01'><p>포인트 사용 여부</p></th>");
							System.out.println("<td class='td02'><p>미사용</p></td></tr>");
						}
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>카드 종류</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("CARD_Code")+ "</p></td></tr>");
						System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
						System.out.println("<tr><th class='td01'><p>카드 발급사</p></th>");
						System.out.println("<td class='td02'><p>" +resultMap.get("CARD_BankCode")+ "</p></td></tr>");
						
						if (resultMap.get("OCB_Num")!=null && resultMap.get("OCB_Num") != "") {
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>OK CASHBAG 카드번호</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("OCB_Num")+ "</p></td></tr>");
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>OK CASHBAG 적립 승인번호</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("OCB_SaveApplNum")+ "</p></td></tr>");
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>OK CASHBAG 포인트지불금액</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("OCB_PayPrice")+ "</p></td></tr>");
						}
						if (resultMap.get("GSPT_Num")!=null && resultMap.get("GSPT_Num") != "") {
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>GS&Point 카드번호</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("GSPT_Num")+ "</p></td></tr>");
							
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>GS&Point 잔여한도</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("GSPT_Remains")+ "</p></td></tr>");
							
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>GS&Point 승인금액</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("GSPT_ApplPrice")+ "</p></td></tr>");
						}
						
						if (resultMap.get("UNPT_CardNum")!=null && resultMap.get("UNPT_CardNum") != "") {
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>U-Point 카드번호</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("UNPT_CardNum")+ "</p></td></tr>");
							
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>U-Point 가용포인트</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("UPNT_UsablePoint")+ "</p></td></tr>");
							
							System.out.println("<tr><th class='line' colspan='2'><p></p></th></tr>");
							System.out.println("<tr><th class='td01'><p>U-Point 포인트지불금액</p></th>");
							System.out.println("<td class='td02'><p>" +resultMap.get("UPNT_PayPrice")+ "</p></td></tr>");
						}
					}
					System.out.println("</table>");
					System.out.println("<span style='padding-left : 100px;'>");
					System.out.println("</span>");
					System.out.println("<form name='frm' method='post'>"); 
					System.out.println("<input type='hidden' name='tid' value='"+resultMap.get("tid")+"'/>");
					System.out.println("</form>");
					
					System.out.println("</pre>");
					
					// 수신결과를 파싱후 resultCode가 "0000"이면 승인성공 이외 실패
					// 가맹점에서 스스로 파싱후 내부 DB 처리 후 화면에 결과 표시
					
					// payViewType을 popup으로 해서 결제를 하셨을 경우
					// 내부처리후 스크립트를 이용해 opener의 화면 전환처리를 하세요
					
					//throw new Exception("강제 Exception");
				} catch (Exception ex) {
					
					//####################################
					// 실패시 처리(***가맹점 개발수정***)
					//####################################
					
					// 결제 로그 에러 등록
					this.payLogController.insertPayLogErr("paymentReturnUrl", ex.toString(), authMap);
					
					//---- db 저장 실패시 등 예외처리----//
//					System.out.println(ex);
					ex.printStackTrace();
					
					//#####################
					// 망취소 API
					//#####################
					String netcancelResultString = httpUtil.processHTTP(authMap, netCancel);	// 망취소 요청 API url(고정, 임의 세팅 금지)
					
					System.out.println("## 망취소 API 결과 ##");
					
					// 취소 결과 확인
					System.out.println("<p>" + netcancelResultString.replaceAll("<", "&lt;").replaceAll(">", "&gt;")+"</p>");
					
					/////////////////////////////////////////////////////
					// 결제 실패 망취소 - jsp 파라미터들 model에 세팅
					/////////////////////////////////////////////////////
					model.addAttribute("redirectUrl", customData[2]); // payFailUrl - 결제실패시 돌아갈 front 화면
					model.addAttribute("resultApiCode", "FAIL");
					model.addAttribute("resultCode", "NET_CANCEL");
					model.addAttribute("resultMsg", "Payment canceled.");
					/////////////////////////////////////////////////////
				}
				
			} else {
				//#############
				// 인증 실패시
				//#############
				System.out.println("<br/>");
				System.out.println("####인증실패####");
				System.out.println("<p>" + paramMap.toString() + "</p>");
				
				/////////////////////////////////////////////////////
				// 결제 실패 인증실패 - jsp 파라미터들 model에 세팅
				/////////////////////////////////////////////////////
				model.addAttribute("resultApiCode", "FAIL");
				model.addAttribute("redirectUrl", customData[2]); // payFailUrl - 결제실패시 돌아갈 front 화면
				model.addAttribute("resultCode", paramMap.get("resultCode"));
				model.addAttribute("resultMsg", paramMap.get("resultMsg"));
				/////////////////////////////////////////////////////
			}
		} catch(Exception e) {
//			System.out.println(e);
			e.printStackTrace();
			
			// 결제 로그 에러 등록
			this.payLogController.insertPayLogErr("paymentReturnUrl", e.toString(), request.getParameterMap());
			
			/////////////////////////////////////////////////////
			// 결제 실패 예외처리 - jsp 파라미터들 model에 세팅
			/////////////////////////////////////////////////////
			model.addAttribute("resultApiCode", "FAIL");
			model.addAttribute("redirectUrl", customData[2]); // payFailUrl - 결제실패시 돌아갈 front 화면
			model.addAttribute("resultCode", "EXCEPTION");
			model.addAttribute("resultMsg", e.toString());
			/////////////////////////////////////////////////////
		}
		
		return "payment/paymentCallback";
	}
	
	/**
	 * 결제 인증 결과 로그 등록
	 * @param paramMap
	 */
	private void insertPayLogAuthRes(Map<String, String> paramMap) {
		PayLogVo payLogVo = new PayLogVo();
		payLogVo.setPc2_resultcode(paramMap.get("resultCode"));
		payLogVo.setPc2_resultmsg(paramMap.get("resultMsg"));
		payLogVo.setPc2_mid(paramMap.get("mid"));
		payLogVo.setPc2_ordernumber(paramMap.get("orderNumber"));
		// payLogVo.setPc2_authtoken(paramMap.get("authToken"));
		payLogVo.setPc2_authurl(paramMap.get("authUrl"));
		payLogVo.setPc2_netcancelurl(paramMap.get("netCancelUrl"));
		payLogVo.setPc2_charset(paramMap.get("charset"));
		payLogVo.setPc2_merchantdata(paramMap.get("merchantData"));
		payLogVo.setReg_id("SYSTEM");
		this.payLogController.insertPayLogAuthRes(payLogVo);
	}
	
	/**
	 * 결제 승인 요청 로그 등록
	 * 
	 * @param mid
	 * @param timestamp
	 * @param charset
	 * @param format
	 * @param signature
	 */
	private void insertPayLogReq(String mid, String timestamp, String charset, String format, String verification, String signature) {
		PayLogVo payLogVo = new PayLogVo();
		payLogVo.setPc3_mid(mid);
		// payLogVo.setPc3_authtoken(authToken);
		payLogVo.setPc3_timestamp(timestamp);
		payLogVo.setPc3_signature(signature);
		payLogVo.setPc3_verification(verification);
		payLogVo.setPc3_charset(charset);
		payLogVo.setPc3_format(format);
		// payLogVo.setPc3_price(price);
		payLogVo.setReg_id("SYSTEM");
		this.payLogController.insertPayLogReq(payLogVo);
	}
	
	/**
	 * 결제 승인 결과 로그 등록
	 * @param resultMap
	 */
	private void insertPayLogRes(Map<String, String> resultMap) {
		PayLogVo payLogVo = new PayLogVo();
		payLogVo.setPc4_resultcode(resultMap.get("resultCode"));
		payLogVo.setPc4_resultmsg(resultMap.get("resultMsg"));
		payLogVo.setPc4_tid(resultMap.get("tid"));
		payLogVo.setPc4_goodname(resultMap.get("goodName"));
		payLogVo.setPc4_totprice(resultMap.get("TotPrice"));
		payLogVo.setPc4_moid(resultMap.get("MOID"));
		payLogVo.setPc4_paymethod(resultMap.get("payMethod"));
		payLogVo.setPc4_applnum(resultMap.get("applNum"));
		payLogVo.setPc4_appldate(resultMap.get("applDate"));
		payLogVo.setPc4_appltime(resultMap.get("applTime"));
		payLogVo.setPc4_eventcode(resultMap.get("EventCode"));
		payLogVo.setPc4_buyername(resultMap.get("buyerName"));
		payLogVo.setPc4_buyertel(resultMap.get("buyerTel"));
		payLogVo.setPc4_buyeremail(resultMap.get("buyerEmail"));
		payLogVo.setPc4_custemail(resultMap.get("custEmail"));
		payLogVo.setPc4_card_num(resultMap.get("CARD_Num"));
		payLogVo.setPc4_card_interest(resultMap.get("CARD_Interest"));
		payLogVo.setPc4_card_quota(resultMap.get("CARD_Quota"));
		payLogVo.setPc4_card_code(resultMap.get("CARD_Code"));
		payLogVo.setPc4_card_corpflag(resultMap.get("CARD_CorpFlag"));
		payLogVo.setPc4_card_checkflag(resultMap.get("CARD_CheckFlag"));
		payLogVo.setPc4_card_prtc_code(resultMap.get("CARD_PRTC_CODE"));
		payLogVo.setPc4_card_bankcode(resultMap.get("CARD_BankCode"));
		payLogVo.setPc4_card_srccode(resultMap.get("CARD_SrcCode"));
		payLogVo.setPc4_card_point(resultMap.get("CARD_Point"));
		payLogVo.setPc4_card_couponprice(resultMap.get("CARD_CouponPrice"));
		payLogVo.setPc4_card_coupondiscount(resultMap.get("CARD_CouponDiscount"));
		payLogVo.setPc4_card_usepoint(resultMap.get("CARD_UsePoint"));
		payLogVo.setPc4_naverpoint_usefreepoint(resultMap.get("NAVERPOINT_UseFreePoint"));
		payLogVo.setPc4_naverpoint_cshrapplyn(resultMap.get("NAVERPOINT_CSHRApplYN"));
		payLogVo.setPc4_naverpoint_cshrapplamt(resultMap.get("NAVERPOINT_CSHRApplAmt"));
		payLogVo.setPc4_currency(resultMap.get("currency"));
		payLogVo.setPc4_orgprice(resultMap.get("OrgPrice"));
		payLogVo.setPc4_card_billkey(resultMap.get("CARD_Billkey"));
		payLogVo.setPc4_acct_bankcode(resultMap.get("ACCT_BankCode"));
		payLogVo.setPc4_cshr_resultcode(resultMap.get("CSHR_ResultCode"));
		payLogVo.setPc4_cshr_type(resultMap.get("CSHR_Type"));
		payLogVo.setPc4_acct_name(resultMap.get("ACCT_Name"));
		payLogVo.setPc4_vact_num(resultMap.get("VACT_Num"));
		payLogVo.setPc4_vact_bankcode(resultMap.get("VACT_BankCode"));
		payLogVo.setPc4_vactbankname(resultMap.get("vactBankName"));
		payLogVo.setPc4_vact_name(resultMap.get("VACT_Name"));
		payLogVo.setPc4_vact_inputname(resultMap.get("VACT_InputName"));
		payLogVo.setPc4_vact_date(resultMap.get("VACT_Date"));
		payLogVo.setPc4_vact_time(resultMap.get("VACT_Time"));
		payLogVo.setPc4_hpp_num(resultMap.get("HPP_Num"));
		payLogVo.setPc4_paydevice(resultMap.get("payDevice"));
		payLogVo.setPc4_hpp_billkey(resultMap.get("HPP_Billkey"));
		payLogVo.setPc4_payoption(resultMap.get("PayOption"));
		payLogVo.setPc4_ocb_num(resultMap.get("OCB_Num"));
		payLogVo.setPc4_ocb_payprice(resultMap.get("OCB_PayPrice"));
		payLogVo.setPc4_ocb_saveapplnum(resultMap.get("OCB_SaveApplNum"));
		payLogVo.setPc4_ocb_payapplnum(resultMap.get("OCB_PayApplNum"));
		payLogVo.setPc4_ocb_appldate(resultMap.get("OCB_ApplDate"));
		payLogVo.setPc4_upoint_num(resultMap.get("UPoint_Num"));
		payLogVo.setPc4_upoint_usablepoint(resultMap.get("UPoint_usablePoint"));
		payLogVo.setPc4_upoint_applprice(resultMap.get("UPoint_ApplPrice"));
		payLogVo.setPc4_upnt_payoption(resultMap.get("UPNT_PayOption"));
		payLogVo.setPc4_upnt_saveprice(resultMap.get("UPNT_SavePrice"));
		payLogVo.setPc4_upnt_payprice(resultMap.get("UPNT_PayPrice"));
		payLogVo.setPc4_gspt_applprice(resultMap.get("GSPT_ApplPrice"));
		payLogVo.setPc4_gspt_saveprice(resultMap.get("GSPT_SavePrice"));
		payLogVo.setPc4_gspt_payprice(resultMap.get("GSPT_PayPrice"));
		payLogVo.setPc4_cult_userid(resultMap.get("CULT_UserID"));
		payLogVo.setPc4_gamg_cnt(resultMap.get("GAMG_Cnt"));
		payLogVo.setPc4_gamg_applprice(resultMap.get("GAMG_ApplPrice"));
		payLogVo.setPc4_gamg_num1(resultMap.get("GAMG_Num1"));
		payLogVo.setPc4_gamg_price1(resultMap.get("GAMG_Price1"));
		payLogVo.setPc4_teen_applprice(resultMap.get("TEEN_ApplPrice"));
		payLogVo.setPc4_teen_userid(resultMap.get("TEEN_UserID"));
		payLogVo.setPc4_teen_applnum(resultMap.get("TEEN_ApplNum"));
		payLogVo.setPc4_bcsh_applprice(resultMap.get("BCSH_ApplPrice"));
		payLogVo.setPc4_bcsh_userid(resultMap.get("BCSH_UserID"));
		payLogVo.setPc4_bcsh_applnum(resultMap.get("BCSH_ApplNum"));
		payLogVo.setPc4_phnb_num(resultMap.get("PHNB_Num"));
		payLogVo.setReg_id("SYSTEM");
		this.payLogController.insertPayLogRes(payLogVo);
	}


}
