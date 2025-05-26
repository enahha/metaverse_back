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
public class AlarmInsertController {
	
	// private static final Logger logger = LoggerFactory.getLogger(AlarmInsertController.class);
	
	@Autowired
	private AlarmMapper alarmMapper;
	
	
	/**
	 * 알람 등록
	 * 
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/alarm/insertAlarm")
	public CommonVo insertAlarm(@RequestBody AlarmVo alarmVo) throws Exception {
		// 결과코드 : 성공
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			// 1. 알람 등록
			alarmVo.setReg_id(alarmVo.getUid());
			alarmVo.setDel_yn("N"); // 삭제플래그 N
			
			int resultCount = this.alarmMapper.insertAlarm(alarmVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("insertAlarm failed.");
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
