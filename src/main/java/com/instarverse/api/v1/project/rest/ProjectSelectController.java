package com.instarverse.api.v1.project.rest;

import java.math.BigInteger;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instarverse.api.v1.common.Constant;
import com.instarverse.api.v1.common.util.CommonUtil;
import com.instarverse.api.v1.contract.ContractUtil;
import com.instarverse.api.v1.media.mapper.MediaMapper;
import com.instarverse.api.v1.project.mapper.ProjectMapper;
import com.instarverse.api.v1.project.vo.ProjectExhibitionHallVo;
import com.instarverse.api.v1.project.vo.ProjectVo;
import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.ContractMethod;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class ProjectSelectController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectSelectController.class);
	
	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private MediaMapper mediaMapper;
	
	@Value("${klaytn.platform-contract-abi}")
	private String klaytnPlatformContractAbi;
	
	/**
	 * 플랫폼 컨트랙트 잔고 조회
	 * 
	 * @param uid
	 * @param platformContractAddress
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/project/selectPlaformContractBalance")
	public String selectPlaformContractBalance(@RequestParam String uid, @RequestParam String mainnet, @RequestParam String platformContractAddress) throws Exception {
		
		String balance = "0";
		
		if (Constant.MAINNET_KLAYTN.equals(mainnet)) {
			// 컨트랙트 생성
			// 1. 지갑 설정
			Caver caver = new Caver(Constant.KLAYTN_MAINNET_URL);
			SingleKeyring deployer = KeyringFactory.createFromKlaytnWalletKey(Constant.KLAYTN_WALLET_KEY_CREATE_TOKEN);
			caver.wallet.add(deployer);
			
			// 2 SendOption 설정
			SendOptions sendOptions = new SendOptions();
			sendOptions.setFrom(deployer.getAddress());
			sendOptions.setGas(BigInteger.valueOf(ContractUtil.KLAYSTAR_SEND_OPTIONS_GAS));
			
			// 3 컨트랙트 파라미터 설정
			// Object[] params = {};
			Contract contractPlatform = caver.contract.create(this.klaytnPlatformContractAbi, platformContractAddress);
			ContractMethod methodGetBalance = contractPlatform.getMethod(ContractUtil.FUNCTION_NAME_GET_BALANCE);
			
			balance = methodGetBalance.call(null).get(0).getValue().toString();
			logger.debug("balance: " + balance);
			
		} else if (Constant.MAINNET_ETHEREUM.equals(mainnet)) {
			// TODO: 이더리움용 코드 생성
		} else {
			// 지원하지 않는 메인넷.
		}
		
		return balance;
	}
	
	/**
	 * 프로젝트 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/project/selectProject")
	public ProjectVo selectProject(@RequestParam String uid, @RequestParam int seq) throws Exception {
		ProjectVo projectVo = new ProjectVo();
		projectVo.setUid(uid);
		projectVo.setSeq(seq);
		ProjectVo result = this.projectMapper.selectProject(projectVo);
		
	    // 등록ID와 UID가 다르면 조회수 증가
	    if (!result.getReg_id().equals(uid) && uid != null) {
	        this.projectMapper.updateProjectView(seq);
	    }
	    
		return this.projectMapper.selectProject(projectVo);
	}
	
	/**
	 * 프로젝트 리스트 마지막 페이지 번호 조회
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/project/selectProjectListLastPageNum")
	public int selectProjectListLastPageNum(@RequestParam String uid, @RequestParam(required = false) String regId, @RequestParam int pageSize, @RequestParam String keyword, @RequestParam String statusCd) throws Exception {
		ProjectVo projectVo = new ProjectVo();
		projectVo.setKeyword(keyword);
		projectVo.setPageSize(pageSize);
		projectVo.setUid(uid);
		projectVo.setReg_id(regId);
		projectVo.setStatus_cd(statusCd);
		return this.projectMapper.selectProjectListLastPageNum(projectVo);
	}
	
	/**
	 * 프로젝트 리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/project/selectProjectList")
	public List<ProjectVo> selectProjectList(@RequestParam String uid, @RequestParam(required = false) String regId, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String keyword, @RequestParam String statusCd) throws Exception {
		// 페이징 처리
		ProjectVo projectVo = new ProjectVo();
		projectVo.setKeyword(keyword);
		projectVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		projectVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		projectVo.setUid(uid);
		projectVo.setReg_id(regId);
		projectVo.setStatus_cd(statusCd);
		return this.projectMapper.selectProjectList(projectVo);
	}
	
	/**
	 * 프로젝트 리스트 조회(인기 순위 조회수순)
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/project/selectProjectListAsView")
	public List<ProjectVo> selectProjectListAsView(@RequestParam int limit) throws Exception {
		return this.projectMapper.selectProjectListAsView(limit);
	}
	
	/**
	 * 프로젝트 리스트 조회(등록날짜 순)
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/project/selectProjectListAsRegTime")
	public List<ProjectVo> selectProjectListAsRegTime(@RequestParam int limit) throws Exception {
		return this.projectMapper.selectProjectListAsRegTime(limit);
	}
	
	/**
	 * 해당 프로젝트에 관한 모든 정보 조회
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/project/selectProjectAndExhibitionHall")
	public ProjectExhibitionHallVo selectProjectAndExhibition(@RequestParam int seq) throws Exception {
		ProjectVo projectVo = new ProjectVo();
		projectVo.setSeq(seq);
		return this.projectMapper.selectProjectAndExhibitionHall(projectVo);	
	}
}
