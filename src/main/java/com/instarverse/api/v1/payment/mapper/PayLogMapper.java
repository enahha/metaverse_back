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
package com.instarverse.api.v1.payment.mapper;

import com.instarverse.api.v1.payment.vo.PayLogErrVo;
import com.instarverse.api.v1.payment.vo.PayLogVo;

public interface PayLogMapper {
	// 결제 인증 요청 로그 등록
	public int insertPayLogAuthReq(PayLogVo payLogVo) throws Exception;
	// 결제 인증 결과 로그 등록
	public int insertPayLogAuthRes(PayLogVo payLogVo) throws Exception;
	// 결제 승인 요청 로그 등록
	public int insertPayLogReq(PayLogVo payLogVo) throws Exception;
	// 결제 승인 결과 로그 등록
	public int insertPayLogRes(PayLogVo payLogVo) throws Exception;
	
	// 결제 로그 에러 등록
	public int insertPayLogErr(PayLogErrVo payLogErrVo) throws Exception;
	
}
