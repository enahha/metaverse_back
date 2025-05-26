package com.instarverse.api.v1.project.rest;

import java.util.List;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.common.Constant;
import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.project.mapper.ProjectMapper;
import com.instarverse.api.v1.project.vo.ProjectVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class ProjectInsertController {
	
	// private static final Logger logger = LoggerFactory.getLogger(ProjectInsertController.class);
	
	@Autowired
	private ProjectMapper projectMapper;
	
	
	/**
	 * 프로젝트 등록
	 * 
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/project/insertProject")
	public CommonVo insertProject(@RequestBody ProjectVo projectVo) throws Exception {
		// 결과코드 : 성공
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			// 1. 프로젝트 등록
			projectVo.setReg_id(projectVo.getUid());
			projectVo.setDel_yn("N"); // 삭제플래그 N
			projectVo.setMain_hall("N");
			projectVo.setStatus_cd(Constant.PROJECT_STATUS_CD_REGISTERED);
			
			projectVo.setPlatform_deposit_balance("0");
			projectVo.setPlatform_yield("0");
			projectVo.setPlatform_total_balance("0");
			
			// 태그리스트 변환
			List<String> tagList = projectVo.getTag_list();
			if (tagList != null || !tagList.isEmpty()) {
				String tag = String.join(",", tagList);
				projectVo.setTag(tag);
			}
			
			int resultCount = this.projectMapper.insertProject(projectVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("insertProject failed.");
				return commonVo;
			}
			
            // 삽입된 프로젝트의 ID를 반환
			commonVo.setLast_seq(projectVo.getSeq());
		} catch (Exception e) {
			e.printStackTrace();
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
	
	/**
	 * 리스트 항목 문자열로 변환
	 * 
	 * @return
	 */
	private String convertListToString(List<String> list) {
		if(list == null || list.isEmpty()) {
			return null;
		}

		String an = String.join(",", list);
		for(int i = 0; i < list.size(); i++) {
			
		}
		
		return null;
	}
}
