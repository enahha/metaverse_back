package com.instarverse.api.v1.alarm.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.alarm.mapper.AlarmMapper;
import com.instarverse.api.v1.alarm.vo.AlarmVo;
import com.instarverse.api.v1.common.vo.CommonVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class AlarmUpdateController {
	
	// private static final Logger logger = LoggerFactory.getLogger(AlarmUpdateController.class);
	
	@Autowired
	private AlarmMapper alarmMapper;
	
//	@Autowired
//	private AlarmDescriptionMapper alarmDescriptionMapper;
	
	/**
	 * 알람 수정
	 * 
	 * @param alarmVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/alarm/updateAlarmRead")
	public CommonVo updateAlarmRead(@RequestBody AlarmVo alarmVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			String uid = alarmVo.getUid();
//			int alarmSeq = alarmVo.getSeq();
			
			alarmVo.setMod_id(uid);
			alarmVo.setRead_yn("Y");
			
			// 1. 알람 테이블 수정
			int resultCount = this.alarmMapper.updateAlarmRead(alarmVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateAlarmRead failed.");
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
