package com.instarverse.api.v1.media.rest;

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
import com.instarverse.api.v1.media.mapper.MediaMapper;
import com.instarverse.api.v1.media.vo.MediaInfoVo;
import com.instarverse.api.v1.media.vo.MediaVo;
import com.instarverse.api.v1.mymedia.vo.MyMediaVo;
import com.instarverse.api.v1.project.mapper.ProjectMapper;
import com.instarverse.api.v1.project.vo.ProjectVo;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class MediaController {
	
	// private static final Logger logger = LoggerFactory.getLogger(MediaController.class);
	
	@Autowired
	private MediaMapper mediaMapper;
	
	@Autowired
	private ProjectMapper projectMapper;
	/**
	 * 전시회  seq로 미디어 리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/media/selectMediaListByProjectSeq")
	public List<MediaInfoVo> selectMediaListByProjectSeq(@RequestParam String uid, @RequestParam int projectSeq, @RequestParam(required = false) String saleYn) throws Exception {
		// 페이징 처리
		MediaInfoVo mediaInfoVo = new MediaInfoVo();
		mediaInfoVo.setProject_seq(projectSeq);
		mediaInfoVo.setSale_yn(saleYn);
		List<MediaInfoVo> mediaInfoVoList = this.mediaMapper.selectMediaListByProjectSeq(mediaInfoVo);
		
		// 프로젝트 테이블 조회
		ProjectVo projectVo = new ProjectVo();
		projectVo.setUid(uid);
		projectVo.setSeq(projectSeq);
		ProjectVo projectResult = this.projectMapper.selectProject(projectVo);
		
		// project 테이블의 media_url_prefix + media 테이블의 nft_id
		String mediaUrlPrefix = projectResult.getMedia_url_prefix();
		if (mediaUrlPrefix != null) {
			for (MediaInfoVo media : mediaInfoVoList) {
				if (media.getNft_id() != null) {
					String fullMediaUrl = mediaUrlPrefix + media.getNft_id();
					media.setNft_market_url(fullMediaUrl);
				}
			}
		}

		
		return mediaInfoVoList;
	}
	
	/**
	 * 미디어 리스트 마지막 페이지 번호 조회(페이징)
	 * 
	 * @param uid
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/media/selectMediaListLastPageNumPaging")
	public int selectMediaListLastPageNumPaging(@RequestParam String uid, @RequestParam int projectSeq, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		MediaVo mediaVo = new MediaVo();
		mediaVo.setKeyword(keyword);
		mediaVo.setProject_seq(projectSeq);
		mediaVo.setPageSize(pageSize);
		return this.mediaMapper.selectMediaListLastPageNumPaging(mediaVo);
	}
	
	/**
	 * 미디어 요청  리스트 조회(페이징)
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/media/selectMediaListPaging")
	public List<MediaVo> selectMediaListPaging(@RequestParam String uid, @RequestParam int exhibition_seq, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String keyword) throws Exception {
		// 페이징 처리
		MediaVo mediaVo = new MediaVo();
		mediaVo.setKeyword(keyword);
		mediaVo.setProject_seq(exhibition_seq);
		mediaVo.setStartRow(CommonUtil.pagingStartRow(pageNum, pageSize));
		mediaVo.setEndRow(CommonUtil.pagingEndRow(pageNum, pageSize));
		return this.mediaMapper.selectMediaListPaging(mediaVo);
	}
	
	/**
	 * 미디어 리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/media/selectMediaList")
	public List<MediaVo> selectMediaList(@RequestParam String uid, @RequestParam int projectSeq) throws Exception {
		// 페이징 처리
		MediaVo mediaVo = new MediaVo();
		mediaVo.setUid(uid);
		mediaVo.setProject_seq(projectSeq);
		return this.mediaMapper.selectMediaList(mediaVo);
	}
	
	/**
	 * 미디어 조회
	 * 
	 * @param uid
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/media/selectMedia")
	public MediaVo selectMedia(@RequestParam String uid, @RequestParam int seq) throws Exception {
		MediaVo mediaVo = new MediaVo();
//		mediaVo.setUid(uid);
		mediaVo.setSeq(seq);
		return this.mediaMapper.selectMedia(mediaVo);
	}
	
	/**
	 * 미디어 재등록을 위한 전체 리스트 조회
	 * 
	 * @param uid
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/media/selectMediaListAll")
	public List<MediaInfoVo> selectMediaListAll(@RequestParam String uid, @RequestParam int projectSeq, @RequestParam(required = false) String saleYn) throws Exception {
		// 페이징 처리
		MediaInfoVo mediaInfoVo = new MediaInfoVo();
		mediaInfoVo.setProject_seq(projectSeq);
		mediaInfoVo.setSale_yn(saleYn);
		List<MediaInfoVo> mediaInfoVoList = this.mediaMapper.selectMediaListAll(mediaInfoVo);
		return mediaInfoVoList;
   }
	
	/**
	 * 미디어 등록
	 * 
	 * @param mediaVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/media/insertMedia")
	public CommonVo insertMedia(@RequestBody MediaVo mediaVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			mediaVo.setReg_id(mediaVo.getUid());
			int resultCount = this.mediaMapper.insertMedia(mediaVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("insertMedia failed.");
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
	 * 미디어 등록(리스트)
	 * 
	 * @param mediaVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/media/insertMediaList")
	public CommonVo insertMediaList(@RequestBody List<MyMediaVo> myMediaVoList) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		
		// 미디어 리스트 order_no 최대값 가져오기
//		System.out.println("myMediaVoList.get(0).getProject_seq()");
//		System.out.println(myMediaVoList.get(0).getProject_seq());
		int maxOrderNO = this.mediaMapper.getMaxOrderNo((int) myMediaVoList.get(0).getProject_seq());
		
		try {
			for(MyMediaVo myMediaVo : myMediaVoList) {
				MediaVo mediaVo = new MediaVo();
				
				mediaVo.setProject_seq((int) myMediaVo.getProject_seq());
				mediaVo.setMy_media_seq(myMediaVo.getSeq());
				mediaVo.setSale_yn(myMediaVo.getSale_yn());
				mediaVo.setPrice(myMediaVo.getPrice());
				mediaVo.setReg_id(mediaVo.getReg_id());
				mediaVo.setOrder_no(++maxOrderNO);
				mediaVo.setReg_id(myMediaVo.getUid());
				
				// 기본 필드 설정
				mediaVo.setDel_yn("N");
				
				int resultCount = this.mediaMapper.insertMedia(mediaVo);
				if (resultCount == 0) {
					commonVo.setResultCd("FAIL");
					commonVo.setResultMsg("insertMedia failed.");
					return commonVo;
				}
			}
		} catch (Exception e) {
			// 결과코드 : 실패
			commonVo.setResultCd("FAIL");
			commonVo.setResultMsg(e.toString());
		}
		return commonVo;
	}
	
	/**
	 * 미디어 수정
	 * 
	 * @param mediaVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/media/updateMedia")
	public CommonVo updateMedia(@RequestBody MediaVo mediaVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			mediaVo.setMod_id(mediaVo.getUid());
			int resultCount = this.mediaMapper.updateMedia(mediaVo);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateMedia failed.");
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
	 * 미디어 삭제 (리스트)
	 * 
	 * @param mediaVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/media/deleteMediaList")
	public CommonVo deleteMediaList(@RequestBody List<MediaVo> mediaList) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		
	    try {
	        for (MediaVo mediaVo : mediaList) {
	            // 각 미디어의 삭제 정보를 설정
	            mediaVo.setDel_id(mediaVo.getUid());
	            mediaVo.setDel_yn("Y");

	            // 데이터베이스 업데이트 (삭제 처리)
	            int resultCount = this.mediaMapper.deleteMedia(mediaVo);
	            if (resultCount == 0) {
	                commonVo.setResultCd("FAIL");
	                commonVo.setResultMsg("deleteMedia failed for seq: " + mediaVo.getSeq());
	                return commonVo;
	            }
	        }
	    } catch (Exception e) {
	        // 결과코드 : 실패
	        commonVo.setResultCd("FAIL");
	        commonVo.setResultMsg(e.toString());
	        return commonVo;
	    }

	    return commonVo;
	}
	
	/**
	 * 마이 미디어 seq로 삭제
	 * 
	 * @param mediaVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/media/deleteMediaByMyMediaSeq")
	public CommonVo deleteMediaByMyMediaSeq(@RequestBody MediaVo mediaVo) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		
	    try {
			// 각 미디어의 삭제 정보를 설정
			mediaVo.setDel_yn("Y");
			
			// 데이터베이스 업데이트 (삭제 처리)
			int resultCount = this.mediaMapper.deleteMediaByMyMediaSeq(mediaVo);
			if (resultCount == 0) {
			    commonVo.setResultCd("FAIL");
			    commonVo.setResultMsg("deleteMedia failed for seq: " + mediaVo.getSeq());
			    return commonVo;
			}
	    } catch (Exception e) {
	        // 결과코드 : 실패
	        commonVo.setResultCd("FAIL");
	        commonVo.setResultMsg(e.toString());
	        return commonVo;
	    }

	    return commonVo;
	}
	
	/**
	 * 미디어 재등록
	 * 
	 * @param mediaVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/media/restoreMedia")
	public CommonVo restoreMedia(@RequestBody List<MediaVo> mediaList) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		
	    try {
	        for (MediaVo mediaVo : mediaList) {
	            // 각 미디어의 삭제 정보를 설정
	            mediaVo.setDel_id(null);
	            mediaVo.setDel_time(null);
	            mediaVo.setDel_yn("N");

	            // 데이터베이스 업데이트 (삭제 처리)
	            int resultCount = this.mediaMapper.restoreMedia(mediaVo);
	            if (resultCount == 0) {
	                commonVo.setResultCd("FAIL");
	                commonVo.setResultMsg("restoreMedia failed for seq: " + mediaVo.getSeq());
	                return commonVo;
	            }
	        }
	    } catch (Exception e) {
	        // 결과코드 : 실패
	        commonVo.setResultCd("FAIL");
	        commonVo.setResultMsg(e.toString());
	        return commonVo;
	    }

	    return commonVo;
	}
	
	/**
	 * 미디어 리스트 order_no 수정
	 * 
	 * @param mediaVo
	 * @return commonVo
	 * @throws Exception
	 */
	@PostMapping("/media/updateMediaOrderNoList")
	public CommonVo updateMediaOrderNoList(@RequestBody List<MediaInfoVo> mediaInfoVoList) throws Exception {
		CommonVo commonVo = new CommonVo();
		commonVo.setResultCd("SUCCESS");
		try {
			int resultCount = this.mediaMapper.updateMediaOrderNoList(mediaInfoVoList);
			if (resultCount == 0) {
				commonVo.setResultCd("FAIL");
				commonVo.setResultMsg("updateMedia failed.");
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
