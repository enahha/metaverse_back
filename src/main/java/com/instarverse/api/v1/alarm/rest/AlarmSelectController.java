package com.instarverse.api.v1.alarm.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.instarverse.api.v1.alarm.mapper.AlarmMapper;
import com.instarverse.api.v1.alarm.vo.AlarmVo;
import com.instarverse.api.v1.common.util.CommonUtil;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class AlarmSelectController {
	
	// private static final Logger logger = LoggerFactory.getLogger(AlarmSelectController.class);
	
	@Autowired
	private AlarmMapper alarmMapper;
	
	/**
	 * 알람 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/alarm/selectAlarm")
	public AlarmVo selectAlarm(@RequestParam String uid, @RequestParam int seq) throws Exception {
		AlarmVo alarmVo = new AlarmVo();
		alarmVo.setUid(uid);
		alarmVo.setSeq(seq);
		return this.alarmMapper.selectAlarm(alarmVo);
	}
	
	/**
	 * My 알람 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/alarm/selectMyAlarmListLastPageNum")
	public int selectMyAlarmListLastPageNum(@RequestParam String uid, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		AlarmVo alarmVo = new AlarmVo();
		alarmVo.setKeyword(keyword);
		alarmVo.setPageSize(pageSize);
		alarmVo.setReg_id(uid);
		return this.alarmMapper.selectMyAlarmListLastPageNum(alarmVo);
	}
	
	/**
	 * My 알람 리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/alarm/selectMyAlarmList")
	public List<AlarmVo> selectMyAlarmList(@RequestParam String uid, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		// 페이징 처리
		AlarmVo alarmVo = new AlarmVo();
		alarmVo.setKeyword(keyword);
		alarmVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		alarmVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		alarmVo.setReg_id(uid);
		return this.alarmMapper.selectMyAlarmList(alarmVo);
	}
	
	/**
	 * 알람 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/alarm/selectAlarmListLastPageNum")
	public int selectAlarmListLastPageNum(@RequestParam String uid, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		AlarmVo alarmVo = new AlarmVo();
		alarmVo.setKeyword(keyword);
		alarmVo.setPageSize(pageSize);
		alarmVo.setUid(uid);
		return this.alarmMapper.selectAlarmListLastPageNum(alarmVo);
	}
	
	/**
	 * 알람 리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/alarm/selectAlarmList")
	public List<AlarmVo> selectAlarmList(@RequestParam String uid, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		// 페이징 처리
		AlarmVo alarmVo = new AlarmVo();
		alarmVo.setKeyword(keyword);
		alarmVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		alarmVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		alarmVo.setUid(uid);
		return this.alarmMapper.selectAlarmList(alarmVo);
	}
}
