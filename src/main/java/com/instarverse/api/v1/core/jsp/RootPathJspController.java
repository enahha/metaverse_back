package com.instarverse.api.v1.core.jsp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.instarverse.api.v1.common.Constant;
import com.instarverse.api.v1.common.util.StringUtil;
import com.instarverse.api.v1.project.mapper.ProjectMapper;
import com.instarverse.api.v1.project.vo.ProjectVo;
import com.instarverse.api.v1.vote.mapper.VoteMapper;
import com.instarverse.api.v1.vote.vo.VoteVo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Transactional
@RequestMapping(value = "/")
@Controller
public class RootPathJspController implements ErrorController {
	
	// private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private VoteMapper voteMapper;
	
	/**
	 * 작가 닉네임으로 대표 전시관으로 패스
	 * 
	 * @param Request - artist(nickname)
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "/{artist}" , method = {RequestMethod.POST, RequestMethod.GET})
//	public String a(@PathVariable("artist") String nickname, HttpServletRequest request, HttpSession session, Model model) throws Exception {
//		// 작가 대표 프로젝트 조회
//		ProjectVo projectVo = new ProjectVo();
//		projectVo.setNickname(nickname);
//		try {
//			projectVo = this.projectMapper.selectProjectByNickName(projectVo);
//			System.out.println("작가 대표 프로젝트 조회");
//			System.out.println(projectVo);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		// og 정보 설정
//		String ogTitle = "";
//		String ogDescription = "";
//		String ogImage = "";
//		String exhibitionPath = "";
//		
//		if (projectVo == null) {
//			// 대표전시관 설정이 안되었을 때
//			ogTitle = "Gallery X";
//			ogDescription = "Metaverse for Exhibition";
//			ogImage = "http://galleryx.io/images/og_image.png";
//			// 
//			exhibitionPath = Constant.SERVER_DOMAIN + "/#/exhibition";
//		} else {
//			ogTitle = projectVo.getTitle();
//			ogDescription = projectVo.getSubtitle();
//			ogImage = projectVo.getPostar_url();
//			if (StringUtil.isEmpty(ogImage)) {
//				ogImage = "http://galleryx.io/images/og_image.png";
//			}
//			// 전시관 url설정
//			int seq = projectVo.getSeq();
//			
//			exhibitionPath = Constant.SERVER_DOMAIN + "/exhibition/index.html?exhibitionSeq=" + seq;
//		}
//		model.addAttribute("exhibitionPath", exhibitionPath);
//		
//		model.addAttribute("ogUrl", exhibitionPath);
//		model.addAttribute("ogTitle", ogTitle);
//		model.addAttribute("ogDescription", ogDescription);
//		model.addAttribute("ogImage", ogImage);
//		
//		System.out.println("exhibitionPath: " + exhibitionPath);
//		System.out.println("ogUrl: " + exhibitionPath);
//		System.out.println("ogTitle: " + ogTitle);
//		System.out.println("ogDescription: " + ogDescription);
//		System.out.println("ogImage: " + ogImage);
//		
//		return "project/exhibition";
//	}
//	
	
	
	
	
	
	/**
	 * /info 프로젝트 정보 공유 링크 패스
	 * 
	 * @param Request - seq(project seq), tab('information' or 'link' or 'comment'), locale('en' or 'ko')
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "i" , method = {RequestMethod.POST, RequestMethod.GET})
	public String i(HttpServletRequest request, HttpSession session, Model model) throws Exception {
		String seq = request.getParameter("s"); // seq
		int intSeq = 0;
		if (!StringUtil.isEmpty(seq)) {
			intSeq = Integer.parseInt(seq);
		}
		String tab = request.getParameter("t"); // tab
		if (StringUtil.isEmpty(tab)) {
			tab = "i";
		}
		String locale = request.getParameter("l"); // locale en/ko
		if (StringUtil.isEmpty(locale)) {
			locale = "en";
		}
		
		// 프로젝트 정보 조회
		ProjectVo projectVo = new ProjectVo();
		projectVo.setSeq(intSeq);
		try {
			projectVo = this.projectMapper.selectProject(projectVo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// og 정보 설정
		String ogTitle = "";
		String ogDescription = "";
		String ogImage = "";
		
		if (projectVo != null) {
			if ("ko".equals(locale)) {
				ogTitle = projectVo.getTitle_ko();
				ogDescription = projectVo.getSummary_ko();
			} else {
				ogTitle = projectVo.getTitle();
				ogDescription = projectVo.getSummary();
			}
			ogImage = projectVo.getLogo_image();
			if (StringUtil.isEmpty(ogImage)) {
				ogImage = "http://galleryx.io/images/og_image.png";
			}
		}
		
		// SEO - 검색용 본문 설정
		String description = "";
		if ("ko".equals(locale)) {
			description = projectVo.getDescription_ko();
		} else {
			description = projectVo.getDescription();
		}
		
		String frontPath = Constant.SERVER_DOMAIN + "/#/i?s=" + seq + "&t=" + tab + "&l=" + locale;
		String ogUrl = Constant.SERVER_DOMAIN + "/i?s=" + seq + "&t=" + tab + "&l=" + locale;
		model.addAttribute("frontPath", frontPath);
		
		
		model.addAttribute("ogUrl", ogUrl);
		model.addAttribute("ogTitle", ogTitle);
		model.addAttribute("ogDescription", ogDescription);
		model.addAttribute("ogImage", ogImage);
		
		// SEO - 검색용 본문 설정
		model.addAttribute("description", description);
		
		return "project/info";
	}
	
	/**
	 * /v 투표 정보 공유 링크 패스
	 * 
	 * @param Request - seq(vote seq), tab('information' or 'link' or 'comment'), locale('en' or 'ko')
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "v" , method = {RequestMethod.POST, RequestMethod.GET})
	public String v(HttpServletRequest request, HttpSession session, Model model) throws Exception {
		String seq = request.getParameter("s"); // seq
		int intSeq = 0;
		if (!StringUtil.isEmpty(seq)) {
			intSeq = Integer.parseInt(seq);
		}
		String tab = request.getParameter("t"); // tab
		if (StringUtil.isEmpty(tab)) {
			tab = "i";
		}
		String locale = request.getParameter("l"); // locale en/ko
		if (StringUtil.isEmpty(locale)) {
			locale = "en";
		}
		
		// 프로젝트 정보 조회
		VoteVo voteVo = new VoteVo();
		voteVo.setSeq(intSeq);
		try {
			voteVo = this.voteMapper.selectVote(voteVo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// og 정보 설정
		String ogTitle = "";
		String ogDescription = "";
		String ogImage = "";
		
		if (voteVo != null) {
			if ("ko".equals(locale)) {
				ogTitle = voteVo.getTitle_ko();
				ogDescription = voteVo.getProject_title_ko();
			} else {
				ogTitle = voteVo.getTitle();
				ogDescription = voteVo.getProject_title();
			}
			ogImage = voteVo.getMain_image();
			if (StringUtil.isEmpty(ogImage)) {
				ogImage = "http://galleryx.io/images/og_image_vote.png";
			}
		}
		
		// SEO - 검색용 본문 설정
		String description = "";
		if ("ko".equals(locale)) {
			description = voteVo.getDescription_ko();
		} else {
			description = voteVo.getDescription();
		}
		
		String frontPath = Constant.SERVER_DOMAIN + "/#/v?s=" + seq + "&t=" + tab + "&l=" + locale;
		String ogUrl = Constant.SERVER_DOMAIN + "/v?s=" + seq + "&t=" + tab + "&l=" + locale;
		model.addAttribute("frontPath", frontPath);
		
		
		model.addAttribute("ogUrl", ogUrl);
		model.addAttribute("ogTitle", ogTitle);
		model.addAttribute("ogDescription", ogDescription);
		model.addAttribute("ogImage", ogImage);
		
		// SEO - 검색용 본문 설정
		model.addAttribute("description", description);
		
		return "vote/info";
	}
}
