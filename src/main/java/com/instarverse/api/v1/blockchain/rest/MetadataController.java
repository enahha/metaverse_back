package com.instarverse.api.v1.blockchain.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.instarverse.api.v1.common.mapper.KeyValueMapper;
import com.instarverse.api.v1.common.rest.CommonController;
import com.instarverse.api.v1.common.vo.CommonResultVo;
import com.instarverse.api.v1.media.mapper.MediaMapper;
import com.instarverse.api.v1.project.mapper.ProjectMapper;

@RestController
@RequestMapping(value = "/api")
public class MetadataController {
	
	// private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
	
	@Autowired
	private CommonController commonController;
	
	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private KeyValueMapper keyValueMapper;
	
	@Autowired
	private MediaMapper mediaMapper;
	
	/**
	 * 메타데이터 json 파일 생성할 JSONObject 내용 작성
	 * 
	 * @param projectSeq
	 * @return CommonResultVo
	 * @throws Exception
	 */
	@GetMapping("/createJsonFile")
	public CommonResultVo createJsonFile(
			@RequestParam int projectSeq
			, @RequestParam String  projectTitle
			, @RequestParam String projectSubtitle
			, @RequestParam String imageUrl
			, @RequestParam int lastNftId) throws Exception {
	    CommonResultVo commonResultVo = new CommonResultVo();
	    commonResultVo.setResultCd("SUCCESS");
	    commonResultVo.setReturnCd("0");

	    // JSON 객체 생성
	    Gson gson = new GsonBuilder().create();

	    // Create the root JSON object
	    JsonObject rootObject = new JsonObject();
	    rootObject.addProperty("name", projectTitle + " #" + lastNftId);
	    rootObject.addProperty("description", projectSubtitle);
	    rootObject.addProperty("image", imageUrl); // URL을 그대로 추가

	    // 속성 배열 생성 및 추가
	    JsonArray attributesArray = new JsonArray();

	    // color 속성 추가
	    JsonObject colorAttribute = new JsonObject();
	    colorAttribute.addProperty("trait_type", "color");
	    colorAttribute.addProperty("value", "red");
	    attributesArray.add(colorAttribute);

	    // Level 속성 추가
	    JsonObject levelAttribute = new JsonObject();
	    levelAttribute.addProperty("trait_type", "Level");
	    levelAttribute.addProperty("value", "1");
	    attributesArray.add(levelAttribute);

	    // AGE 속성 추가
	    JsonObject ageAttribute = new JsonObject();
	    ageAttribute.addProperty("trait_type", "AGE");
	    ageAttribute.addProperty("value", "1");
	    attributesArray.add(ageAttribute);

	    // JSON 객체에 attributes 추가
	    rootObject.add("attributes", attributesArray);

	    // JSON 파일 생성 후 URL 설정
	    String jsonString = gson.toJson(rootObject);
	    String fileUrl = this.commonController.saveJsonFile(lastNftId, projectSeq, jsonString);
	    commonResultVo.setReturnValue(fileUrl);

	    return commonResultVo;
	}
}