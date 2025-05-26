package com.instarverse.api.v1.sitemap.rest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import com.instarverse.api.v1.common.util.DateUtil;
import com.instarverse.api.v1.common.util.FileUtil;
import com.instarverse.api.v1.common.vo.CommonVo;
import com.instarverse.api.v1.sitemap.mapper.SitemapMapper;
import com.instarverse.api.v1.sitemap.vo.SitemapVo;
import com.instarverse.api.v1.user.mapper.UserMapper;
import com.instarverse.api.v1.user.vo.UserVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class SitemapController {
	
	private static final Logger logger = LoggerFactory.getLogger(SitemapController.class);
	
	@Value("${file.sitemap-path}")
	private String fileSitemapPath;
	
	@Value("${file.sitemap-backup-path}")
	private String fileSitemapBackupPath;
	
	@Autowired
	private SitemapMapper sitemapMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	/**
	 * 사이트맵 파일을 생성한다.
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/sitemap/createSitemapFile")
	public CommonVo createSitemapFile(@RequestParam String uid) throws Exception {
		logger.debug("####### createSitemapFile called!");
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		
		// 사용자 존재 여부 확인
		UserVo userVo = this.userMapper.selectUser(uid);
		if (userVo == null) {
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg("User does not exist");
			return commonVo;
		}
		// 관리자 여부 확인 adcd = 1029
		if (!"1029".equals(userVo.getAdcd())) {
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg("User is not an admin");
			return commonVo;
		}
		
		String lastmod = DateUtil.getYYYYMMDD(); // '2019-06-14' ※ "2019-06-13T23:09:27+00:00" 형식으로 안해도 된다는디 ㅋ optional field라서 무관
		String changefreq = "weekly"; // always hourly daily weekly monthly yearly never
		String priority = "1.0";
		
		try {
			// 생성될 파일
			// 'sitemap.xml'
			String fileName = this.fileSitemapPath + "/" + Constant.SITEMAP_FILE_FULL_NAME;
			
			// 백업용 파일
			// '/old/sitemap_yyyyMMddHHmmss.txt'
			String backupFileName = this.fileSitemapBackupPath + "/"
								+ Constant.SITEMAP_FILE_NAME + "_" + DateUtil.getYYYYMMDDHHMMSS() + ".xml";
			
			// 파일 객체 생성
			File file = new File(fileName);
			
			// 파일 존재시 백업 후 삭제
			if (file.exists()) {
				// 파일 백업
				FileUtil.fileCopy(fileName, backupFileName);
				// 파일 삭제
				file.delete();
			}
			
			// BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
			BufferedWriter fw = new BufferedWriter(new FileWriter(fileName, true));
			
			// 1. 사이트맵 조회
			List<SitemapVo> sitemapList = this.sitemapMapper.selectSitemapList();
			
			// 2. 헤더 작성
			String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n";
			header += "<urlset" + "\n";
			header += "\t" + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + "\n";
			header += "\t" + "xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"" + "\n";
			header += "\t" + "xsi:schemaLocation=\"http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd\"" + "\n";
			header += "\t" + "xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">" + "\n";
			
			// ■ 헤더 작성
			fw.write(header);
			fw.newLine(); // 개행
			
			// 3. 사이트맵 리스트 루프 돌면서 작성
			SitemapVo sitemapVo;
			for (int i = 0; i < sitemapList.size(); i++) {
				sitemapVo = sitemapList.get(i);
				
				String sitemapData = "";
				
				sitemapData += "<url>" + "\n";
				sitemapData += "\t" + "<loc>" + sitemapVo.getLoc() + "</loc>" + "\n";
				sitemapData += "\t" + "<lastmod>" + lastmod + "</lastmod>" + "\n";
				sitemapData += "\t" + "<changefreq>" + sitemapVo.getChangefreq() + "</changefreq>" + "\n";
				sitemapData += "\t" + "<priority>" + sitemapVo.getPriority() + "</priority>" + "\n";
				sitemapData += "</url>" + "\n";
				
				fw.write(sitemapData); // ■ 사이트맵 1개 작성
			}
			sitemapList = null;
			
			int maxSeq = 0; // MAX SEQ
			
			// 4. 토큰 상세 페이지 작성
			maxSeq = this.sitemapMapper.selectTokenMaxSeq();
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// l=en
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// t=i&l=en - tab=information, locale=en
			for (int seq = 1; seq <= maxSeq; seq++) {
				
				String sitemapData = "";
				
				sitemapData += "<url>" + "\n";
				sitemapData += "\t" + "<loc>" + Constant.SERVER_DOMAIN + "/i?s=" + seq + "&amp;t=i" + "&amp;l=en" + "</loc>" + "\n";
				sitemapData += "\t" + "<lastmod>" + lastmod + "</lastmod>" + "\n";
				sitemapData += "\t" + "<changefreq>" + changefreq + "</changefreq>" + "\n";
				sitemapData += "\t" + "<priority>" + priority + "</priority>" + "\n";
				sitemapData += "</url>" + "\n";
				
				fw.write(sitemapData); // ■ 사이트맵 1개 작성
			}
			// t=l&l=en - tab=link, locale=en
			for (int seq = 1; seq <= maxSeq; seq++) {
				
				String sitemapData = "";
				
				sitemapData += "<url>" + "\n";
				sitemapData += "\t" + "<loc>" + Constant.SERVER_DOMAIN + "/i?s=" + seq + "&amp;t=l" + "&amp;l=en" + "</loc>" + "\n";
				sitemapData += "\t" + "<lastmod>" + lastmod + "</lastmod>" + "\n";
				sitemapData += "\t" + "<changefreq>" + changefreq + "</changefreq>" + "\n";
				sitemapData += "\t" + "<priority>" + priority + "</priority>" + "\n";
				sitemapData += "</url>" + "\n";
				
				fw.write(sitemapData); // ■ 사이트맵 1개 작성
			}
			// t=c&l=en - tab=comment, locale=en
			for (int seq = 1; seq <= maxSeq; seq++) {
				
				String sitemapData = "";
				
				sitemapData += "<url>" + "\n";
				sitemapData += "\t" + "<loc>" + Constant.SERVER_DOMAIN + "/i?s=" + seq + "&amp;t=c" + "&amp;l=en" + "</loc>" + "\n";
				sitemapData += "\t" + "<lastmod>" + lastmod + "</lastmod>" + "\n";
				sitemapData += "\t" + "<changefreq>" + changefreq + "</changefreq>" + "\n";
				sitemapData += "\t" + "<priority>" + priority + "</priority>" + "\n";
				sitemapData += "</url>" + "\n";
				
				fw.write(sitemapData); // ■ 사이트맵 1개 작성
			}
			
			
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// l=ko
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// t=i&l=ko - tab=information, locale=ko
			for (int seq = 1; seq <= maxSeq; seq++) {
				
				String sitemapData = "";
				
				sitemapData += "<url>" + "\n";
				sitemapData += "\t" + "<loc>" + Constant.SERVER_DOMAIN + "/i?s=" + seq + "&amp;t=i" + "&amp;l=ko" + "</loc>" + "\n";
				sitemapData += "\t" + "<lastmod>" + lastmod + "</lastmod>" + "\n";
				sitemapData += "\t" + "<changefreq>" + changefreq + "</changefreq>" + "\n";
				sitemapData += "\t" + "<priority>" + priority + "</priority>" + "\n";
				sitemapData += "</url>" + "\n";
				
				fw.write(sitemapData); // ■ 사이트맵 1개 작성
			}
			// t=l&l=ko - tab=link, locale=ko
			for (int seq = 1; seq <= maxSeq; seq++) {
				
				String sitemapData = "";
				
				sitemapData += "<url>" + "\n";
				sitemapData += "\t" + "<loc>" + Constant.SERVER_DOMAIN + "/i?s=" + seq + "&amp;t=l" + "&amp;l=ko" + "</loc>" + "\n";
				sitemapData += "\t" + "<lastmod>" + lastmod + "</lastmod>" + "\n";
				sitemapData += "\t" + "<changefreq>" + changefreq + "</changefreq>" + "\n";
				sitemapData += "\t" + "<priority>" + priority + "</priority>" + "\n";
				sitemapData += "</url>" + "\n";
				
				fw.write(sitemapData); // ■ 사이트맵 1개 작성
			}
			// t=c&l=ko - tab=comment, locale=ko
			for (int seq = 1; seq <= maxSeq; seq++) {
				
				String sitemapData = "";
				
				sitemapData += "<url>" + "\n";
				sitemapData += "\t" + "<loc>" + Constant.SERVER_DOMAIN + "/i?s=" + seq + "&amp;t=c" + "&amp;l=ko" + "</loc>" + "\n";
				sitemapData += "\t" + "<lastmod>" + lastmod + "</lastmod>" + "\n";
				sitemapData += "\t" + "<changefreq>" + changefreq + "</changefreq>" + "\n";
				sitemapData += "\t" + "<priority>" + priority + "</priority>" + "\n";
				sitemapData += "</url>" + "\n";
				
				fw.write(sitemapData); // ■ 사이트맵 1개 작성
			}
			
			
			// 5. urlset 종료 태그 작성
			fw.write("</urlset>");
			
			fw.flush();
			
			// 6. 객체 닫기
			fw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			// 에러메세지 결과로 설정
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
			
		}
		
		return commonVo;
	}
	
}
