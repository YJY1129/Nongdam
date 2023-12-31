package kr.co.ezen.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.JsonObject;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import kr.co.ezen.entity.Criteria;
import kr.co.ezen.entity.Imgur;
import kr.co.ezen.entity.PageCre;
import kr.co.ezen.entity.Tr;
import kr.co.ezen.entity.TrComment;
import kr.co.ezen.service.TrService;

@Controller
@RequestMapping("/tr/*")
public class TrController {

	@Autowired
	private TrService trService;

	@RequestMapping("/main")
	public String main(Model m, Criteria cri) {
		if (cri.getKeyword() == null) {
			cri.setKeyword("");
		}

		List<Tr> li = trService.findAll(cri);

		m.addAttribute("li", li);
		m.addAttribute("cri", cri);

		PageCre pageCre = new PageCre();
		pageCre.setCri(cri); // PageCre의 Criteria cri 필드에 매개변수 cri 넣음
		pageCre.setTotalCount(trService.totalCount(cri)); // int 총 게시글 수

		m.addAttribute("pageCre", pageCre);
		return "tr/main";
	}

	@GetMapping("/detail")
	public String detail(@RequestParam("tr_idx") int tr_idx, Model m, Criteria cri) {
		// 게시글조회

		Tr vo = trService.findByIdx(tr_idx);
		List<TrComment> cvo = trService.findAllComment(tr_idx);

		m.addAttribute("vo", vo);
		m.addAttribute("cvo", cvo);
		m.addAttribute("cri",cri);

		return "tr/detail";
	}

	@GetMapping("/write")
	public String write() {

		return "tr/write";
	}

	@PostMapping("/write")
	public String write(@RequestParam("tr_imgpath") MultipartFile file, @RequestParam("tr_title") String tr_title,
			@RequestParam("user_idx") String user_idx, @RequestParam("tr_content") String tr_content, Model m,
			RedirectAttributes rttr, HttpServletRequest request) throws IOException {

		String uploadPath = request.getServletContext().getRealPath("/resources/image/tr");
     	Tr vo = new Tr();
	    vo.setTr_title(tr_title);
	    vo.setUser_idx(user_idx);
	    vo.setTr_content(tr_content);

	    if (file != null && !file.isEmpty()) {
	    	String ckEditorFileName = file.getOriginalFilename();
	    	String ext = ckEditorFileName.substring(ckEditorFileName.lastIndexOf(".") + 1).toUpperCase();

	    	// 썸네일 파일 저장
	    	String thumbnailFileName = "thumbnail.png";
	    	String thumbnailFilePath = uploadPath + File.separator + thumbnailFileName;
	    	File thumbnailDest = new File(thumbnailFilePath);
	    	file.transferTo(thumbnailDest);

	    	// 썸네일 이미지 업로드
	    	Imgur imgur = new Imgur();
	    	String thumbnailImageUrl = imgur.requestUpload(Files.readAllBytes(thumbnailDest.toPath())); // 썸네일 이미지 업로드
                                                                              // 서비스 호출 및 URL

	    	// 데이터베이스에 필요한 정보 등록
	    	vo.setTr_imgpath(thumbnailImageUrl); // 썸네일 이미지 URL을 vo에 설정

	    	// 원본 이미지 저장
	    	String ckEditorFileNameWithoutExt = ckEditorFileName.substring(0, ckEditorFileName.lastIndexOf("."));
	    	String ckEditorImageFileName = ckEditorFileNameWithoutExt + "_original." + ext;
	    	String ckEditorFilePath = uploadPath + File.separator + ckEditorImageFileName;
	    	File ckEditorDest = new File(ckEditorFilePath);
	    	file.transferTo(ckEditorDest);

	    	// 원본 이미지 업로드
	    	String ckEditorImageUrl = imgur.requestUpload(Files.readAllBytes(ckEditorDest.toPath())); // 원본 이미지 업로드 서비스

	    }

	    trService.insert(vo); // 데이터베이스에 저장
	    System.out.println(vo.getTr_imgpath());
	    System.out.println(vo.getTr_content());
	    return "redirect:/tr/main";
	}

	
	@GetMapping("/modify")
	public String modify(Model m, @RequestParam("tr_idx") int tr_idx) {

		Tr vo = trService.findByIdx(tr_idx);
		m.addAttribute("vo", vo);
		return "/tr/modify";
	}

