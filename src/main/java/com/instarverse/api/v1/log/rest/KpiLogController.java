package com.instarverse.api.v1.log.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.common.vo.CommonResultVo;
import com.instarverse.api.v1.log.mapper.KpiLogMapper;
import com.instarverse.api.v1.log.vo.KpiLogVo;

//@ApiIgnore
@RestController
//@Transactional
@RequestMapping(value = "/api")
public class KpiLogController {
	
	// private static final Logger logger = LoggerFactory.getLogger(KpiUserController.class);
	
	@Autowired
	private KpiLogMapper kpiLogMapper;
	
	/**
	 * i18n 리스트 등록
	 * 
	 * @param sid
	 * @param i18nVo
	 * @return commonResultVo
	 * @throws Exception
	 */
	@RequestMapping(value = "/log/insertKpiLog" , method = {RequestMethod.POST})
	public CommonResultVo insertKpiLog(@RequestBody KpiLogVo kpiLogVo) throws Exception {
		CommonResultVo commonResultVo = new CommonResultVo();
		commonResultVo.setResultCd("SUCCESS");
		commonResultVo.setReturnCd("0");
		
		try {
//			KpiLogVo kpiLogVo = new KpiLogVo();
//			kpiLogVo.setAction_no(actionNo);
//			kpiLogVo.setAction_cd(actionCd);
//			kpiLogVo.setParams(params);
//			
//			try {
//				actionTime = actionTime.replace("Z", "");
//			} catch (Exception e) {
//				actionTime = null;
//			}
			
//			kpiLogVo.setAction_time(actionTime); // ex) 2024-04-08T03:53:26.301Z
			if (kpiLogVo.getUid() == null || kpiLogVo.getUid().equals("")) {
				kpiLogVo.setReg_id("GUEST");
			}
			kpiLogVo.setReg_id(kpiLogVo.getUid());
			
			System.out.println(kpiLogVo);

			
			int resultCount = this.kpiLogMapper.insertKpiLog(kpiLogVo);
			if (resultCount == 0) {
				commonResultVo.setResultCd("FAIL");
				commonResultVo.setReturnCd("1");
				commonResultVo.setResultMsg("insertKpiLog failed.");
				return commonResultVo;
			}
		} catch (Exception e) {
			// 결과코드 : 실패
			// e.printStackTrace();
			commonResultVo.setResultCd("FAIL");
			commonResultVo.setReturnCd("2");
			commonResultVo.setResultMsg(e.toString());
			return commonResultVo;
		}
		return commonResultVo;
	}
}
