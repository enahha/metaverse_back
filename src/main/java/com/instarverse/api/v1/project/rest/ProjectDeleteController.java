package com.instarverse.api.v1.project.rest;

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
public class ProjectDeleteController {
	
	// private static final Logger logger = LoggerFactory.getLogger(ProjectDeleteController.class);
	
	@Autowired
	private ProjectMapper projectMapper;
	
	/**
	 * 프로젝트 삭제
	 * 
	 * @param projectVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/project/deleteProject")
	public CommonVo deleteProject(@RequestBody ProjectVo projectVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			projectVo.setDel_id(projectVo.getUid());
			projectVo.setDel_yn("Y");
			projectVo.setStatus_cd(Constant.PROJECT_STATUS_CD_DELETE);
			int resultCount = this.projectMapper.deleteProject(projectVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("deleteProject failed.");
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