	@PostMapping("/modify")
	public String modify(@RequestParam("tr_imgpath") MultipartFile file,
                       @RequestParam("tr_idx") int tr_idx,
                       @RequestParam("tr_title") String tr_title,
                       @RequestParam("tr_content") String tr_content,
                       @RequestParam(value = "existing_image", required = false) String existingImage,
                       HttpServletRequest request,
                       RedirectAttributes rttr,
                       Criteria cri) throws IOException {

		Tr existingTr = trService.findByIdx(tr_idx);
		String existingThumbnail = existingTr.getTr_imgpath();

		Tr vo = new Tr();
		vo.setTr_idx(tr_idx);
		vo.setTr_title(tr_title);
		vo.setTr_content(tr_content);

		rttr.addAttribute("tr_idx", tr_idx);

		// 기존 이미지 삭제 로직은 유지됩니다.
		if (existingImage != null && !existingImage.isEmpty() && !file.isEmpty()) {
			String imagePath = request.getServletContext().getRealPath("/resources/image/tr/") + existingImage;
			File existingFile = new File(imagePath);

			if (existingFile.exists()) {
				if (existingFile.delete()) {
					System.out.println("기존 파일 삭제 성공");
				} else {
                   System.out.println("기존 파일 삭제 실패");
               }
           } else {
               System.out.println("존재하지 않는 파일입니다.");
           }
       }

       // 파일이 비어있지 않은 경우에만 업로드 진행
       if (file != null && !file.isEmpty()) {
    	   String ckEditorFileName = file.getOriginalFilename();
           String ext = ckEditorFileName.substring(ckEditorFileName.lastIndexOf(".") + 1).toUpperCase();

           // 썸네일 파일 저장
           String thumbnailFileName = "thumbnail.png";
           String thumbnailFilePath = request.getServletContext().getRealPath("/resources/image/tr") + File.separator + thumbnailFileName;
           File thumbnailDest = new File(thumbnailFilePath);
           file.transferTo(thumbnailDest);

           // 썸네일 이미지 업로드
           Imgur imgur = new Imgur();
           String thumbnailImageUrl = imgur.requestUpload(Files.readAllBytes(thumbnailDest.toPath())); // 썸네일 이미지 업로드 서비스 호출 및 URL 받아오기

           // 데이터베이스에 필요한 정보 등록
           vo.setTr_imgpath(thumbnailImageUrl); // 썸네일 이미지 URL을 vo에 설정

           // 원본 이미지 저장
           String ckEditorFileNameWithoutExt = ckEditorFileName.substring(0, ckEditorFileName.lastIndexOf("."));
           String ckEditorImageFileName = ckEditorFileNameWithoutExt + "_original." + ext;
           String ckEditorFilePath = request.getServletContext().getRealPath("/resources/image/tr") + File.separator + ckEditorImageFileName;
           File ckEditorDest = new File(ckEditorFilePath);
           file.transferTo(ckEditorDest);

           // 원본 이미지 업로드
           String ckEditorImageUrl = imgur.requestUpload(Files.readAllBytes(ckEditorDest.toPath())); // 원본 이미지 업로드 서비스 호출 및 URL 받아오기

       }

       trService.updateByIdx(vo); // 데이터베이스에 저장
       return "redirect:/tr/detail";
   }

	
   @ResponseBody
   @RequestMapping(value = "fileupload.do")
   public String communityImageUpload(@RequestParam("upload") MultipartFile upload) {
      JsonObject jsonObject = new JsonObject();

      try {
         if (upload != null && !upload.isEmpty() && upload.getContentType().toLowerCase().startsWith("image/")) {
            Imgur imgurUploader = new Imgur();
            byte[] bytes = upload.getBytes();

            // 이미지를 imgur에 업로드하고 URL 받아오기
            String imageUrl = imgurUploader.requestUpload(bytes);

            // CKEditor에 이미지 URL 반환
            jsonObject.addProperty("uploaded", 1);
            jsonObject.addProperty("url", imageUrl);
         }
      } catch (Exception e) {
         e.printStackTrace();
         // 업로드 실패 시 오류 반환
         jsonObject.addProperty("uploaded", 0);
         jsonObject.addProperty("error", "Failed to upload image");
      }

      return jsonObject.toString();
   }

	// --------------------------------------------------------

	// 231206
	@PostMapping("/insertComment")
	public @ResponseBody void insertComment(TrComment cvo) {
		trService.insertComment(cvo);
	}

	@GetMapping("/getComment")
	public @ResponseBody List<TrComment> getComment(int tr_idx) {
		List<TrComment> cvo = trService.findAllComment(tr_idx);
		return cvo;
	}

	@PostMapping("/deleteByIdx")
	public @ResponseBody void deleteByIdx(int tr_idx) {
		trService.deleteCommentByTr_idx(tr_idx);
		trService.deleteByIdx(tr_idx);
	}

	@PostMapping("/deleteCommentByIdx")
	public @ResponseBody void deleteCommentByIdx(int tr_comment_idx) {
		trService.deleteCommentByIdx(tr_comment_idx);
	}

	@PostMapping("/insertReplyComment")
	public @ResponseBody void insertReplyComment(TrComment vo) {
		trService.insertReplyComment(vo);
	}

	// 231207
	@PostMapping("/updateComment")
	public @ResponseBody void updateComment(TrComment cvo) {
		trService.updateCommentByIdx(cvo);
	}

}
