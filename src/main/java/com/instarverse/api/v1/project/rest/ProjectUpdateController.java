package com.instarverse.api.v1.project.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.project.mapper.ProjectMapper;
import com.instarverse.api.v1.project.vo.ProjectVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class ProjectUpdateController {
	
	// private static final Logger logger = LoggerFactory.getLogger(ProjectUpdateController.class);
	
	@Autowired
	private ProjectMapper projectMapper;
	
//	@Autowired
//	private ProjectDescriptionMapper projectDescriptionMapper;
	
	/**
	 * 프로젝트 수정
	 * 
	 * @param projectVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/project/updateProject")
	public CommonVo updateProject(@RequestBody ProjectVo projectVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			String uid = projectVo.getUid();
//			int projectSeq = projectVo.getSeq();
			
			projectVo.setMod_id(uid);
			projectVo.setDel_yn("N");
			
			// 태그리스트 변환
			List<String> tagList = projectVo.getTag_list();
			if (tagList != null || !tagList.isEmpty()) {
				String tag = String.join(",", tagList);
				projectVo.setTag(tag);
			}
			
			// 1. 프로젝트 테이블 수정
			int resultCount = this.projectMapper.updateProject(projectVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateProject failed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
	
	/**
	 * 프로젝트 수정 - 잔고
	 * 
	 * @param projectVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/project/updateProjectBalance")
	public CommonVo updateProjectBalance(@RequestBody ProjectVo projectVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			String uid = projectVo.getUid();
//			int projectSeq = projectVo.getSeq();
			
			projectVo.setMod_id(uid);
			
			// 1. 프로젝트 잔고 수정
			int resultCount = this.projectMapper.updateProjectBalance(projectVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateProjectBalance failed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
	
	/**
	 * 프로젝트 수정 - 상태
	 * 
	 * @param projectVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/project/updateProjectStatusCd")
	public CommonVo updateProjectStatusCd(@RequestBody ProjectVo projectVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			String uid = projectVo.getUid();
//			int projectSeq = projectVo.getSeq();
			
			projectVo.setMod_id(uid);
			
			// 1. 프로젝트 상태 수정
			int resultCount = this.projectMapper.updateProjectStatusCd(projectVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateProjectBalance failed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
	
	/**
	 * 프로젝트 수정 - 메인 홀
	 * 
	 * @param projectVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/project/updateMainHall")
	public CommonVo updateMainHall(@RequestBody ProjectVo projectVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			String uid = projectVo.getUid();
//			int projectSeq = projectVo.getSeq();
			
			projectVo.setMod_id(uid);
			
			// 1. 프로젝트 메인 홀 수정
			int resultCountPre = this.projectMapper.updateMainHallPre(projectVo);
			if (resultCountPre == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateProjectBalance failed.");
			}
			
			int resultCount = this.projectMapper.updateMainHall(projectVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateProjectBalance failed.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
	
	/**
	 * 프로젝트 수정 - 컬렉션 url
	 * 
	 * @param projectVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/project/updateProjectCollectionUrl")
	public CommonVo updateProjectCollectionUrl(@RequestBody ProjectVo projectVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
//			String uid = projectVo.getUid();
////			int projectSeq = projectVo.getSeq();
//			
//			projectVo.setMod_id(uid);
			
			// 1. 프로젝트 상태 수정
			int resultCount = this.projectMapper.updateProjectCollectionUrl(projectVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateProjectBalance failed.");
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
