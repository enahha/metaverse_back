/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.instarverse.api.v1.alarm.mapper;

import java.util.List;

import com.instarverse.api.v1.alarm.vo.AlarmVo;
import com.instarverse.api.v1.common.vo.CommonVo;

public interface AlarmMapper {
	
	// 알람 등록
	public int insertAlarm(AlarmVo alarmVo) throws Exception;
	
	// 알람 삭제
	public int deleteAlarm(AlarmVo alarmVo) throws Exception;
	
	// My 알람 리스트 조회
	public int selectMyAlarmListLastPageNum(CommonVo commonVo) throws Exception;
	public List<AlarmVo> selectMyAlarmList(CommonVo commonVo) throws Exception;
	
	// 알람 리스트 조회
	public int selectAlarmListLastPageNum(CommonVo commonVo) throws Exception;
	public List<AlarmVo> selectAlarmList(CommonVo commonVo) throws Exception;
	
	// 알람 조회(전체 컬럼)
	public AlarmVo selectAlarm(AlarmVo alarmVo) throws Exception;
	
	// 알람 수정 - 읽기 완료
	public int updateAlarmRead(AlarmVo alarmVo) throws Exception;
}
