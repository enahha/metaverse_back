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
package com.instarverse.api.v1.notice.mapper;

import java.util.List;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.notice.vo.NoticeVo;
 
public interface NoticeMapper {
	
	// 공지사항 리스트 조회
	public int selectNoticeListLastPageNum(CommonVo commonVo) throws Exception;
	public List<NoticeVo> selectNoticeList(CommonVo commonVo) throws Exception;
	
	// 공지사항 조회
	public NoticeVo selectNotice(NoticeVo noticeVo) throws Exception;
	
	// 공지사항 등록
	public int insertNotice(NoticeVo noticeVo) throws Exception;
	
	// 공지사항 수정
	public int updateNotice(NoticeVo noticeVo) throws Exception;
	
	// 공지사항 삭제
	public int deleteNotice(NoticeVo noticeVo) throws Exception;
}
