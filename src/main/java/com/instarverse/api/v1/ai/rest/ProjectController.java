package com.instarverse.api.v1.ai.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.ai.mapper.AiProjectMapper;
import com.instarverse.api.v1.ai.vo.AiProjectVo;
import com.instarverse.api.v1.common.vo.CommonResultVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class ProjectController {
	
	// private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
	
	@Autowired
	private AiProjectMapper aiProjectMapper;
	
	
	/**
	 * 프로젝트 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/ai/selectProject")
	public CommonResultVo selectProject(@RequestParam String uid, @RequestParam int seq) throws Exception {
		
		
		CommonResultVo commonResultVo = new CommonResultVo();
		commonResultVo.setResultCd("SUCCESS");
		commonResultVo.setReturnCd("0");
		
		AiProjectVo aiProjectVo = new AiProjectVo();
		aiProjectVo.setUid(uid);
		aiProjectVo.setSeq(seq);
		
		commonResultVo.setReturnValue(this.aiProjectMapper.selectProject(aiProjectVo));
		
		return commonResultVo;
	}
}
