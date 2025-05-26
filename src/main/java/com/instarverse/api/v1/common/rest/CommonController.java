package com.instarverse.api.v1.common.rest;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.instarverse.api.v1.common.mapper.FileMapper;
import com.instarverse.api.v1.common.util.FileUtil;
import com.instarverse.api.v1.common.vo.FileVo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


//@ApiIgnore
@RestController
@Transactional
@RequestMapping(value = "/api")
public class CommonController {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
	
	@Autowired
	private FileMapper fileMapper;
	
//	@Autowired
//	private FileMstMapper fileMstMapper;

	@Value("${file.server-ip}")
	private String fileServerIp;
	
	@Value("${file.server-ip-local}")
	private String fileServerIpLocal;
	
	@Value("${file.upload-path}")
	private String fileUploadPath;
	
	@Value("${file.upload-path-thumb}")
	private String fileUploadPathThumb;
	
	@Value("${file.upload-path-local}")
	private String fileUploadPathLocal;
	
	@Value("${file.upload-path-local-thumb}")
	private String fileUploadPathLocalThumb;
	
	@Value("${file.uploaded-path}")
	private String fileUploadedPath;
	
	@Value("${file.uploaded-path-thumb}")
	private String fileUploadedPathThumb;
	
	@Value("${file.upload-path-nft}")
	private String fileUploadPathNft;
	
	@Value("${file.upload-path-nft-local}")
	private String fileUploadPathNftLocal;
	
	@Value("${file.uploaded-path-nft}")
	private String fileUploadedPathNft;
	
	@Value("${file.upload-path-json}")
	private String fileUploadPathJson;
	
	@Value("${file.uploaded-path-json}")
	private String fileUploadedPathJson;
	
