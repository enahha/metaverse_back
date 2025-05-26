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
public class AlarmDeleteController {
	
	// private static final Logger logger = LoggerFactory.getLogger(AlarmDeleteController.class);
	
	@Autowired
	private AlarmMapper alarmMapper;
	
	/**
	 * 알람 삭제
	 * 
	 * @param alarmVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/alarm/deleteAlarm")
	public CommonVo deleteAlarm(@RequestBody AlarmVo alarmVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			alarmVo.setDel_id(alarmVo.getUid());
			alarmVo.setDel_yn("Y");
			int resultCount = this.alarmMapper.deleteAlarm(alarmVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("deleteAlarm failed.");
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
