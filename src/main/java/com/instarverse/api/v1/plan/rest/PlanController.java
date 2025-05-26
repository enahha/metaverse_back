package com.instarverse.api.v1.plan.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.common.util.CommonUtil;
import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.plan.mapper.PlanMapper;
import com.instarverse.api.v1.plan.vo.PlanVo;

//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class PlanController {
	
	// private static final Logger logger = LoggerFactory.getLogger(PlanController.class);
	
	@Autowired
	private PlanMapper planMapper;
	
	/**
	 * 플랜 리스트 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/plan/selectPlanList")
	public List<PlanVo> selectPlanList(@RequestParam String uid) throws Exception {
		return this.planMapper.selectPlanList();
	}
	
	/**
	 * 플랜 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/plan/selectPlan")
	public PlanVo selectPlan(@RequestParam String uid, @RequestParam int seq) throws Exception {
		PlanVo planVo = new PlanVo();
//		planVo.setUid(uid);
		planVo.setSeq(seq);
		return this.planMapper.selectPlan(planVo);
	}
	
	/**
	 * 플랜 등록
	 * 
	 * @param planVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/plan/insertPlan")
	public CommonVo insertPlan(@RequestBody PlanVo planVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			planVo.setDisplay_yn("Y");
			planVo.setReg_id(planVo.getUid());
			int resultCount = this.planMapper.insertPlan(planVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("insertPlan failed.");
				return commonVo;
			}
		} catch (Exception e) {
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
	
	/**
	 * 플랜 수정
	 * 
	 * @param planVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/plan/updatePlan")
	public CommonVo updatePlan(@RequestBody PlanVo planVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			planVo.setMod_id(planVo.getUid());
			int resultCount = this.planMapper.updatePlan(planVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updatePlan failed.");
				return commonVo;
			}
		} catch (Exception e) {
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
	
	/**
	 * 플랜 삭제
	 * 
	 * @param planVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/plan/deletePlan")
	public CommonVo deletePlan(@RequestBody PlanVo planVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			planVo.setDel_id(planVo.getUid());
			int resultCount = this.planMapper.deletePlan(planVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("deletePlan failed.");
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