	/**
	 * 이미지를 업로드 한다.
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/common/uploadImage")
	public String uploadImage(
			@RequestPart(value = "file") final MultipartFile file
			, HttpServletRequest request
			, HttpServletResponse response
			, HttpSession session
			, Model model) throws Exception {
		logger.debug("####### uploadImage called!");
		logger.debug("####### file.getOriginalFilename(): " + file.getOriginalFilename());
		logger.debug("####### file.getSize(): " + file.getSize());
		logger.debug("####### file.getContentType(): " + file.getContentType());
		
		int seqFileMst = 0;
		String uid = "SYSTEM"; // 세션 uid
		String fileUrl = this.saveFile(file, seqFileMst, uid);
		
		return fileUrl; // 파일 URL
	}
	
	/**
	 * 이미지를 업로드, 썸네일 업로드 한다.
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/common/uploadImageAndThumbnail")
	public Map<String, String> uploadImageAndThumbnail(
			@RequestPart(value = "file") final MultipartFile file
			, HttpServletRequest request
			, HttpServletResponse response
			, HttpSession session
			, Model model) throws Exception {
		logger.debug("####### uploadImage called!");
		logger.debug("####### file.getOriginalFilename(): " + file.getOriginalFilename());
		logger.debug("####### file.getSize(): " + file.getSize());
		logger.debug("####### file.getContentType(): " + file.getContentType());
		int seqFileMst = 0;
		String uid = "SYSTEM"; // 세션 uid
		Map<String, String> url = this.saveFileAndThumbnail(file, seqFileMst, uid);
		
		return url; // 파일 URL
	}
	
	/**
	 * 파일을 저장한다.
	 * 
	 * @param file
	 * @param seqFileMst
	 * @param uid
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	private String saveFile(MultipartFile file, int seqFileMst, String uid) throws IOException, Exception {
//		if (seqFileMst == null || "".equals(seqFileMst)) {
//			seqFileMst = "0";
//		}
		// 1. 파일 저장
		String sourceFileName = file.getOriginalFilename();
		String sourceFileNameExtension = FileUtil.getExtension(sourceFileName).toLowerCase();
		File destinationFile;
		String destinationFileName;
		
		String fileUploadRealPath = ""; // 서버상에 저장되는 패스
		String folderName = FileUtil.getYYYYMMDDFolderName();
		
		do {
			destinationFileName = FileUtil.getNewFileName() + "." + sourceFileNameExtension;
			fileUploadRealPath = this.fileUploadPath + "/" + folderName + "/" + destinationFileName;
			destinationFile = new File(fileUploadRealPath);
		} while (destinationFile.exists());
		destinationFile.getParentFile().mkdirs();
		
		// 운영 패스가 예외처리 될 경우 로컬 패스로 실행 - 로컬 테스트용 - 상수값 계속 수정하기 귀찮아서..
		boolean localFlag = false;
		try {
			file.transferTo(destinationFile);
		} catch (Exception e) {
			// e.printStackTrace();
			logger.info("■■■■■■■ file.transferTo(destinationFile) failed. Set to local path....");
			
			// 로컬 패스로 파일 저장
			fileUploadRealPath = this.fileUploadPathLocal + "/" + folderName + "/" + destinationFileName;
			destinationFile = new File(fileUploadRealPath);
			destinationFile.getParentFile().mkdirs();
			file.transferTo(destinationFile);
			
			localFlag = true;
		}
		
		// 2. DB에 등록
		// String fileUrl = Constant.SERVER_DOMAIN + Constant.UPLOADED_FILE_PATH + "/" + destinationFileName;
		// InetAddress inet = InetAddress.getLocalHost();
		// String serverIp = Constant.SERVER_DOMAIN;
		// serverIp = inet.getHostAddress(); // 테스트용
		String fileUrl = "https://" + this.fileServerIp + this.fileUploadedPath + "/" + folderName + "/" + destinationFileName;
		if (localFlag) {
			fileUrl = "http://" + this.fileServerIpLocal + this.fileUploadedPath + "/" + folderName + "/" + destinationFileName;
		}
		
		FileVo fileVo = new FileVo();
		fileVo.setName(destinationFileName);
		fileVo.setName_original(sourceFileName);
		fileVo.setDir_path(fileUploadRealPath);
		fileVo.setUrl_path(fileUrl);
		fileVo.setSize(String.valueOf(file.getSize()));
		fileVo.setExtension(sourceFileNameExtension);
		fileVo.setContent_type(file.getContentType());
		fileVo.setUid(uid);
		fileVo.setSeq_file_mst(seqFileMst);
		this.fileMapper.insertFile(fileVo);
		logger.debug("### uploaded file SEQ: " + fileVo.getSeq());
		return fileUrl;
	}
	
	/**
	 * 파일과 썸네일을 저장한다.
	 * 
	 * @param file
	 * @param seqFileMst
	 * @param uid
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	private Map<String, String> saveFileAndThumbnail(MultipartFile file, int seqFileMst, String uid) throws IOException, Exception {
		Map<String, String> url = new HashMap<>();
		
		String sourceFileName = file.getOriginalFilename();
		String sourceFileNameExtension = FileUtil.getExtension(sourceFileName).toLowerCase();
		File destinationFile;
		String destinationFileName;
		String newFileName;
		
		String fileUploadRealPath = ""; // 서버상에 저장되는 패스
		String folderName = FileUtil.getYYYYMMDDFolderName();
		
		do {
			newFileName = FileUtil.getNewFileName();
		    destinationFileName = newFileName + "." + sourceFileNameExtension;
		    fileUploadRealPath = this.fileUploadPath + "/" + folderName + "/" + destinationFileName;
		    destinationFile = new File(fileUploadRealPath);
		} while (destinationFile.exists());
		destinationFile.getParentFile().mkdirs();
		
		boolean localFlag = false;
		try {
		    file.transferTo(destinationFile);
		} catch (Exception e) {
			logger.info("■■■■■■■ file.transferTo(destinationFile) failed. Set to local path....");
			
			fileUploadRealPath = this.fileUploadPathLocal + "/" + folderName + "/" + destinationFileName;
			destinationFile = new File(fileUploadRealPath);
			destinationFile.getParentFile().mkdirs();
			file.transferTo(destinationFile);
			
			localFlag = true;
		}
		
		// 썸네일 생성 및 저장, 그리고 URL 반환
		String thumbnailUrl = createThumbnail(destinationFile
											, newFileName
											, folderName
											, seqFileMst
											, uid
											, localFlag);
		
		String fileUrl = "https://" + this.fileServerIp + this.fileUploadedPath + "/" + folderName + "/" + destinationFileName;
		if (localFlag) {
		    fileUrl = "http://" + this.fileServerIpLocal + this.fileUploadedPath + "/" + folderName + "/" + destinationFileName;
		}
		
		url.put("fileUrl", fileUrl);
		url.put("thumnailUrl", thumbnailUrl);
		
		FileVo fileVo = new FileVo();
		fileVo.setName(destinationFileName);
		fileVo.setName_original(sourceFileName);
		fileVo.setDir_path(fileUploadRealPath);
		fileVo.setUrl_path(fileUrl);
		fileVo.setSize(String.valueOf(file.getSize()));
		fileVo.setExtension(sourceFileNameExtension);
		fileVo.setContent_type(file.getContentType());
		fileVo.setUid(uid);
		fileVo.setSeq_file_mst(seqFileMst);
		this.fileMapper.insertFile(fileVo);
		logger.debug("### uploaded file SEQ: " + fileVo.getSeq());
		
		return url;
	}
	
	/**
	 * 썸네일을 만들고 저장한다.
	 * 
	 * @param file
	 * @param seqFileMst
	 * @param uid
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	private String createThumbnail(File destinationFile, String newFileName, String folderName, int seqFileMst, String uid, boolean localFlag) throws IOException {
        String thumbnailFileName;
        String thumbnailFilePath;
        File thumbnailFile;

        // 썸네일 파일명이 충돌하지 않도록 파일명을 생성하는 로직 추가
        do {
            thumbnailFileName = newFileName + "_thumb" + ".jpeg";
            thumbnailFilePath = this.fileUploadPathThumb + "/" + folderName + "/" + thumbnailFileName;
            thumbnailFile = new File(thumbnailFilePath);
        } while (thumbnailFile.exists());
        thumbnailFile.getParentFile().mkdirs();

        // 원본 이미지 읽기
        BufferedImage originalImage = ImageIO.read(destinationFile);

        int targetWidth = 512;
        int targetHeight = (int) (originalImage.getHeight() * ((double) targetWidth / originalImage.getWidth()));

        // 점진적 축소 (메모리 절약을 위해 여러 단계로 줄임)
        BufferedImage scaledImage = originalImage;
        while (scaledImage.getWidth() > targetWidth * 2) {
            int intermediateWidth = scaledImage.getWidth() / 2;
            int intermediateHeight = scaledImage.getHeight() / 2;
            BufferedImage intermediateImage = new BufferedImage(intermediateWidth, intermediateHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = intermediateImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(scaledImage, 0, 0, intermediateWidth, intermediateHeight, null);
            g.dispose();
            scaledImage = intermediateImage;
        }

        // 최종 크기로 리사이즈
        BufferedImage thumbnailImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = thumbnailImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(scaledImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        // 썸네일을 파일에 저장
        ImageIO.write(thumbnailImage, "jpeg", thumbnailFile);
        logger.info("### Thumbnail created at: " + thumbnailFilePath);

        // 썸네일 파일 정보 DB 저장
        String thumbnailUrl = "https://" + this.fileServerIp + this.fileUploadedPathThumb + "/" + folderName + "/" + thumbnailFileName;
        if (localFlag) {
            thumbnailUrl = "http://" + this.fileServerIpLocal + this.fileUploadedPathThumb + "/" + folderName + "/" + thumbnailFileName;
        }

        FileVo thumbnailFileVo = new FileVo();
        System.out.println("@@@@@@@@@@@@" + thumbnailFileName);
        thumbnailFileVo.setName(thumbnailFileName);
        thumbnailFileVo.setName_original(destinationFile.getName());
        thumbnailFileVo.setDir_path(thumbnailFilePath);
        thumbnailFileVo.setUrl_path(thumbnailUrl);
        thumbnailFileVo.setSize(String.valueOf(thumbnailFile.length()));
        thumbnailFileVo.setExtension("jpeg");
        thumbnailFileVo.setContent_type("image/jpeg"); // 썸네일은 보통 JPEG 형식
        thumbnailFileVo.setUid(uid);
        thumbnailFileVo.setSeq_file_mst(seqFileMst);
        try {
            this.fileMapper.insertFile(thumbnailFileVo);
        } catch (Exception e) {
            logger.error("파일 정보를 데이터베이스에 삽입하는 도중 오류가 발생했습니다.", e);
            throw new IOException("파일 정보 DB 삽입 실패", e);  // 필요시 새로운 예외 던짐
        }

        logger.debug("### Thumbnail uploaded file SEQ: " + thumbnailFileVo.getSeq());

        return thumbnailUrl;
    }

	
	/**
	 * NFT 파일을 저장한다.
	 * image, json : folderName 구분
	 * 
	 * @param file
	 * @param folderName
	 * @param newFileName
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public String saveNftFile(MultipartFile file, String folderName, String newFileName) throws IOException, Exception {
		// 1. 파일 저장
		String sourceFileName = file.getOriginalFilename();
		String sourceFileNameExtension = FileUtil.getExtension(sourceFileName).toLowerCase();
		File destinationFile;
		String destinationFileName;
		
		String fileUploadRealPath = ""; // 서버상에 저장되는 패스
		
		// do {
		destinationFileName = newFileName + "." + sourceFileNameExtension; // newFileName = 1, 2, 3 순으로 호출됨.
		fileUploadRealPath = this.fileUploadPathNft + "/" + folderName + "/" + destinationFileName;
		destinationFile = new File(fileUploadRealPath);
		// } while (destinationFile.exists());
		destinationFile.getParentFile().mkdirs();
		
		// 운영 패스가 예외처리 될 경우 로컬 패스로 실행 - 로컬 테스트용 - 상수값 계속 수정하기 귀찮아서..
		boolean localFlag = false;
		try {
			file.transferTo(destinationFile);
		} catch (Exception e) {
			// e.printStackTrace();
			logger.info("■■■■■■■ NFT file.transferTo(destinationFile) failed. Set to local path....");
			
			// 로컬 패스로 파일 저장
			fileUploadRealPath = this.fileUploadPathNftLocal + "/" + folderName + "/" + destinationFileName;
			destinationFile = new File(fileUploadRealPath);
			destinationFile.getParentFile().mkdirs();
			file.transferTo(destinationFile);
			
			localFlag = true;
		}
		
		String fileUrl = "https://" + this.fileServerIp + this.fileUploadedPathNft + "/" + folderName + "/" + destinationFileName;
		if (localFlag) {
			fileUrl = "http://" + this.fileServerIpLocal + this.fileUploadedPathNft + "/" + folderName + "/" + destinationFileName;
		}
		
		return fileUrl;
	}
	
	/**
	 * JSON 파일을 저장한다.
	 * 
	 * @param projectTitle
	 * @param jsonObject
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public String saveJsonFile(int lastNftId, int projectSeq, String jsonString) throws IOException, Exception {
//			if (seqFileMst == null || "".equals(seqFileMst)) {
//				seqFileMst = "0";
//			}
		
		// 1. 파일 저장
		String sourceFileNameExtension = "json";
		String fileName = lastNftId + "." + sourceFileNameExtension; // ex) bravekongz_collection.json
		
		// Define the directory and file path
		String filePath = this.fileUploadPathJson + "/" + projectSeq + "/" + fileName;
		
		// 운영 패스가 예외처리 될 경우 로컬 패스로 실행 - 로컬 테스트용 - 상수값 계속 수정하기 귀찮아서..
		boolean localFlag = false;
		
		// Ensure the directory exists
		Path path = Paths.get(this.fileUploadPathJson + "/" + projectSeq);

		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
				System.out.println("Directory created: " + this.fileUploadPathJson + "/" + projectSeq);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Write the JSON object to the specified file
		try {
			FileWriter fileWriter = new FileWriter(filePath);
			fileWriter.write(jsonString);
			fileWriter.flush();
			fileWriter.close();
			System.out.println("JSON file created: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
			
//				// 로컬 환경으로 설정 
//				filePath = this.fileUploadPathJsonLocal + "/" + projectId + "/"  + fileName;
//				path = Paths.get(this.fileUploadPathJsonLocal);
//				
//				if (!Files.exists(path)) {
//					try {
//						Files.createDirectories(path);
//						System.out.println("Local Directory created: " + this.fileUploadPathJsonLocal);
//					} catch (IOException e2) {
//						e2.printStackTrace();
//					}
//				}
//				localFlag = true;
//				
//				FileWriter fileWriter = new FileWriter(filePath);
//				fileWriter.write(jsonObject.toJSONString());
//				fileWriter.flush();
//				fileWriter.close();
		}
		
		// file URL 작성
		String fileUrl = "https://" + this.fileServerIp + this.fileUploadedPathJson + "/" + projectSeq + "/" + fileName;
		if (localFlag) {
			fileUrl = "http://" + this.fileServerIpLocal + this.fileUploadedPathJson + "/" + projectSeq + "/" + fileName;
		}
		
		// file 테이블에 로그 등록
		FileVo fileVo = new FileVo();
		fileVo.setName(fileName);
		fileVo.setName_original(fileName);
		fileVo.setDir_path(filePath);
		fileVo.setUrl_path(fileUrl);
		// fileVo.setSize(String.valueOf(file.getSize()));
		fileVo.setExtension(sourceFileNameExtension);
		// fileVo.setContent_type(file.getContentType());
		fileVo.setUid("SYSTEM");
		fileVo.setSeq_file_mst(0);
		this.fileMapper.insertFile(fileVo);
		logger.debug("### uploaded file SEQ: " + fileVo.getSeq());
		logger.debug("### uploaded file URL: " + fileUrl);
		
		return fileUrl;
	}
}
