package com.instarverse.api.v1.common.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.common.mapper.KeyValueMapper;
import com.instarverse.api.v1.common.vo.CommonResultVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class KeyValueController {
	
//	private static final Logger logger = LoggerFactory.getLogger(LoginLogController.class);
	
	@Autowired
	private KeyValueMapper keyValueMapper;
	
	/**
	 * 키밸류 코드 값 조회
	 * 
	 * @param uid
	 * @param cdKey
	 * 
	 * @return commonResultVo
	 * @throws Exception
	 */
	@GetMapping("/common/selectKeyValue")
	public CommonResultVo selectKeyValue(@RequestParam String uid, @RequestParam String cdKey) throws Exception {
		CommonResultVo commonResultVo = new CommonResultVo();
		commonResultVo.setResultCd("SUCCESS");
		commonResultVo.setReturnCd("0");
		
		commonResultVo.setReturnValue(this.keyValueMapper.selectKeyValue(cdKey));
		
		return commonResultVo;
	}
}
