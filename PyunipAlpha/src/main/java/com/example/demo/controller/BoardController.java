package com.example.demo.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.FilesDto;
import com.example.demo.dto.RpyDto;
import com.example.demo.dto.SuccessDto;
import com.example.demo.dto.UserDto;
import com.example.demo.service.BoardService;
import com.example.demo.service.FilesService;
import com.example.demo.service.LogService;
import com.example.demo.util.FilesUtils;
import com.example.demo.util.IpUtils;
import com.example.demo.util.SummerUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	//ExecutorService executorService = Executors.newFixedThreadPool(10); 다중 스레드도 고려
	
	@Value("${file.upload-dir}")
	private String uploadPath;
	@Autowired
	private FilesUtils filesUtils;
	@Autowired
	private FilesService filesService;
	@Autowired
	private LogService errorService;
	@Autowired
	private BoardService boardService;

	//----------------------------편입이야기---------------------------------------------	
	/**
	 * 편입이야기
	 */
	@GetMapping("/board/talk_form")
	public void boardTalkForm(HttpSession session, Model model,
							  @RequestParam(value="nSelect",required = false , defaultValue="1")int nSelect) {
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			model.addAttribute("USER_NUM", USER_NUM);
		}
		model.addAttribute("nSelect", nSelect);
	}

	/**
	 * 편입 이야기 게시물 리스트
	 * @param SCH     검색어
	 * @param DV      구분   F 자유주제 / Q 질문하기 / N 전체
	 * @param MINE    Y 내게시물보기 N 전체보기
	 * @param HEART   Y 관심게시물보기 N 전체보기
	 * @param BRD_CTG 
	 * @throws Exception 
	 */
	@GetMapping("/board/talk_list")
    public String boardTalkList(@RequestParam String BRD_CTG, 
					            @RequestParam(required = false) String SCH, 
					            @RequestParam(defaultValue = "N") String DV, 
					            @RequestParam(defaultValue = "1") int nSelect, 
					            @RequestParam(defaultValue = "N") String MINE, 
					            @RequestParam(defaultValue = "N") String HEART, 
					            Model model, HttpSession session, HttpServletRequest request) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			map.put("USER_NUM", USER_NUM);
			
			if(!MINE.equals("N")) {
				map.put("MINE", MINE);
			}
			
			if(!HEART.equals("N")) {
				map.put("HEART", HEART);
			}
		}
		map.put("BRD_CTG", BRD_CTG);
		
		if (!DV.equals("N")) {
			map.put("DV", DV);
		}
		map.put("SCH", SCH);

		try {
			int nMaxVCnt = 10;
			int nMaxRecordCnt = boardService.findBoardByCtg(map).size();

			map.put("nSelectPage", nSelect);
			map.put("nMaxVCnt", nMaxVCnt);
			List<BoardDto> list = boardService.findBoardByCtg(map);
			model.addAttribute("talkList", list);
			model.addAttribute("size", nMaxRecordCnt);
			model.addAttribute("nMaxVCnt", nMaxVCnt);
			model.addAttribute("nMaxRecordCnt", nMaxRecordCnt);
			
		} catch (Exception e) {
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요");
		}

		return "forward:/WEB-INF/views/board/talk_list.jsp";
	}
	
	/**
	 * 편입 이야기 게시물 상세
	 * @param BRD_NUM  게시글_일련번호
	 * @param nSelect  게시물이 있는 목록 번호
	 * @throws Exception 
	 */	
	@GetMapping("/board/talk_detail")
	public void boardTalkDetail(@RequestParam String BRD_NUM,
								@RequestParam(value="nSelect",required = false , defaultValue="1")int nSelect
								, Model model, HttpSession session, HttpServletRequest request) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			map.put("USER_NUM", USER_NUM);
			model.addAttribute("USER_NUM", USER_NUM);
		}
		map.put("BRD_NUM", BRD_NUM);
		
		//게시물 존재 여부 확인
		List<String> list=new ArrayList<>();
		list=boardService.findBoardNum();
		if(!list.contains(BRD_NUM)) {
			model.addAttribute("error", "해당 게시물은 삭제되었습니다.");
			return;
		}
		
		// 조회수 증가
		boardService.updateViewCnt(map);

		try {
			model.addAttribute("t", boardService.findBoardByNum(map));
			model.addAttribute("num", BRD_NUM);
			model.addAttribute("nSelect", nSelect);

		} catch (Exception e) {
			e.printStackTrace();
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요");
		}	
	}
		
	/**
	 * 편입 이야기 게시물 추가
	 */		
	@GetMapping("/user/board/talk_insert")
	public String talkInsert(HttpSession session,HttpServletRequest request, RedirectAttributes re) {
		if (session.getAttribute("m") != null) {
			return "redirect:/board/talk_insert_form";
		}else {
			String uri = request.getHeader("Referer");
			re.addFlashAttribute("referer",uri);
			return "redirect:/main/login_form";			
		}
	}
	
	/**
	 * 편입 이야기 게시물 추가 페이지
	 */		
	@GetMapping("/board/talk_insert_form")
	public void talkInsertForm2(HttpSession session, Model model) {
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			model.addAttribute("USER_NUM", USER_NUM);
		}	
	}
	
	/**
	 * 편입 이야기 게시물 수정 페이지
	 * @throws Exception 
	 */			
	@GetMapping("/board/talk_update_form")
	public void talkUpdateForm(@RequestParam String BRD_NUM,
							   @RequestParam(value="nSelect",required = false , defaultValue="1")int nSelect, 
							   Model model, HttpSession session,HttpServletRequest request) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<>();
			if (session.getAttribute("m") != null) {
				String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
				model.addAttribute("USER_NUM", USER_NUM);
				map.put("USER_NUM", USER_NUM);
			} 
			map.put("BRD_NUM", BRD_NUM);

			BoardDto t = boardService.findBoardByNum(map);
			String content = t.getCONTENT();
			
			//내용에 이미지 파일을 가지고 있는 경우
			if(content != null) {
				if(content.indexOf("img") > 0) {
					String filenames =SummerUtils.extractFilenames(content);
					model.addAttribute("filenames", filenames);
				}
			}
			model.addAttribute("t", t);
			model.addAttribute("num", BRD_NUM);
			model.addAttribute("nSelect", nSelect);
		} catch (Exception e) {
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "에러가 발생했습니다. 관리제에 문의해주세요");
		}
	}	
	
	/**
	 * 편입 이야기 게시물 추가 
	 * @param b
	 * @param filenames summertnote통해 업로드된 파일
	 * @throws Exception 
	 */			
	@PostMapping("/user/talk")
	@ResponseBody
	public String insertTalk(@ModelAttribute BoardDto b,
							 @RequestParam String filenames, 
							 HttpSession session,HttpServletRequest req) throws Exception {
		String msg = "";

		if (session.getAttribute("m") != null) {
			try {
				//수정된 파일 삭제
				String content=b.getCONTENT();
				SummerUtils.deleteFilenames(content, filenames);				
				
				String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM();
				b.setREG_USER_NUM(USER_NUM);
				b.setREG_IP(IpUtils.getRemoteIp(req));
				b.setBRD_CTG("0");
				
				int re = boardService.insertBoard(b);
				if (re != 1) {
					msg = "게시물 등록에 실패했습니다. 관리자에게 문의해주세요";
				}

			} catch (Exception e) {
				String error=e.getMessage();
				errorService.insertErrorLog(req,session,error);
				msg = "에러가 발생했습니다. 관리자에게 문의해주세요";
			}
		} else {
			msg = "로그인이 필요한 서비스입니다.";
		}

		return msg;
	}

	/**
	 * 편입 이야기 게시물 수정
	 * @param b
	 * @param filenames summertnote통해 업로드된 파일
	 * @throws Exception 
	 */			
	@PutMapping("/user/talk")
	@ResponseBody
	public String updateTalk(@ModelAttribute BoardDto b,
			 				 @RequestParam String filenames,
			 				 HttpSession session,HttpServletRequest req) throws Exception {
		String msg = "";
		if (session.getAttribute("m") != null) {
			try {
				//수정자와 작성자가 동일한지 확인
				String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM();
				String REG_USER_NUM = b.getREG_USER_NUM();
				if(!USER_NUM.equals(REG_USER_NUM)) {
					return msg="게시물 수정에 실패했습니다. 정보를 다시 확인해주세요";
				}
				
				//수정된 파일 삭제
				String content=b.getCONTENT();
				SummerUtils.deleteFilenames(content, filenames);
				
				b.setUPT_USER_NUM(USER_NUM);
				b.setUPT_IP(IpUtils.getRemoteIp(req));

				boardService.updateBoard(b);

			} catch (Exception e) {
				String error=e.getMessage();
				errorService.insertErrorLog(req,session,error);
				msg = "에러가 발생했습니다. 관리자에게 문의해주세요";
			}
		} else {
			msg = "로그인이 필요한 서비스입니다.";
		}

		return msg;
	}
	
	
	//----------------------------편입스터디---------------------------------------------	
	/**
	 * 편입스터디
	 */	
	@GetMapping("/board/study_form")
	public void boardStudyForm(HttpSession session, Model model,
							   @RequestParam(value="nSelect",required = false , defaultValue="1")int nSelect) {
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			model.addAttribute("USER_NUM", USER_NUM);
		}
		model.addAttribute("nSelect", nSelect);
	}	
	
	/**
	 * 편입 스터디 게시물 리스트
	 * @param SCH  검색어
	 * @param ING  모집 중인 스터디만 확인 Y: 모집중 / N: 전체
	 * @param MINE  Y 내게시물보기 N 전체보기
	 * @param HEART Y 관심게시물보기 N 전체보기
	 * @param BRD_CTG 
	 * @throws Exception 
	 */
	@GetMapping("/board/study_list")
	public String boardStudyList(@RequestParam String BRD_CTG, 
								 @RequestParam String SCH, 
								 @RequestParam(defaultValue = "1") int nSelect, 
								 @RequestParam(defaultValue = "N")String ING, 
								 @RequestParam(defaultValue = "N")String MINE, 
								 @RequestParam(defaultValue = "N")String HEART,  
								 Model model, HttpSession session, HttpServletRequest request) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		map.put("BRD_CTG", BRD_CTG);
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			map.put("USER_NUM", USER_NUM);
			
			if(!MINE.equals("N")) {
				map.put("MINE", MINE);
			}		
			
			if(!HEART.equals("N")) {
				map.put("HEART", HEART);
			}			
		}

		if (!ING.equals("N") && !ING.equals("") && ING != null) {
			map.put("ING", ING);
		}

		map.put("SCH", SCH);
		
		try {
			List<BoardDto> list = boardService.findStudyByCtg(map);
			int nMaxRecordCnt = list.size();
			int nMaxVCnt = 16;

			map.put("nSelectPage", nSelect);
			map.put("nMaxVCnt", nMaxVCnt);
			list = boardService.findStudyByCtg(map);

			model.addAttribute("list", list);
			model.addAttribute("size", nMaxRecordCnt);
			model.addAttribute("nMaxVCnt", nMaxVCnt);
			model.addAttribute("nMaxRecordCnt", nMaxRecordCnt);
		} catch (Exception e) {
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요");
		}

		return "forward:/WEB-INF/views/board/study_list.jsp";
	}	
	
	/**
	 * 편입 스터디 게시물 상세
	 * @param BRD_NUM  게시글_일련번호
	 * @throws Exception 
	 */	
	@GetMapping("/board/study_detail")
	public void studyDetail(@RequestParam String BRD_NUM,
							@RequestParam(value="nSelect",required = false , defaultValue="1")int nSelect, 
							Model model,HttpSession session, HttpServletRequest request) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			map.put("USER_NUM", USER_NUM);
			model.addAttribute("USER_NUM", USER_NUM);
		}
		map.put("BRD_NUM", BRD_NUM);
		map.put("BRD_CTG", "1");

		// 조회수 증가
		boardService.updateViewCnt(map);

		try {
			model.addAttribute("s", boardService.findStudyByNum(map));
			model.addAttribute("num", BRD_NUM);
			model.addAttribute("nSelect", nSelect);
		} catch (Exception e) {
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요");
		}
	}	

	/**
	 * 편입 스터디 게시물 추가
	 */		
	@GetMapping("/user/board/study_insert")
	public String boardStudyInsert(HttpSession session,HttpServletRequest request, RedirectAttributes re) {
		if (session.getAttribute("m") != null) {
			return "redirect:/board/study_insert_form";
		}else {
			String uri = request.getHeader("Referer");
			re.addFlashAttribute("referer",uri);
			return "redirect:/main/login_form";			
		}
	}
	
	/**
	 * 편입 스터디 게시물 추가 페이지
	 */			
	@GetMapping("/board/study_insert_form")
	public void boardStudyInsertForm(HttpSession session, Model model) {
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			model.addAttribute("USER_NUM", USER_NUM);
		}
	}	
	
	/**
	 * 편입 스터디 게시물 추가 
	 * @param b
	 * @param filenames summertnote통해 업로드된 파일
	 * @throws Exception 
	 */			
	@PostMapping("/user/study")
	@ResponseBody
	public String insertStudy(@ModelAttribute BoardDto b,
							  @RequestParam	String filenames, 
							  HttpSession session,HttpServletRequest req) throws Exception {
		String msg = "";

		if (session.getAttribute("m") != null) {
			try {
				String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM();
				b.setREG_USER_NUM(USER_NUM);

			    b.setBRD_CTG("1");
				b.setREG_IP(IpUtils.getRemoteIp(req));

				// date 형식 맞추기
				b.setAPPLY_ED_DTM(b.getAPPLY_ED_DTM().replace("-", ""));
				b.setED_DTM(b.getED_DTM().replace("-", ""));
				b.setST_DTM(b.getST_DTM().replace("-", ""));

				boardService.insertBoardStudy(b);

			} catch (Exception e) {
				String error=e.getMessage();
				errorService.insertErrorLog(req,session,error);
				msg = "에러가 발생했습니다. 관리자에게 문의해주세요";
			}

		} else {
			msg = "로그인이 필요한 서비스입니다.";
		}

		return msg;
	}	
	

	/**
	 * 편입 스터디 게시물 수정 페이지
	 * @throws Exception 
	 */			
	@GetMapping("/board/study_update_form")
	public void studyUpdateForm(@RequestParam String BRD_NUM,
								@RequestParam(value="nSelect",required = false , defaultValue="1")int nSelect, 
								Model model, HttpSession session, HttpServletRequest request) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<>();	
			if (session.getAttribute("m") != null) {
				String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
				model.addAttribute("USER_NUM", USER_NUM);
				map.put("USER_NUM", USER_NUM);
			} else {
				model.addAttribute("error", "로그인이 필요한 서비스입니다.");
			}
			map.put("BRD_NUM", BRD_NUM);
			map.put("BRD_CTG", "1");

			BoardDto dto = boardService.findStudyByNum(map);
			
			String content = dto.getCONTENT();
			
			//내용에 이미지 파일을 가지고 있는 경우
			if(content.indexOf("img") > 0) {
				String filenames = SummerUtils.extractFilenames(content);
				model.addAttribute("filenames", filenames);
			}

			dto.setAPPLY_ED_DTM(dto.getAPPLY_ED_DTM().replace(".", "-"));
			dto.setED_DTM(dto.getED_DTM().replace(".", "-"));
			dto.setST_DTM(dto.getST_DTM().replace(".", "-"));

			model.addAttribute("s", dto);
			model.addAttribute("nSelect", nSelect);
		} catch (Exception e) {
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "에러가 발생했습니다. 관리제에 문의해주세요");
		}

	}	
	
	/**
	 * 편입 스터디 게시물 수정 
	 * @param b
	 * @param filenames summertnote통해 업로드된 파일
	 * @throws Exception 
	 */			
	@PutMapping("/user/study")
	@ResponseBody
	public String updateStudy(@ModelAttribute BoardDto b,
							  @RequestParam	String filenames, 
							  HttpSession session,HttpServletRequest req) throws Exception {
		String msg = "";

		if (session.getAttribute("m") != null) {
			try {
				//수정자와 작성자가 동일하지 확인
				String REG_USER_NUM=b.getREG_USER_NUM();
				String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM();
				
				if(!REG_USER_NUM.equals(USER_NUM)) {
					return msg="게시물 수정에 실패했습니다. 정보를 다시 확인해주세요";
				}
				
				//수정된 파일 삭제
				String content=b.getCONTENT();
				SummerUtils.deleteFilenames(content, filenames);				
				
				
				b.setUPT_USER_NUM(USER_NUM);
				b.setUPT_IP(IpUtils.getRemoteIp(req));

				// date 형식 맞추기
				b.setAPPLY_ED_DTM(b.getAPPLY_ED_DTM().replace("-", ""));
				b.setED_DTM(b.getED_DTM().replace("-", ""));
				b.setST_DTM(b.getST_DTM().replace("-", ""));

				boardService.updateBoardStudy(b);

			} catch (Exception e) {
				String error=e.getMessage();
				errorService.insertErrorLog(req,session,error);
				msg = "에러가 발생했습니다. 관리자에게 문의해주세요";
			}
		} else {
			msg = "로그인이 필요한 서비스입니다.";
		}
		return msg;
	}
		
	//----------------------------합격인증샷---------------------------------------------	
	
	/**
	 *	합격 인증샷
	 */		
	@GetMapping("/board/proof_shot_form")
	public void boardProofShotForm(HttpSession session,Model model,
								   @RequestParam(value="nSelect",required = false , defaultValue="1")int nSelect) {
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			model.addAttribute("USER_NUM", USER_NUM);
		}		
		model.addAttribute("nSelect", nSelect);
	}	
	
	/**
	 * 합격 인증샷 리스트
	 * @param BRD_CTG 카테고리 5 
	 * @param MINE    Y 내게시물보기 N 전체보기
	 * @param HEART   Y 관심게시물보기 N 전체보기
	 * @param SCH     검색어
	 * @throws Exception 
	 */			
	@GetMapping("/board/proof_shot_list")
	public String boardProofShotList(@RequestParam String BRD_CTG, 
									 @RequestParam(defaultValue = "1")int nSelect, 
									 @RequestParam String SCH, 
									 @RequestParam(defaultValue = "N") String MINE, 
									 @RequestParam(defaultValue = "N") String HEART, 
									 Model model, HttpSession session, HttpServletRequest request) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		map.put("BRD_CTG", BRD_CTG);

		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			map.put("USER_NUM", USER_NUM);
			
			if(!MINE.equals("N")) {
				map.put("MINE", MINE);
			}
			
			if(!HEART.equals("N")) {
				map.put("HEART", HEART);
			}
		}
		map.put("SCH", SCH);

		try {
			List<SuccessDto> list = boardService.findProof(map);
			int nMaxRecordCnt = list.size();
			int nMaxVCnt = 16;
			
			map.put("nSelectPage", nSelect);
			map.put("nMaxVCnt", nMaxVCnt);
			list = boardService.findProof(map);
			
			model.addAttribute("list", list);
			model.addAttribute("size", nMaxRecordCnt);
			model.addAttribute("nMaxVCnt", nMaxVCnt);
			model.addAttribute("nMaxRecordCnt", nMaxRecordCnt);
			model.addAttribute("nSelectPage", nSelect);

		} catch (Exception e) {
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}

		return "forward:/WEB-INF/views/board/proof_shot_list.jsp";
	}	
	
	/**
	 * 합격 인증샷 추가 페이지
	 */		
	@GetMapping("/board/proof_shot_insert_form")
	public void proofShotInsert2(HttpSession session, Model model) {
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			model.addAttribute("USER_NUM", USER_NUM);
		}	
	}		
	
	/**
	 * 합격 인증샷 추가
	 */		
	@GetMapping("/board/proof_shot_insert")
	public String proofShotInsert(HttpSession session,HttpServletRequest request, RedirectAttributes re) {
		if (session.getAttribute("m") != null) {
			return "redirect:/board/proof_shot_insert_form";
		}else {
			String uri = request.getHeader("Referer");
			re.addFlashAttribute("referer",uri);
			return "redirect:/main/login_form";			
		}
	}		
	
	/**
	 * 합격 인증샷 상세
	 * @param BRD_NUM 게시물_일련번호
	 * @throws Exception 
	 */		
	@GetMapping("/board/proof_shot_detail")
	public void boardProofShotDetail(@RequestParam String BRD_NUM, 
									 @RequestParam(value="nSelect",required = false , defaultValue="1")int nSelect, 
									 Model model, HttpSession session, HttpServletRequest request) throws Exception {
		HashMap<String, Object> map = new HashMap<>();

		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			map.put("USER_NUM", USER_NUM);
			model.addAttribute("USER_NUM", USER_NUM);
		}
		map.put("BRD_NUM", BRD_NUM);

		// 조회수 증가
		boardService.updateViewCnt(map);

		try {
			SuccessDto dto = boardService.findProofByNum(map);
			model.addAttribute("t", dto);
			model.addAttribute("num", BRD_NUM);
			model.addAttribute("nSelect", nSelect);
		} catch (Exception e) {
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}
	}	
	
	/**
	 * 합격 인증샷 게시물 추가 
	 * @param s
	 */			
	@PostMapping("/user/proof_shot")
	@ResponseBody
	public CompletableFuture<Map<String,Object>> insertProofShot(@RequestParam(value="images",required = false) MultipartFile file ,
																 @ModelAttribute BoardDto s, 
																 HttpSession session,HttpServletRequest req) {
		return CompletableFuture.supplyAsync(()->{
			Map<String, Object> response = new HashMap<>();
			
			if (session.getAttribute("m") != null) {
				try {
					String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM();
					//board 
					s.setREG_USER_NUM(USER_NUM);
					s.setREG_IP(IpUtils.getRemoteIp(req));
					s.setBRD_CTG("5");
					
					//files
					if(file!=null) {
						FilesDto files_dto = filesUtils.getAttachByMultipart(file,"2");
						files_dto.setREG_USER_IP(IpUtils.getRemoteIp(req));
						files_dto.setREG_USER_NUM(USER_NUM);
						
						boardService.insertProofShot(s, files_dto);
					}else {
						boardService.insertBoard(s);
					}
					
				} catch (Exception e) {
					String error=e.getMessage();
					try {
						errorService.insertErrorLog(req,session,error);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					response.put("success", false);
					response.put("message", "게시물 등록에 실패했습니다. 관리자에게 문의해주세요.");
				}
				response.put("success", true);
				
			} else {
				response.put("success", false);
				response.put("message", "로그인이 필요한 서비스입니다.");
			}
			return response;
		});
	}		
	
	/**
	 * 합격 인증샷 수정 페이지
	 * @throws Exception 
	 */		
	@GetMapping("/board/proof_shot_update_form")
	public void proofShotUpdateForm(@RequestParam(value="nSelect",required = false , defaultValue="1")int nSelect, 
									HttpSession session, Model model,String BRD_NUM) throws Exception {
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			model.addAttribute("USER_NUM", USER_NUM);
			
			HashMap<String, Object> map=new HashMap<>();
			map.put("BRD_NUM", BRD_NUM);
			model.addAttribute("s", boardService.findProofByNum(map));
			model.addAttribute("nSelect", nSelect);
		}	
	}		
	
	/**
	 * 합격 인증샷 게시물 수정 
	 * @param s
	 */			
	@PutMapping("/user/proof_shot")
	@ResponseBody
	public CompletableFuture<Map<String,Object>> updateProofShot(@RequestParam(value="images",required = false) MultipartFile file ,
																 @ModelAttribute BoardDto s, 
																 @RequestParam String filename, 
																 HttpSession session,HttpServletRequest req) {
		return CompletableFuture.supplyAsync(()->{
			
			Map<String, Object> response = new HashMap<>();
			if (session.getAttribute("m") != null) {
				String REG_USER_NUM = s.getREG_USER_NUM();
				String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM();
				
				if(!REG_USER_NUM.equals(USER_NUM)) {
					response.put("success", false);
					response.put("message", "게시물 수정에 실패했습니다. 정보를 다시 확인해주세요");
					return response;
				}
				
				String BRD_NUM = s.getBRD_NUM();
				s.setUPT_USER_NUM(USER_NUM);
				s.setUPT_IP(IpUtils.getRemoteIp(req));
				
				try {
					if(file!=null) {
						//기존에 있던 파일 삭제
						int re=filesUtils.deleteAttachByBoard(filename, "2");
						if(re==1) {
							response.put("success", false);
							response.put("message", "기존 파일 삭제시 에러가 발생했습니다. 관리자에게 문의해주세요");
							return response;
						}
						
						HashMap<String, Object> map=new HashMap<>();
						map.put("FILE_NAME", filename);
						map.put("BRD_NUM", BRD_NUM);
						
						//수정된 파일 삽입
						FilesDto files_dto = filesUtils.getAttachByMultipart(file,"2");
						files_dto.setBRD_NUM(BRD_NUM);
						files_dto.setREG_USER_IP(IpUtils.getRemoteIp(req));
						files_dto.setREG_USER_NUM(USER_NUM);
						
						boardService.updateProofShoot(s, files_dto,map);
						
					}else {
						boardService.updateBoard(s);
					}
				} catch (Exception e) {
					String error=e.getMessage();
					try {
						errorService.insertErrorLog(req,session,error);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					response.put("success", false);
					response.put("message", "게시물 수정 에러가 발생했습니다. 관리자에게 문의해주세요.");
				}
				
			} else {
				response.put("success", false);
				response.put("message", "로그인이 필요한 서비스입니다.");
			}
			
			return response;
		});
	}		
	//----------------------------합격 수기---------------------------------------------	

	/**
	 * 합격 수기 추가
	 */		
	@GetMapping("/board/success_story_insert")
	public String SuccessStoryInsert(HttpSession session,HttpServletRequest request, RedirectAttributes re) {
		if (session.getAttribute("m") != null) {
			return "redirect:/board/success_story_insert_form";
		}else {
			String uri = request.getHeader("Referer");
			re.addFlashAttribute("referer",uri);
			return "redirect:/main/login_form";			
		}
	}	
	
	/**
	 * 합격 수기 추가 페이지
	 */		
	@GetMapping("/board/success_story_insert_form")
	public void SuccessStoryInsert2(HttpSession session, Model model) {
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			model.addAttribute("USER_NUM", USER_NUM);
		}	
	}	
	
	/**
	 * 합격 수기 수정 페이지
	 * @throws Exception 
	 */			
	@GetMapping("/board/success_story_update_form")
	public void successStoryUpdateForm(@RequestParam String BRD_NUM,
									   @RequestParam(value="nSelect",required = false , defaultValue="1")int nSelect, 
									   Model model, HttpSession session, HttpServletRequest request) throws Exception {
		try {
			HashMap<String, Object> map = new HashMap<>();
			if (session.getAttribute("m") != null) {
				String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
				model.addAttribute("USER_NUM", USER_NUM);
				map.put("USER_NUM", USER_NUM);
			} else {
				model.addAttribute("error", "로그인이 필요한 서비스입니다.");
			}
			map.put("BRD_NUM", BRD_NUM);
			map.put("BRD_CTG", "4");
			
			BoardDto dto = boardService.findSuccessByNum(map);
			String content = dto.getCONTENT();
			
			//내용에 이미지 파일을 가지고 있는 경우
			if(content.indexOf("img") > 0) {
				String filenames = SummerUtils.extractFilenames(content);
				model.addAttribute("filenames", filenames);
			}
			
			model.addAttribute("s", dto);
			model.addAttribute("nSelect", nSelect);
		} catch (Exception e) {
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "에러가 발생했습니다. 관리제에 문의해주세요");
		}
	}
	
	/**
	 * 합격 수기 게시물 추가 
	 * @param s
	 * @param filenames summertnote통해 업로드된 파일
	 * @throws Exception 
	 */			
	@PostMapping("/user/success_story")
	@ResponseBody
	public String insertsuccessStory(@ModelAttribute BoardDto s,
									 @RequestParam String filenames, 
									 HttpSession session,HttpServletRequest req) throws Exception {
		String msg = "";
		
		if (session.getAttribute("m") != null) {
			try {
				String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM();
				s.setREG_USER_NUM(USER_NUM);
				s.setREG_IP(IpUtils.getRemoteIp(req));
				s.setBRD_CTG("4");
				
				boardService.insertSuc(s);
				
			} catch (Exception e) {
				String error=e.getMessage();
				errorService.insertErrorLog(req,session,error);
				msg = "에러가 발생했습니다. 관리자에게 문의해주세요";
			}
			
		} else {
			msg = "로그인이 필요한 서비스입니다.";
		}
		
		return msg;
	}	
	
	/**
	 * 합격 수기 수정 
	 * @param b
	 * @param filenames summertnote통해 업로드된 파일
	 * @throws Exception 
	 */			
	@PutMapping("/user/success_story")
	@ResponseBody
	public String updateSuccessStory(@ModelAttribute BoardDto b,
									 @RequestParam String filenames, 
									 HttpSession session,HttpServletRequest req) throws Exception {
		String msg = "";
		
		if (session.getAttribute("m") != null) {
			try {
				String REG_USER_NUM = b.getREG_USER_NUM();
				String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM();
				
				if(!REG_USER_NUM.equals(USER_NUM)) {
					return msg="게시물 수정에 실패했습니다. 정보를 다시 확인해주세요";
				}				
				
				//수정된 파일 삭제
				String content=b.getCONTENT();
				SummerUtils.deleteFilenames(content, filenames);				
				
				b.setUPT_USER_NUM(USER_NUM);
				b.setUPT_IP(IpUtils.getRemoteIp(req));
				
				boardService.updateSuc(b);
				
			} catch (Exception e) {
				String error=e.getMessage();
				errorService.insertErrorLog(req,session,error);
				msg = "에러가 발생했습니다. 관리자에게 문의해주세요";
			}
		} else {
			msg = "로그인이 필요한 서비스입니다.";
		}
		return msg;
	}	
	
	/**
	 * 합격 수기 리스트
	 * @param BRD_CTG 카테고리 4 
	 * @param SCH     검색어
	 * @param MINE    Y 내게시물보기 N 전체보기
	 * @param HEART   Y 관심게시물보기 N 전체보기
	 * @throws Exception 
	 */			
	@GetMapping("/board/success_story_list")
	public String boardSuccessStoryList(@RequestParam String BRD_CTG, 
										@RequestParam(defaultValue = "1") int nSelect, 
										@RequestParam String SCH, 
										@RequestParam(defaultValue = "N") String MINE, 
										@RequestParam(defaultValue = "N") String HEART, 
										Model model, HttpSession session, HttpServletRequest request) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		map.put("BRD_CTG", BRD_CTG);

		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			map.put("USER_NUM", USER_NUM);
			
			if(!MINE.equals("N")) {
				map.put("MINE", MINE);
			}
			
			if(!HEART.equals("N")) {
				map.put("HEART", HEART);
			}
		}
		map.put("SCH", SCH);

		try {
			List<BoardDto> list = boardService.findSuccess(map);
			int nMaxRecordCnt = list.size();
			int nMaxVCnt = 12;
			
			map.put("nSelectPage", nSelect);
			map.put("nMaxVCnt", nMaxVCnt);
			
			list = boardService.findSuccess(map);

			model.addAttribute("list", list);
			model.addAttribute("size", nMaxRecordCnt);
			model.addAttribute("nMaxVCnt", nMaxVCnt);
			model.addAttribute("nMaxRecordCnt", nMaxRecordCnt);
			model.addAttribute("nSelectPage", nSelect);

		} catch (Exception e) {
			e.printStackTrace();
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}

		return "forward:/WEB-INF/views/board/success_story_list.jsp";
	}
	
	/**
	 * 합격 수기 상세
	 * @param BRD_NUM 게시물_일련번호
	 * @throws Exception 
	 */		
	@GetMapping("/board/success_story_detail")
	public void boardSuccessStoryDetail(@RequestParam String BRD_NUM, 
										@RequestParam(value="nSelect",required = false , defaultValue="1")int nSelect,
										Model model, HttpSession session, HttpServletRequest request) throws Exception {
		HashMap<String, Object> map = new HashMap<>();

		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			map.put("USER_NUM", USER_NUM);
			model.addAttribute("USER_NUM", USER_NUM);
		}
		map.put("BRD_NUM", BRD_NUM);

		// 조회수 증가
		boardService.updateViewCnt(map);

		try {
			BoardDto dto = boardService.findSuccessByNum(map);
			model.addAttribute("t", dto);
			model.addAttribute("num", BRD_NUM);
			model.addAttribute("nSelect", nSelect);
		} catch (Exception e) {
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}
	}


	/**
	 *	합격 수기
	 */		
	@GetMapping("/board/success_story_form")
	public void boardSuccessStoryForm(HttpSession session,Model model,
									  @RequestParam(value="nSelect",required = false , defaultValue="1")int nSelect) {
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			model.addAttribute("USER_NUM", USER_NUM);
		}
		model.addAttribute("nSelect", nSelect);
	}	
	
	//----------------------------편입뉴스---------------------------------------------	
	/**
	 * 편입 뉴스 리스트
	 * @param BRD_CTG 카테고리
	 * @param SCH     검색어
	 * @throws Exception 
	 */		
	@GetMapping("/board/univ_news_list")
	public String boardUnivNewsList(@RequestParam String BRD_CTG, 
									@RequestParam(defaultValue = "1")int nSelect, 
									@RequestParam String SCH, 
									Model model, HttpSession session, HttpServletRequest request) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		map.put("BRD_CTG", BRD_CTG);

		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			map.put("USER_NUM", USER_NUM);
		}

		map.put("SCH", SCH);

		try {
			int nMaxVCnt = 10;
			int nMaxRecordCnt = boardService.findBoardSize(map);

			map.put("nSelectPage", nSelect);
			map.put("nMaxVCnt", nMaxVCnt);
			List<BoardDto> list = boardService.findBoardByCtg(map);

			model.addAttribute("list", list);
			model.addAttribute("size", nMaxRecordCnt);
			model.addAttribute("nMaxVCnt", nMaxVCnt);
			model.addAttribute("nMaxRecordCnt", nMaxRecordCnt);
			model.addAttribute("nSelectPage", nSelect);

		} catch (Exception e) {
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}

		return "forward:/WEB-INF/views/board/univ_news_list.jsp";
	}
	
	/**
	 * 편입 뉴스 상세
	 * @param BRD_NUM 게시물_일련번호
	 * @throws Exception 
	 */			
	@GetMapping("/board/univ_news_detail")
	public void boardUnivNewsDetail(@RequestParam String BRD_NUM,
									@RequestParam(value="nSelect",required = false , defaultValue="1")int nSelect, 
									Model model, HttpSession session, HttpServletRequest request) throws Exception {
		HashMap<String, Object> map = new HashMap<>();

		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			map.put("USER_NUM", USER_NUM);
			model.addAttribute("USER_NUM", USER_NUM);
		}

		map.put("BRD_NUM", BRD_NUM);

		// 조회수 증가
		boardService.updateViewCnt(map);

		try {
			BoardDto dto = boardService.findBoardByNum(map);

			if (dto.getFILE_YN().equals("Y")) {
				model.addAttribute("files", filesService.findFilesByNum(map));
			}

			model.addAttribute("b", dto);
			model.addAttribute("num", BRD_NUM);
			model.addAttribute("nSelect", nSelect);
		} catch (Exception e) {
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}

	}

	/**
	 * 편입 뉴스
	 */		
	@GetMapping("/board/univ_news_form")
	public void boardUnivNewsForm(@RequestParam(value="nSelect",required = false , defaultValue="1")int nSelect ,Model model) {
		model.addAttribute("nSelect", nSelect);
		
	}
	
	//----------------------------과년도 모집요강---------------------------------------------	
	/**
	 *과년도 모집 요강
	 */		
	@GetMapping("/board/apply_form")
	public void boardApplyForm(@RequestParam(value="nSelect",required = false , defaultValue="1")int nSelect ,Model model) {
		model.addAttribute("nSelect", nSelect);
	}

	/**
	 * 과년도 모집 요강 리스트
	 * @param BRD_CTG 카테고리
	 * @param SCH     검색어
	 * @throws Exception 
	 */		
	@GetMapping("/board/apply_list")
	public String boardApplyList(@RequestParam String BRD_CTG, 
								 @RequestParam(defaultValue = "1") int nSelect, 
								 @RequestParam String SCH, 
								 Model model, HttpSession session, HttpServletRequest request) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		map.put("BRD_CTG", BRD_CTG);

		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			map.put("USER_NUM", USER_NUM);
		}
		map.put("SCH", SCH);

		try {
			int nMaxVCnt = 10;
			int nMaxRecordCnt = boardService.findBoardSize(map);

			map.put("nSelectPage", nSelect);
			map.put("nMaxVCnt", nMaxVCnt);
			List<BoardDto> list = boardService.findBoardByCtg(map);

			model.addAttribute("list", list);
			model.addAttribute("size", nMaxRecordCnt);
			model.addAttribute("nMaxVCnt", nMaxVCnt);
			model.addAttribute("nMaxRecordCnt", nMaxRecordCnt);
			model.addAttribute("nSelectPage", nSelect);

		} catch (Exception e) {
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}

		return "forward:/WEB-INF/views/board/apply_list.jsp";
	}	

	/**
	 * 과년도 모집요강 상세
	 * @param BRD_NUM 게시물_일련번호
	 * @throws Exception 
	 */		
	@GetMapping("/board/apply_detail")
	public void boardApplyDetail(@RequestParam String BRD_NUM,
								 @RequestParam(value="nSelect",required = false , defaultValue="1")int nSelect, 
								 Model model, HttpSession session, HttpServletRequest request) throws Exception {
		HashMap<String, Object> map = new HashMap<>();

		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			map.put("USER_NUM", USER_NUM);
			model.addAttribute("USER_NUM", USER_NUM);
		}

		map.put("BRD_NUM", BRD_NUM);

		// 조회수 증가
		boardService.updateViewCnt(map);

		try {
			BoardDto dto = boardService.findBoardByNum(map);

			if (dto.getFILE_YN().equals("Y")) {
				model.addAttribute("files", filesService.findFilesByNum(map));
			}

			model.addAttribute("b", dto);
			model.addAttribute("num", BRD_NUM);
			model.addAttribute("nSelect", nSelect);

		} catch (Exception e) {
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}
	}
		
	//----------------------------댓글---------------------------------------------	
	/**
	 * 댓글
	 * @param BRD_NUM  게시글_일련번호
	 * @throws Exception 
	 */		
	@GetMapping("/board/rpy_form")
	public String boardRpyForm(@RequestParam String BRD_NUM, Model model, HttpSession session, HttpServletRequest request) throws Exception {
		try {
			List<RpyDto> list = boardService.findRpyByNum(BRD_NUM);
			int size = list.size();
			if (session.getAttribute("m") != null) {
				String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
				model.addAttribute("USER_NUM", USER_NUM);
			}
			model.addAttribute("list", list);
			model.addAttribute("size", size);
		} catch (Exception e) {
			String error=e.getMessage();
			errorService.insertErrorLog(request,session,error);
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요");
		}
		return "forward:/WEB-INF/views/board/rpy_form.jsp";
	}

	/**
	 * 댓글추가
	 * @param BRD_NUM  게시글_일련번호
	 * @param content  내용
	 */		
	@PostMapping("/user/rpy")
	@ResponseBody
	public CompletableFuture<Map<String, Object>> insertRpy(@RequestParam String content, 
															@RequestParam String BRD_NUM, 
															HttpSession session,HttpServletRequest req) {
		// CompletableFuture를 반환하여 비동기 작업을 처리
		return CompletableFuture.supplyAsync(()->{
			
			Map<String, Object> response = new HashMap<>();
			
			if (session.getAttribute("m") != null) {
				try {
					HashMap<String, Object> map = new HashMap<>();
					map.put("CONTENT", content);
					map.put("BRD_NUM", BRD_NUM);
					map.put("REG_IP", IpUtils.getRemoteIp(req));
	
					String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
					map.put("REG_USER_NUM", USER_NUM);
					
					int re = boardService.insertRpy(map);
					if (re != 1) {
		                response.put("success", false);
		                response.put("message", "댓글 추가에 실패했습니다. 관리자에게 문의해주세요.");
					}else {
						response.put("success", true);
					}
				} catch (Exception e) {
					e.printStackTrace();
					String error=e.getMessage();
					try {
						errorService.insertErrorLog(req,session,error);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
		            response.put("success", false);
		            response.put("message", "댓글 추가에 실패했습니다. 관리자에게 문의해주세요.");
				}
			} else {
		        response.put("success", false);
		        response.put("message", "로그인이 필요한 서비스 입니다.");
			}
			
			return response;
		});
	}

	
	/**
	 * 대댓글추가
	 * @param BRD_NUM  게시글_일련번호
	 * @param RPY_NUM  댓글_일련번호
	 * @param content  내용
	 */		
	@PostMapping("/user/reRpy")
	@ResponseBody
	public CompletableFuture<Map<String, Object>> insertReRpy(@RequestParam String content, 
															  @RequestParam String BRD_NUM, 
															  @RequestParam int RPY_NUM, 
															  HttpSession session,HttpServletRequest req) {
		return CompletableFuture.supplyAsync(()->{
			
			Map<String, Object> response = new HashMap<>();
			if (session.getAttribute("m") != null) {
				try {
					HashMap<String, Object> map = new HashMap<>();
					map.put("CONTENT", content);
					map.put("BRD_NUM", BRD_NUM);
					map.put("RPY_NUM", RPY_NUM);
					map.put("REG_IP", IpUtils.getRemoteIp(req));
					String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
					map.put("REG_USER_NUM", USER_NUM);
					
					int re = boardService.insertReRpy(map);
					if (re != 1) {
						response.put("success", false);
						response.put("message", "댓글 추가에 실패했습니다. 관리자에게 문의해주세요.");
					}else {
						response.put("success", true);
					}
				} catch (Exception e) {
					String error=e.getMessage();
					try {
						errorService.insertErrorLog(req,session,error);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					response.put("success", false);
					response.put("message", "댓글 추가에 실패했습니다. 관리자에게 문의해주세요.");
				}
			} else {
				response.put("success", false);
				response.put("message", "로그인이 필요한 서비스입니다.");
			}
	
			return response;
		});
	}

	/**
	 * 댓글 삭제
	 * @param RPY_NUM  댓글_일련번호
	 * @throws Exception 
	 */		
	@DeleteMapping("/user/rpy")
	@ResponseBody
	public String deleteRpy(@RequestParam int RPY_NUM, HttpSession session, HttpServletRequest request) throws Exception {
		String msg = "";
		if (session.getAttribute("m") != null) {
			try {
				HashMap<String, Object> map = new HashMap<>();
				map.put("RPY_NUM", RPY_NUM);
				boardService.deleteRpy(map);
			} catch (Exception e) {
				String error=e.getMessage();
				errorService.insertErrorLog(request,session,error);
				msg = "댓글 삭제에 실패했습니다. 관리자에게 문의해주세요";
			}
		} else {
			msg = "로그인이 필요한 서비스입니다.";
		}
		return msg;
	}

	/**
	 * 대댓글 삭제
	 * @param RPY_NUM     댓글_일련번호
	 * @param RE_RPY_NUM  대댓글_일련번호
	 * @throws Exception 
	 */		
	@DeleteMapping("/user/reRpy")
	@ResponseBody
	public String deleteReRpy(@RequestParam int RPY_NUM,
							  @RequestParam int RE_RPY_NUM, 
							  HttpSession session, HttpServletRequest request) throws Exception {
		String msg = "";
		if (session.getAttribute("m") != null) {
			try {
				HashMap<String, Object> map = new HashMap<>();
				map.put("RPY_NUM", RPY_NUM);
				map.put("RE_RPY_NUM", RE_RPY_NUM);

				boardService.deleteRpy(map);
			} catch (Exception e) {
				String error=e.getMessage();
				errorService.insertErrorLog(request,session,error);
				msg = "댓글 삭제에 실패했습니다. 관리자에게 문의해주세요";
			}
		} else {
			msg = "로그인이 필요한 서비스입니다.";
		}
		return msg;
	}

	//----------------------------좋아요---------------------------------------------
	/**
	 * 좋아요 추가
	 * @param BRD_NUM  게시물_일련번호
	 */		
	@PostMapping("/user/boardLike")
	@ResponseBody
	public String insertBoardLike(@RequestParam String BRD_NUM, HttpSession session, HttpServletRequest request) {
		String msg = "";
		if (session.getAttribute("m") == null) {
			return msg = "로그인이 필요한 서비스입니다.";
		}

		HashMap<String, Object> map = new HashMap<>();
		String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
		map.put("USER_NUM", USER_NUM);
		map.put("BRD_NUM", BRD_NUM);
		
		executorService.submit(()->{
			try {
				boardService.insertBoardLike(map);
			} catch (Exception e) {
				 String error = e.getMessage();
	             try {
					errorService.insertErrorLog(request, session, error);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		return msg;
	}

	/**
	 * 좋아요 삭제
	 * @param BRD_NUM  게시물_일련번호
	 */		
	@DeleteMapping("/user/boardLike")
	@ResponseBody
	public String deleteBoardLike(@RequestParam String BRD_NUM, HttpSession session, HttpServletRequest request) {
		String msg = "";
		if (session.getAttribute("m") == null) {
			return msg = "로그인이 필요한 서비스입니다.";
		}
		
		HashMap<String, Object> map = new HashMap<>();
		String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
		map.put("USER_NUM", USER_NUM);
		map.put("BRD_NUM", BRD_NUM);
		
		executorService.submit(()->{
			try {
				boardService.deleteBoardLike(map);
			} catch (Exception e) {
				String error=e.getMessage();
				try {
					errorService.insertErrorLog(request,session,error);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		return msg;
	}

	//----------------------------공통사용---------------------------------------------
	/**
	 * 게시물 삭제 (0 편입이야기 / 1 편입스터디 / 2 모집요강 / 3 편입뉴스 / 4  합격 수기)
	 * @param BRD_NUM 게시글_일련번호
	 * @param BRD_CTG 카테고리
	 * @throws Exception 
	 */			
	@DeleteMapping("/user/board")
	@ResponseBody
	public String deleteBoard(@RequestParam String BRD_NUM, 
							  @RequestParam String BRD_CTG, 
							  HttpSession session, HttpServletRequest request) throws Exception {
		String msg = "";
		String REG_USER_NUM=boardService.findRegUserNumByBrdNum(BRD_NUM); //작성자
		
		if (session.getAttribute("m") != null) {
			//작성자와 수정자가 동일한지 확인
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM();
			if(!REG_USER_NUM.equals(USER_NUM)) {
				//관리자는 삭제 가능
				if(!((UserDto) session.getAttribute("m")).getROLE().equals("ADMIN")) {
					return msg="게시물 삭제를 실패했습니다. 정보를 다시 확인해주세요";
				}
			}
			
			try {
				HashMap<String, Object> map = new HashMap<>();
				map.put("BRD_NUM", BRD_NUM);
				map.put("BRD_CTG", BRD_CTG);

				if (BRD_CTG.equals("1")) { 
					String content=boardService.findStudyByNum(map).getCONTENT();
					if(content != null) {
						String filenames=SummerUtils.extractFilenames(content);
						if(filenames != null) {
							SummerUtils.deleteFilenames2(filenames);
						}
					}				
					
					//boardService.deleteStudy(map);
					boardService.deleteBoard(map);
				}else if(BRD_CTG.equals("0") || BRD_CTG.equals("4")){
					String content=boardService.findBoardByNum(map).getCONTENT();
					
					if(content != null) {
						String filenames=SummerUtils.extractFilenames(content);
						if(filenames != null) {
							SummerUtils.deleteFilenames2(filenames);
						}
					}
					
					boardService.deleteBoard(map);
				}else if(BRD_CTG.equals("2") || BRD_CTG.equals("3")){
					//summernote 파일 삭제
					String content=boardService.findBoardByNum(map).getCONTENT();
					if(content != null) {
						String filenames=SummerUtils.extractFilenames(content);
						if(filenames != null) {
							SummerUtils.deleteFilenames2(filenames);
						}
					}
					
					//올린 첨부파일 삭제
					List<FilesDto> filesdto=filesService.findFilesByNum(map);
					for(FilesDto dto : filesdto) {
						int re = filesUtils.deleteAttachByBoard(dto.getFILE_NAME(),"1");
						if(re == 1) {
							msg="첨부파일 삭제에 실패했습니다. 관리자에게 문의해주세요";
							return msg;
						}
					}
					boardService.deleteBoard(map);
				}else{
					boardService.deleteBoard(map);
				}
			} catch (Exception e) {
				String error=e.getMessage();
				errorService.insertErrorLog(request,session,error);
				msg = "에러가 발생했습니다. 관리자에게 문의해주세요";
			}
		} else {
			msg = "로그인이 필요한 서비스입니다.";
		}
		return msg;
	}

	/**
	 * 파일 다운로드
	 * @param FILE_NUM 파일_일련번호
	 */		
	@RequestMapping(value = "/filedownload")
	public void DownloadFile(@RequestParam String FILE_NUM, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		map.put("FILE_NUM", FILE_NUM);
		FilesDto dto = filesService.findFilesByNum(map).get(0);

		/* 다운로드횟수 */
		filesService.download(map);

		String originalName = dto.getORIGIN_NAME();
		String fileName = dto.getFILE_NAME();

		File downloadFile = new File(uploadPath + fileName);
		byte fileByte[] = FileUtils.readFileToByteArray(downloadFile);

		response.setContentType("application/octet-stream");
		response.setContentLength(fileByte.length);

		response.setHeader("Content-Disposition", "attachment; fileName=" + URLEncoder.encode(originalName, "UTF-8"));
		response.setHeader("Content-Transfer-Encoding", "binary");

		response.getOutputStream().write(fileByte);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	
	/**
	 * 파일 다운로드
	 * @param NUM=1 서비스이용약관 , NUM=2 개인정보처리방침
	 */		
	@RequestMapping(value = "/filedownload2")
	public void DownloadFile2(@RequestParam int NUM, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName="";
		String path="/vosej2241/tomcat/webapps/ROOT/WEB-INF/classes/static/agreement";
		if(NUM == 1) {
			fileName="서비스이용약관.pdf";
		}else {
			fileName="개인정보처리방침.pdf";
		}
		File downloadFile = new File(path + fileName);
		byte fileByte[] = FileUtils.readFileToByteArray(downloadFile);
		
		response.setContentType("application/octet-stream");
		response.setContentLength(fileByte.length);
		
		response.setHeader("Content-Disposition", "attachment; fileName=" + URLEncoder.encode(fileName, "UTF-8"));
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		response.getOutputStream().write(fileByte);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

}
