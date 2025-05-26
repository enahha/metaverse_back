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
import com.instarverse.api.v1.common.Constant;
import com.instarverse.api.v1.common.rest.PayLogController;
import com.instarverse.api.v1.common.util.StringUtil;
import com.instarverse.api.v1.payment.vo.PayLogVo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Transactional
@RequestMapping(value = "/api")
@Controller
public class PaymentCallbackMobileController implements ErrorController {
	
	// private static final Logger logger = LoggerFactory.getLogger(PaymentCallbackMobileController.class);
	
	@Autowired
	private PayLogController payLogController;
	
	
	
	/**
	 * 결제 returnUrl (Mobile)
	 * 
	 * @param  request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "payment/paymentReturnUrlMobile" , method = {RequestMethod.POST, RequestMethod.GET})
	public String paymentReturnUrlMobile(HttpServletRequest request, HttpSession session, Model model) throws Exception {
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
//		System.out.println("paramMap : "+ paramMap.toString());
		
		// 가맹점 관리데이터 수신
		String p_noti = request.getParameter("P_NOTI");
		String[] customData = null;
		if (!StringUtil.isEmpty(p_noti)) {
			customData = p_noti.split("\\|");
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
			if ("00".equals(paramMap.get("P_STATUS"))) {
				
				System.out.println("####인증성공/승인요청####");
				System.out.println("<br/>");
				System.out.println("####인증성공/승인요청####");
				
				//############################################
				// 1.전문 필드 값 설정(***가맹점 개발수정***)
				//############################################
				String mid			= Constant.PAYMENT_MID;			// 상점아이디 (결제요청 시 사용한 P_MID)
				String p_tid		= paramMap.get("P_TID");		// 인증거래번호 (성공시에만 전달)
				String p_req_url	= paramMap.get("P_REQ_URL");	// 승인요청 URL (해당 URL로 HTTPS API Request 승인요청 - POST)
				
				// 결제 승인 요청 로그 등록
				this.insertPayLogReq(mid, p_tid);
				
				//#####################
				// 2.API 요청 전문 생성
				//#####################
				Map<String, String> authMap = new Hashtable<String, String>();
				authMap.put("P_MID"	, mid);		// 필수
				authMap.put("P_TID"	, p_tid);	// 필수
				System.out.println("##승인요청 API 요청##");
				
				HttpUtil httpUtil = new HttpUtil();
				try {
					//#####################
					// 4.API 통신 시작
					//#####################
					String authResultString = "";
					authResultString = httpUtil.processHTTP(authMap, p_req_url);
					
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
					
					if ("00".equals(resultMap.get("P_STATUS"))) {
						/*****************************************************************************
						여기에 가맹점 내부 DB에 결제 결과를 반영하는 관련 프로그램 코드를 구현한다.  
						******************************************************************************/	
						/////////////////////////////////////////////////////
						// 결제 성공 - jsp 파라미터들 model에 세팅
						/////////////////////////////////////////////////////
						model.addAttribute("resultApiCode", "SUCCESS");
						model.addAttribute("resultCode", resultMap.get("P_STATUS"));
						model.addAttribute("resultMsg", resultMap.get("P_RMESG1"));
						model.addAttribute("orderId", resultMap.get("P_OID")); // 주문번호
						/////////////////////////////////////////////////////
						
					} else {
						/////////////////////////////////////////////////////
						// 결제 실패 - jsp 파라미터들 model에 세팅
						/////////////////////////////////////////////////////
						model.addAttribute("redirectUrl", customData[2]); // payFailUrl - 결제실패시 돌아갈 front 화면
						model.addAttribute("resultApiCode", "FAIL");
						model.addAttribute("resultCode", resultMap.get("P_STATUS"));
						model.addAttribute("resultMsg", resultMap.get("P_RMESG1"));
						/////////////////////////////////////////////////////
					}
					
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
					// String netcancelResultString = httpUtil.processHTTP(authMap, netCancel);	// 망취소 요청 API url(고정, 임의 세팅 금지)
					
					// System.out.println("## 망취소 API 결과 ##");
					
					// 취소 결과 확인
					// System.out.println("<p>" + netcancelResultString.replaceAll("<", "&lt;").replaceAll(">", "&gt;")+"</p>");
					
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
				model.addAttribute("resultCode", paramMap.get("P_STATUS"));
				model.addAttribute("resultMsg", paramMap.get("P_RMESG1"));
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
		payLogVo.setMo2_p_status(paramMap.get("P_STATUS"));
		payLogVo.setMo2_p_rmesg1(paramMap.get("P_RMESG1"));
		payLogVo.setMo2_p_tid(paramMap.get("P_TID"));
		payLogVo.setMo2_p_amt(paramMap.get("P_AMT"));
		payLogVo.setMo2_p_req_url(paramMap.get("P_REQ_URL"));
		payLogVo.setMo2_p_noti(paramMap.get("P_NOTI"));
		payLogVo.setReg_id("SYSTEM");
		this.payLogController.insertPayLogAuthRes(payLogVo);
	}
	
	/**
	 * 결제 승인 요청 로그 등록
	 * 
	 * @param mid
	 * @param tid
	 */
	private void insertPayLogReq(String mid, String tid) {
		PayLogVo payLogVo = new PayLogVo();
		payLogVo.setMo3_p_mid(mid);
		payLogVo.setMo3_p_tid(tid);
		payLogVo.setReg_id("SYSTEM");
		this.payLogController.insertPayLogReq(payLogVo);
	}
	
	/**
	 * 결제 승인 결과 로그 등록
	 * @param resultMap
	 */
	private void insertPayLogRes(Map<String, String> resultMap) {
		PayLogVo payLogVo = new PayLogVo();
		payLogVo.setMo4_p_status(resultMap.get("P_STATUS"));
		payLogVo.setMo4_p_rmesg1(resultMap.get("P_RMESG1"));
		payLogVo.setMo4_p_tid(resultMap.get("P_TID"));
		payLogVo.setMo4_p_type(resultMap.get("P_TYPE"));
		payLogVo.setMo4_p_auth_dt(resultMap.get("P_AUTH_DT"));
		payLogVo.setMo4_p_mid(resultMap.get("P_MID"));
		payLogVo.setMo4_p_oid(resultMap.get("P_OID"));
		payLogVo.setMo4_p_amt(resultMap.get("P_AMT"));
		payLogVo.setMo4_p_uname(resultMap.get("P_UNAME"));
		payLogVo.setMo4_p_mname(resultMap.get("P_MNAME"));
		payLogVo.setMo4_p_noti(resultMap.get("P_NOTI"));
		payLogVo.setMo4_p_noteurl(resultMap.get("P_NOTEURL"));
		payLogVo.setMo4_p_next_url(resultMap.get("P_NEXT_URL"));
		payLogVo.setMo4_p_card_issuer_code(resultMap.get("P_CARD_ISSUER_CODE"));
		payLogVo.setMo4_p_card_member_num(resultMap.get("P_CARD_MEMBER_NUM"));
		payLogVo.setMo4_p_card_purchase_code(resultMap.get("P_CARD_PURCHASE_CODE"));
		payLogVo.setMo4_p_card_prtc_code(resultMap.get("P_CARD_PRTC_CODE"));
		payLogVo.setMo4_p_card_interest(resultMap.get("P_CARD_INTEREST"));
		payLogVo.setMo4_card_corpflag(resultMap.get("CARD_CorpFlag"));
		payLogVo.setMo4_p_card_checkflag(resultMap.get("P_CARD_CHECKFLAG"));
		payLogVo.setMo4_p_rmesg2(resultMap.get("P_RMESG2"));
		payLogVo.setMo4_p_fn_cd1(resultMap.get("P_FN_CD1"));
		payLogVo.setMo4_p_fn_nm(resultMap.get("P_FN_NM"));
		payLogVo.setMo4_p_card_num(resultMap.get("P_CARD_NUM"));
		payLogVo.setMo4_p_auth_no(resultMap.get("P_AUTH_NO"));
		payLogVo.setMo4_p_isp_cardcode(resultMap.get("P_ISP_CARDCODE"));
		payLogVo.setMo4_p_couponflag(resultMap.get("P_COUPONFLAG"));
		payLogVo.setMo4_p_coupon_discount(resultMap.get("P_COUPON_DISCOUNT"));
		payLogVo.setMo4_p_card_applprice(resultMap.get("P_CARD_APPLPRICE"));
		payLogVo.setMo4_p_src_code(resultMap.get("P_SRC_CODE"));
		payLogVo.setMo4_p_card_usepoint(resultMap.get("P_CARD_USEPOINT"));
		payLogVo.setMo4_naverpoint_usefreepoint(resultMap.get("NAVERPOINT_UseFreePoint"));
		payLogVo.setMo4_naverpoint_cshrapplyn(resultMap.get("NAVERPOINT_CSHRApplYN"));
		payLogVo.setMo4_naverpoint_cshrapplamt(resultMap.get("NAVERPOINT_CSHRApplAmt"));
		payLogVo.setMo4_card_empprtncode(resultMap.get("CARD_EmpPrtnCode"));
		payLogVo.setMo4_card_nomlmobprtncode(resultMap.get("CARD_NomlMobPrtnCode"));
		payLogVo.setMo4_bank_p_fn_cd1(resultMap.get("BANK_P_FN_CD1"));
		payLogVo.setMo4_bank_p_fn_nm(resultMap.get("BANK_P_FN_NM"));
		payLogVo.setMo4_p_vact_num(resultMap.get("P_VACT_NUM"));
		payLogVo.setMo4_p_vact_bank_code(resultMap.get("P_VACT_BANK_CODE"));
		payLogVo.setMo4_vact_p_fn_nm(resultMap.get("VACT_P_FN_NM"));
		payLogVo.setMo4_p_vact_date(resultMap.get("P_VACT_DATE"));
		payLogVo.setMo4_p_vact_time(resultMap.get("P_VACT_TIME"));
		payLogVo.setMo4_p_vact_name(resultMap.get("P_VACT_NAME"));
		payLogVo.setMo4_p_hpp_corp(resultMap.get("P_HPP_CORP"));
		payLogVo.setMo4_p_hpp_num(resultMap.get("P_HPP_NUM"));
		payLogVo.setMo4_p_cshr_code(resultMap.get("P_CSHR_CODE"));
		payLogVo.setMo4_p_cshr_msg(resultMap.get("P_CSHR_MSG"));
		payLogVo.setMo4_p_cshr_amt(resultMap.get("P_CSHR_AMT"));
		payLogVo.setMo4_p_cshr_sup_amt(resultMap.get("P_CSHR_SUP_AMT"));
		payLogVo.setMo4_p_cshr_tax(resultMap.get("P_CSHR_TAX"));
		payLogVo.setMo4_p_cshr_srvc_amt(resultMap.get("P_CSHR_SRVC_AMT"));
		payLogVo.setMo4_p_cshr_type(resultMap.get("P_CSHR_TYPE"));
		payLogVo.setMo4_p_cshr_dt(resultMap.get("P_CSHR_DT"));
		payLogVo.setMo4_p_cshr_auth_no(resultMap.get("P_CSHR_AUTH_NO"));
		payLogVo.setMo4_p_kpay_appl_price(resultMap.get("P_KPAY_APPL_PRICE"));
		payLogVo.setMo4_p_kpay_paymethod(resultMap.get("P_KPAY_PAYMETHOD"));
		payLogVo.setMo4_p_kpay_quota(resultMap.get("P_KPAY_QUOTA"));
		payLogVo.setMo4_p_kpay_inst(resultMap.get("P_KPAY_INST"));
		payLogVo.setMo4_p_kpay_check_flg(resultMap.get("P_KPAY_CHECK_FLG"));
		payLogVo.setReg_id("SYSTEM");
		this.payLogController.insertPayLogRes(payLogVo);
	}


}
