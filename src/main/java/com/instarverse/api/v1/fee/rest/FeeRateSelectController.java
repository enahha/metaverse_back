package com.instarverse.api.v1.fee.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.fee.mapper.FeeRateMapper;
import com.instarverse.api.v1.fee.vo.FeeRateVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class FeeRateSelectController {
	
	// private static final Logger logger = LoggerFactory.getLogger(FeeRateSelectController.class);
	
	@Autowired
	private FeeRateMapper feeRateMapper;
	
	/**
	 * 수수료율 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/feeRate/selectFeeRate")
	public FeeRateVo selectFeeRate(@RequestParam String uid, @RequestParam String fee_cd) throws Exception {
		FeeRateVo feeRateVo = new FeeRateVo();
		feeRateVo.setFee_cd(fee_cd);
		return this.feeRateMapper.selectFeeRate(feeRateVo);
	}
}
