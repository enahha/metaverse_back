package com.instarverse.api.v1.common.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.payment.mapper.PayLogMapper;
import com.instarverse.api.v1.payment.vo.PayLogErrVo;
import com.instarverse.api.v1.payment.vo.PayLogVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class PayLogController {
	
//	private static final Logger logger = LoggerFactory.getLogger(PayLogController.class);
	
	@Autowired
	private PayLogMapper payLogMapper;
	
	/**
	 * 아이디/비밀번호 메일보내기
	 * 
	 * @param  findVo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/payment/insertPayLogAuthReq")
	public CommonVo insertPayLogAuthReq(@RequestBody PayLogVo payLogVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		commonVo.setResultMsg("PayLogAuthReq inserted.");
		
		try {
			int resultCount = this.payLogMapper.insertPayLogAuthReq(payLogVo);
			if (resultCount < 1) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("Fail to insertPayLogAuthReq.");
			}
		} catch (Exception e) {
//			logger.debug(e.toString());
			insertPayLogErr("insertPayLogAuthReq", e.toString(), payLogVo);
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		
		return commonVo;
	}
	
	/**
	 * 결제 인증 결과 로그 등록
	 * @param payLogVo
	 * 
	 * @return resultCount
	 */
	public int insertPayLogAuthRes(PayLogVo payLogVo) {
		int resultCount = 0;
		try {
			resultCount = this.payLogMapper.insertPayLogAuthRes(payLogVo);
		} catch (Exception e) {
			e.printStackTrace();
			insertPayLogErr("insertPayLogAuthRes", e.toString(), payLogVo);
		}
		return resultCount;
	}
	
	/**
	 * 결제 승인 요청 로그 등록
	 * @param payLogVo
	 * 
	 * @return resultCount
	 */
	public int insertPayLogReq(PayLogVo payLogVo) {
		int resultCount = 0;
		try {
			resultCount = this.payLogMapper.insertPayLogReq(payLogVo);
		} catch (Exception e) {
			e.printStackTrace();
			insertPayLogErr("insertPayLogReq", e.toString(), payLogVo);
		}
		return resultCount;
	}
	
	/**
	 * 결제 승인 결과 로그 등록
	 * @param payLogVo
	 * 
	 * @return resultCount
	 */
	public int insertPayLogRes(PayLogVo payLogVo) {
		int resultCount = 0;
		try {
			resultCount = this.payLogMapper.insertPayLogRes(payLogVo);
		} catch (Exception e) {
			e.printStackTrace();
			insertPayLogErr("insertPayLogRes", e.toString(), payLogVo);
		}
		return resultCount;
	}
	
	/**
	 * 결제 로그 에러 등록
	 * @param payLogVo
	 * 
	 * @return resultCount
	 */
	public void insertPayLogErr(String errorCode, String errorMessage, Object paramObj) {
		try {
			PayLogErrVo payLogErrVo = new PayLogErrVo();
			payLogErrVo.setCode(errorCode);
			payLogErrVo.setMessage(errorMessage);
			if (paramObj != null) {
				payLogErrVo.setParams(paramObj.toString());
			}
			payLogErrVo.setReg_id("SYSTEM");
			this.payLogMapper.insertPayLogErr(payLogErrVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
