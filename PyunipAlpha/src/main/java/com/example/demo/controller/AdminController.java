package com.example.demo.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.AcssLogDto;
import com.example.demo.dto.BoardDto;
import com.example.demo.dto.ErrorLogDto;
import com.example.demo.dto.FilesDto;
import com.example.demo.dto.RpyDto;
import com.example.demo.dto.SltDetailInfoDto;
import com.example.demo.dto.SuccessDto;
import com.example.demo.dto.TransInfoDto;
import com.example.demo.dto.UnivInfoDto;
import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import com.example.demo.service.BoardService;
import com.example.demo.service.FilesService;
import com.example.demo.service.LogService;
import com.example.demo.service.UnivCacheService;
import com.example.demo.service.UnivService;
import com.example.demo.util.DateUtils;
import com.example.demo.util.FilesUtils;
import com.example.demo.util.IpUtils;
import com.example.demo.util.SummerUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	@Value("${file.upload-dir}")
	private String uploadPath;
	@Autowired
	private FilesUtils fileService;
	@Autowired
	private DateUtils dateService;	
	@Autowired
	private FilesService filesService;
	@Autowired
	private UnivService univService;
	@Autowired
	private UserService userService;
	@Autowired
	private LogService logService;
    @Autowired
    private UnivCacheService univCacheService;
    @Autowired
    private BoardService boardService;
	
	//----------------------------과년도 모집요강---------------------------------------------
	
	/**
	 * 과년도 모집 요강
	 */		
	@GetMapping("/admin/board/ad_apply")
	public void adApply(HttpSession session,Model model) {
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			model.addAttribute("USER_NUM", USER_NUM);
		}		
	}
	
	/**
	 * 과년도 모집 요강 리스트
	 * @param BRD_CTG 카테고리
	 * @param SCH     검색어
	 */		
	@GetMapping("/admin/board/apply_list")
	public String adApplyList(@RequestParam String BRD_CTG, 
							  @RequestParam(defaultValue = "1") int nSelect, 
							  @RequestParam String SCH, 
							  Model model, HttpSession session) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("BRD_CTG", BRD_CTG);

		if (session.getAttribute("m") != null) {
			if(((UserDto) session.getAttribute("m")).getROLE().equals("ROLE_ADMIN")) {
				model.addAttribute("error", "권한이 없습니다.");
				return "forward:/WEB-INF/views/admin/board/apply_list.jsp";		
			}else {
				String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
				map.put("USER_NUM", USER_NUM);
			}
		}else {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
			return "forward:/WEB-INF/views/admin/board/apply_list.jsp";	
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
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}

		return "forward:/WEB-INF/views/admin/board/apply_list.jsp";		
	}

	/**
	 * 과년도 모집 요강 추가
	 */		
	@GetMapping("/admin/board/apply_add")
	public String adApplyAdd(HttpSession session,HttpServletRequest request, RedirectAttributes re) {
		if (session.getAttribute("m") != null) {
			return "redirect:/admin/board/ad_apply_add";
		}else {
			String uri = request.getHeader("Referer");
			re.addFlashAttribute("referer",uri);
			return "redirect:/main/login_form";			
		}		
	}

	@GetMapping("/admin/board/ad_apply_add")
	public void applyAdd() {}
	
	/**
	 * 게시물 추가 ( 2 모집요강 / 3 편입뉴스 / 4 성공스토리 )
	 * @param images 파일 
	 * @param dto 
	 * @param filenames summertnote통해 업로드된 파일
	 */		
	@PostMapping("/admin/board")
	@ResponseBody
	public String adInsertBoard(HttpServletRequest request,
								@RequestParam(value = "images", required = false) List<MultipartFile> files, 
								@ModelAttribute BoardDto dto, 
								@RequestParam String filenames,
								HttpSession session,HttpServletRequest req) {
		String msg = "";
		
		if (session.getAttribute("m") != null) {
			//수정된 파일 삭제
			String content=dto.getCONTENT();
			SummerUtils.deleteFilenames(content, filenames);				
			
			//board
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM();
			dto.setREG_USER_NUM(USER_NUM);
			dto.setREG_IP(IpUtils.getRemoteIp(req));

			if (dto.getTOP_NOTICE_YN() != null) {
				dto.setTOP_NOTICE_YN("Y");
			} else {
				dto.setTOP_NOTICE_YN("N");
			}

			try {
				if (files != null) {
					List<FilesDto> files_list=new ArrayList<>();
					for (MultipartFile multi : files) {
						FilesDto files_dto = fileService.getAttachByMultipart(multi,"1");
						files_dto.setREG_USER_IP(IpUtils.getRemoteIp(req));
						files_dto.setREG_USER_NUM(USER_NUM);
						files_list.add(files_dto);
					}
					boardService.insertBoardWithFile(dto, files_list);
				}else {
					boardService.insertBoard(dto);
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg = "파일업로드에 에러가 발생했습니다. 관리자에게 문의해주세요";
			}

		} else {
			msg = "로그인이 필요한 서비스 입니다.";
		}
		return msg;
	}
	
	/**
	 * 과년도 모집요강 상세
	 * @param BRD_NUM 게시물_일련번호
	 */		
	@GetMapping("/admin/board/apply_detail")
	public void boardApplyDetail(@RequestParam String BRD_NUM, Model model, HttpSession session) {
		HashMap<String, Object> map = new HashMap<>();

		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			map.put("USER_NUM", USER_NUM);
			model.addAttribute("USER_NUM", USER_NUM);
		}else {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
			return;
		}

		map.put("BRD_NUM", BRD_NUM);

		try {
			BoardDto dto = boardService.findBoardByNum(map);

			if (dto.getFILE_YN().equals("Y")) {
				model.addAttribute("files", filesService.findFilesByNum(map));
			}

			model.addAttribute("b", dto);
			model.addAttribute("num", BRD_NUM);

		} catch (Exception e) {
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}
	}	
	
	//----------------------------대학정보---------------------------------------------
	
	/**
	 * 대학 정보 
	 */		
	@GetMapping("/admin/univ/ad_univ_info")
	public void adUnivInfo() {
	}	
	
	/**
	 * 대학 정보 추가
	 */		
	@GetMapping("/admin/univ/univ_info_add")
	public void adUnivInfoAdd() {
	}	
	
	/**
	 * 대학 편입 정보 상세
	 */		
	@GetMapping("/admin/univ/ad_trans_info")
	public void adTransInfo(Model model,HttpSession session) {
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			model.addAttribute("USER_NUM", USER_NUM);
		}			
		
		HashMap<String, Object> map=new HashMap<>();
		model.addAttribute("univList",  univCacheService.getUnivList(map));
		model.addAttribute("majorList", univService.findDeptNm());		
	}	

	/**
	 * 대학 리스트
	 * @param SCH     검색어
	 */		
	@GetMapping("/admin/univ/univ_info_list")
	public String adUnivInfoList(@RequestParam int nSelect, 
								 @RequestParam String SCH, 
								 Model model, HttpSession session) {
		HashMap<String, Object> map = new HashMap<>();

		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
		}
		map.put("SCH", SCH);
		try {
			List<UnivInfoDto> list = univService.findUnivNameNum(map);
			int nMaxRecordCnt = list.size();
			int nMaxVCnt = 10;

			map.put("nSelectPage", nSelect);
			map.put("nMaxVCnt", nMaxVCnt);

			list = univService.findUnivNameNum(map);
			
			model.addAttribute("list", list);
			model.addAttribute("size", nMaxRecordCnt);
			model.addAttribute("nMaxVCnt", nMaxVCnt);
			model.addAttribute("nMaxRecordCnt", nMaxRecordCnt);
			model.addAttribute("nSelectPage", nSelect);

		} catch (Exception e) {
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}

		return "forward:/WEB-INF/views/admin/univ/univ_info_list.jsp";		
	}
	
	
	/**
	 * 대학 정보 상세
	 * @param UNIV_NUM 대학일련번호
	 */		
	@GetMapping("/admin/univ/univ_info_detail")
	public void adUnivInfoDetail(@RequestParam int UNIV_NUM, Model model, HttpSession session) {
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
			return;
		}
		model.addAttribute("UNIV_NUM", UNIV_NUM);
		
		HashMap<String, Object> map=new HashMap<>();
		map.put("UNIV_NUM", UNIV_NUM);		
		
		try {
			UnivInfoDto dto=univService.findUnivInfo(map);
			
			dto.setAPY_ST_DTM(dateService.DateConversion3(dto.getAPY_ST_DTM()));
			dto.setAPY_ED_DTM(dateService.DateConversion3(dto.getAPY_ED_DTM()));
			dto.setSLT_DATE(dateService.DateConversion(dto.getSLT_DATE()));
			
			String suc_apy_dtm=dto.getSUC_APY_DTM();
			String intv_dtm=dto.getINTV_DTM();
			String doc_sub_dtm = dto.getDOC_SUB_DTM();
			int size =0;
			int size2 = 0;
			
			//합격자발표
			if(suc_apy_dtm != null) {
				List<String> suc = dateService.GetDateList2(suc_apy_dtm);
				
				size=suc.size();
				model.addAttribute("suc", suc);
			}
			
			//면접일정
			if(intv_dtm != null) {
				List<String> intv =dateService.GetDateList2(intv_dtm);
				size2=intv.size();
				
				model.addAttribute("intv", intv);
			}
			
			//서류제출
			if(doc_sub_dtm != null) {
				List<String> doc = dateService.GetDateList2(doc_sub_dtm);				
				model.addAttribute("doc", doc);
			}
			
			model.addAttribute("size", size);
			model.addAttribute("size2", size2);
			model.addAttribute("s", dto);
			
		} catch (Exception e) {
			model.addAttribute("error", "오류가 발생했습니다. 관리자에게 문의해주세요.");
		}	
	}	
	
	/**
	 * 대학 정보 수정 페이지
	 * @param UNIV_NUM 대학일련번호
	 */		
	@GetMapping("/admin/univ/univ_info_update")
	public void adUnivInfoUpdate(@RequestParam int UNIV_NUM, Model model, HttpSession session) {
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
			return;
		}		
		
		HashMap<String, Object> map=new HashMap<>();
		map.put("UNIV_NUM", UNIV_NUM);		
		
		try {
			UnivInfoDto dto=univService.findUnivInfo(map);
			String APY_ST_DTM = dto.getAPY_ST_DTM();
			String APY_ED_DTM = dto.getAPY_ED_DTM();
			
			if(APY_ST_DTM != null) {
				//yyyy-MM-dd
				dto.setAPY_ST_DTM(dateService.DateConversion2(APY_ST_DTM.substring(0, 8)));
				//HH 시간
				model.addAttribute("APY_ST_DTM_HH", APY_ST_DTM.substring(8, 10));
				//MM 분
				model.addAttribute("APY_ST_DTM_MM", APY_ST_DTM.substring(10, 12));
			}
			
			if(APY_ED_DTM != null) {
				//yyyy-MM-dd
				dto.setAPY_ED_DTM(dateService.DateConversion2(APY_ST_DTM.substring(0, 8)));
				//HH 시간
				model.addAttribute("APY_ED_DTM_HH", APY_ST_DTM.substring(8, 10));
				//MM 분
				model.addAttribute("APY_ED_DTM_MM", APY_ST_DTM.substring(10, 12));					
			}			
			
			dto.setSLT_DATE(dateService.DateConversion2(dto.getSLT_DATE()));
			
			String suc_apy_dtm=dto.getSUC_APY_DTM();
			String intv_dtm=dto.getINTV_DTM();
			String doc_sub_dtm = dto.getDOC_SUB_DTM();
			
			//합격자발표
			if(suc_apy_dtm != null) {
				List<String> suc = dateService.GetDateList(suc_apy_dtm);
				model.addAttribute("suc", suc);
			}
			
			//면접일정
			if(intv_dtm != null) {
				List<String> intv =dateService.GetDateList(intv_dtm);
				model.addAttribute("intv", intv);
			}
			
			//서류제출
			if(doc_sub_dtm != null) {
				List<String> doc = dateService.GetDateList(doc_sub_dtm);		
				model.addAttribute("doc", doc);
			}
			model.addAttribute("s", dto);
		} catch (Exception e) {
			model.addAttribute("error", "오류가 발생했습니다. 관리자에게 문의해주세요.");
		}			
	}		
	
	/**
	 * 대학 정보 수정
	 * @param filename	기존 파일
	 * @param images    새로운 파일
	 */			
	@PutMapping("/admin/univ")
	@ResponseBody
	public String updateUniv(@RequestParam(value="images",required = false) MultipartFile file,
							@RequestParam int UNIV_NUM,
							@RequestParam String UNIV_NAME,
							@RequestParam String ADDR,
							@RequestParam String URL,
							@RequestParam String PHONE,
							@RequestParam String APY_ST_DTM,
							@RequestParam String APY_ED_DTM,
							@RequestParam String APY_NOTE,
							@RequestParam String SLT_DATE,
							@RequestParam String SLT_NOTE,
							@RequestParam String SUC_APY_DTM,
							@RequestParam String SUC_APY_NOTE,
							@RequestParam String DOC_SUB_DTM,
							@RequestParam String DOC_SUB_NOTE,
							@RequestParam String INTV_DTM,
							@RequestParam String INTV_NOTE,
							@RequestParam String REG_YY,
							@RequestParam String filename,
							HttpSession session) {
		
		String msg="";
		
		if (session.getAttribute("m") == null) {
			msg = "로그인이 필요한 서비스입니다.";
			return msg;
		}		
		
		UnivInfoDto univ=new UnivInfoDto();
		univ.setUNIV_NUM(UNIV_NUM);
		univ.setUNIV_NAME(UNIV_NAME);
		univ.setADDR(ADDR);
		univ.setURL(URL);
		univ.setPHONE(PHONE);
		univ.setAPY_ST_DTM(APY_ST_DTM);
		univ.setAPY_ED_DTM(APY_ED_DTM);
		univ.setAPY_NOTE(SUC_APY_NOTE);
		univ.setSLT_DATE(SLT_DATE);
		univ.setSLT_NOTE(SLT_NOTE);
		univ.setSUC_APY_DTM(SUC_APY_DTM);
		univ.setSUC_APY_NOTE(SUC_APY_NOTE);
		univ.setDOC_SUB_DTM(DOC_SUB_DTM);
		univ.setDOC_SUB_NOTE(DOC_SUB_NOTE);
		univ.setINTV_DTM(INTV_DTM);
		univ.setINTV_NOTE(INTV_NOTE);
		univ.setREG_YY(REG_YY);
		try {
			if(file!=null) {
				if( filename != null) {
					fileService.deleteImage(filename); 
				}
				String originalName = fileService.getAttachByMultipart2(file,"1");
				univ.setLOGO(originalName);
			}
		} catch (Exception e) {
			msg="파일 업로드 에러가 발생했습니다. 관리자에게 문의해주세요.";
		}
		
		try {
			univService.updateUniv(univ);
			univCacheService.clearUnivCache(); //데이터 업데이트 시 캐시 무효화
		} catch (Exception e) {
			msg="내용 변경에 실패했습니다. 관리자에게 문의해주세요.";
		}
		return msg;
	}
	
	//----------------------------편입학과정보---------------------------------------------
	
	/**
	 * 편입 학과 정보 리스트
	 * @param UNIV_NAME    대학이름
	 * @param MAJOR_NM     전공이름
	 */		
	@GetMapping("/admin/univ/trans_info_list")
	public String adTransInfoList(@RequestParam String MAJOR_NM, 
								  @RequestParam(defaultValue = "1")int nSelect, 
								  @RequestParam String UNIV_NAME, 
								  Model model, HttpSession session) {
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
			return "forward:/WEB-INF/views/admin/univ/trans_info_list.jsp";		
		}		

		HashMap<String, Object> map = new HashMap<>();
		map.put("UNIV_NAME", UNIV_NAME);
		map.put("MAJOR_NM", MAJOR_NM);
		
		try {
			List<TransInfoDto> list=univService.findTransInfo(map);
			int nMaxRecordCnt=list.size();
			int nMaxVCnt=10;
			
			map.put("nSelectPage", nSelect);
			map.put("nMaxVCnt",nMaxVCnt);
			list=univService.findTransInfo(map);
			
			model.addAttribute("list", list);
			model.addAttribute("nMaxVCnt", nMaxVCnt);
			model.addAttribute("nMaxRecordCnt", nMaxRecordCnt);		
			model.addAttribute("nSelectPage", nSelect);

		} catch (Exception e) {
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}

		return "forward:/WEB-INF/views/admin/univ/trans_info_list.jsp";		
	}	
	
	/**
	 *  편입 학과 정보 추가 
	 */		
	@GetMapping("/admin/trans_info_add")
	public String TransInfoAdd(Model model,HttpSession session,HttpServletRequest request, RedirectAttributes re) {
		if (session.getAttribute("m") != null) {
			return "redirect:/admin/univ/trans_info_add";
		}else {
			String uri = request.getHeader("Referer");
			re.addFlashAttribute("referer",uri);
			return "redirect:/main/login_form";			
		}		
	}	
	
	/**
	 *  편입 학과 정보 추가 페이지
	 */		
	@GetMapping("/admin/univ/trans_info_add")
	public void adTransInfoAdd(Model model,HttpSession session) {
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
		}	
		
		HashMap<String, Object> map=new HashMap<>();
		model.addAttribute("univList", univCacheService.getUnivList(map));		
	}	
	
	/**
	 * 편입 학과 정보 추가
	 * @param dto
	 */			
	@PostMapping("/admin/trans")
	@ResponseBody
	public String insertTrans(@ModelAttribute TransInfoDto dto,HttpSession session) 
	{
		String msg="";
		
		if (session.getAttribute("m") == null) {
			msg="로그인이 필요한 서비스입니다.";
			return msg;
		}		
		
		try {
			int TRANS_NUM=univService.getNextTransNum(dto.getUNIV_NUM());
			dto.setTRANS_NUM(TRANS_NUM);
			univService.insertTransInfo(dto);
		} catch (Exception e) {
			e.printStackTrace();
			msg="추가에 실패했습니다. 관리자에게 문의해주세요.";
		}
		return msg;
	}
	
	/**
	 *  편입 학과 정보 상세 페이지
	 *  @param UNIV_NUM
	 *  @param TRANS_NUM
	 */		
	@GetMapping("/admin/univ/trans_info_detail")
	public void adTransInfoDetail(Model model, HttpSession session , 
								  @RequestParam int UNIV_NUM , 
								  @RequestParam int TRANS_NUM) {
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
		}	
		
		HashMap<String, Object> map=new HashMap<>();
		map.put("UNIV_NUM", UNIV_NUM);
		map.put("TRANS_NUM", TRANS_NUM);
		model.addAttribute("s", univService.findTransInfoByNum(map));		
	}		
	
	/**
	 * 편입 학과 정보 삭제
	 * @param UNIV_NUM
	 * @param TRANS_NUM
	 */			
	@DeleteMapping("/admin/trans")
	@ResponseBody
	public String deleteTrans(@RequestParam int UNIV_NUM, 
							  @RequestParam	int TRANS_NUM,
							  HttpSession session){
		String msg="";
		if (session.getAttribute("m") == null) {
			msg="로그인이 필요한 서비스입니다.";
			return msg;
		}		
		
		try {
			HashMap<String, Object> map=new HashMap<String, Object>();
			map.put("UNIV_NUM", UNIV_NUM);
			map.put("TRANS_NUM", TRANS_NUM);
			univService.deleteTransInfo(map);
		} catch (Exception e) {
			msg="삭제에 실패했습니다. 관리자에게 문의해주세요.";
		}
		return msg;
	}	
	
	/**
	 *  편입 학과 정보 상세 페이지
	 *  @param UNIV_NUM
	 *  @param TRANS_NUM
	 */		
	@GetMapping("/admin/univ/trans_info_update")
	public void adTransInfoUpdate(Model model, HttpSession session , 
								  @RequestParam int UNIV_NUM , 
								  @RequestParam int TRANS_NUM) {
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
		}			
		
		HashMap<String, Object> map=new HashMap<>();
		map.put("UNIV_NUM", UNIV_NUM);
		map.put("TRANS_NUM", TRANS_NUM);
		model.addAttribute("s", univService.findTransInfoByNum(map));		
		
		HashMap<String, Object> map2=new HashMap<>();
		model.addAttribute("univList",  univCacheService.getUnivList(map2));				
	}	
	
	/**
	 * 편입 학과 정보 수정
	 * @param dto
	 */			
	@PutMapping("/admin/trans")
	@ResponseBody
	public String updateTrans(@ModelAttribute TransInfoDto dto,HttpSession session) 
	{
		String msg="";
		if (session.getAttribute("m") == null) {
			msg="로그인이 필요한 서비스입니다.";
			return msg;
		}	
		
		try {
			univService.updateTransInfo(dto);
		} catch (Exception e) {
			msg="수정에 실패했습니다. 관리자에게 문의해주세요.";
		}
		return msg;
	}	
	
	/**
	 *  편입 학과 정보 상세 페이지
	 *  @param UNIV_NUM
	 *  @param TRANS_NUM
	 */		
	@GetMapping("/admin/univ/ad_slt_info")
	public void adSltInfo(Model model, HttpSession session) {
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			model.addAttribute("USER_NUM", USER_NUM);
		}		
	}		
	
	//----------------------------편입전형---------------------------------------------
	
	/**
	 * 편입전형 페이지
	 * @param SCH     검색어
	 */		
	@GetMapping("/admin/univ/slt_info_list")
	public String adSltInfoList(@RequestParam(defaultValue = "1") int nSelect, 
								@RequestParam String SCH,
								Model model, HttpSession session) {
		HashMap<String, Object> map = new HashMap<>();

		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
		}
		map.put("SCH", SCH);

		try {
			List<SltDetailInfoDto> list=univService.findSltInfo(map);
			int nMaxRecordCnt = list.size();
			int nMaxVCnt = 10;

			map.put("nSelectPage", nSelect);
			map.put("nMaxVCnt", nMaxVCnt);

			list = univService.findSltInfo(map);

			model.addAttribute("list", list);
			model.addAttribute("size", nMaxRecordCnt);
			model.addAttribute("nMaxVCnt", nMaxVCnt);
			model.addAttribute("nMaxRecordCnt", nMaxRecordCnt);
			model.addAttribute("nSelectPage", nSelect);

		} catch (Exception e) {
			model.addAttribute("error", "오류가 발생했습니다. 관리자에게 문의해주세요.");
		}

		return "forward:/WEB-INF/views/admin/univ/slt_info_list.jsp";		
	}	
	
	/**
	 *  편입 전형 상세 페이지
	 *  @param SLT_NUM
	 */		
	@GetMapping("/admin/univ/slt_info_detail")
	public void adSltInfoDetail(Model model, HttpSession session, 
								@RequestParam int SLT_NUM) {
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
		}			
		HashMap<String, Object> map=new HashMap<>();
		map.put("SLT_NUM", SLT_NUM);		
		
		try {
			SltDetailInfoDto dto=univService.findSltInfo(map).get(0); 
			List<SltDetailInfoDto> list2 = new ArrayList<>();
			
			List<SltDetailInfoDto> detail=univService.findSltDetailInfo(map);
			int size=detail.size();
			dto.setSize(size);
			
			for(int i=0;i<size;i++) {
				SltDetailInfoDto dto2=detail.get(i);
				
				if(dto2.getSLT_STEP().indexOf("2") == -1) {
					dto.setSLT_STEP(dto2.getSLT_STEP());
					dto.setENG_PC(dto2.getENG_PC());
					dto.setMATH_PC(dto2.getMATH_PC());
					dto.setMAJOR_PC(dto2.getMAJOR_PC());
					dto.setPREV_GRADE(dto2.getPREV_GRADE());
					dto.setINTV_PC(dto2.getINTV_PC());
					dto.setRECOG_ENG_PC(dto2.getRECOG_ENG_PC());
					dto.setWHATEVER(dto2.getWHATEVER());
					dto.setWHATEVER_NOTE(dto2.getWHATEVER_NOTE());
				}else{
					dto.setSLT_STEP2(dto2.getSLT_STEP());
					dto.setENG_PC2(dto2.getENG_PC());
					dto.setMATH_PC2(dto2.getMATH_PC());
					dto.setMAJOR_PC2(dto2.getMAJOR_PC());
					dto.setPREV_GRADE2(dto2.getPREV_GRADE());
					dto.setINTV_PC2(dto2.getINTV_PC()+"");
					dto.setRECOG_ENG_PC2(dto2.getRECOG_ENG_PC());
					dto.setWHATEVER2(dto2.getWHATEVER());
					dto.setWHATEVER_NOTE2(dto2.getWHATEVER_NOTE());
				}
				
			}
			list2.add(dto);
			
			model.addAttribute("t", dto);
			model.addAttribute("list", list2);
			model.addAttribute("size", list2.size());
		} catch (Exception e) {
			model.addAttribute("error", "오류가 발생했습니다. 관리자에게 문의해주세요.");
		}
		
	}		

	/**
	 *  편입 전형 추가 
	 */		
	@GetMapping("/admin/slt_info_add")
	public String SltInfoAdd(Model model, HttpSession session,HttpServletRequest request, RedirectAttributes re) {
		if (session.getAttribute("m") != null) {
			return "redirect:/admin/univ/slt_info_add";
		}else {
			String uri = request.getHeader("Referer");
			re.addFlashAttribute("referer",uri);
			return "redirect:/main/login_form";			
		}			
	}		
	
	/**
	 *  편입 전형 추가 페이지
	 */		
	@GetMapping("/admin/univ/slt_info_add")
	public void adSltInfoAdd(Model model, HttpSession session) {
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
		}			
		HashMap<String, Object> map2=new HashMap<>();
		model.addAttribute("univList",  univCacheService.getUnivList(map2));				
	}		
	
	/**
	 * 편입전형 추가
	 * @param dto
	 * @param step S:단계별 A:일괄합산
	 */			
	@PostMapping("/admin/slt")
	@ResponseBody
	public String insertSlt(@ModelAttribute SltDetailInfoDto dto,
							@RequestParam String step,
							HttpSession session){
		String msg="";
		if (session.getAttribute("m") == null) {
			msg="로그인이 필요한 서비스입니다.";
			return msg;
		}	
		int slt_num=univService.findNextSltNum();
		try {
			//------slt_info 
			dto.setSLT_NUM(slt_num);
			univService.insertSltInfo(dto);
			
			//------slt_detail_info
			int SLT_DETAIL_NUM=univService.findNextSltDetailNum(slt_num);
			dto.setSLT_DETAIL_NUM(SLT_DETAIL_NUM);
			univService.insertSltDetailInfo(dto);
			
			//1단계+2단계일 때 
			if(step.equals("S")) {
				SltDetailInfoDto dto2 = new SltDetailInfoDto();
				dto2.setSLT_NUM(slt_num);
				dto2.setSLT_STEP(dto.getSLT_STEP2());
				dto2.setENG_PC(dto.getENG_PC2());
				dto2.setMATH_PC(dto.getMATH_PC2());
				dto2.setMAJOR_PC(dto.getMAJOR_PC2());
				dto2.setPREV_GRADE(dto.getPREV_GRADE2());
				dto2.setINTV_PC(dto.getINTV_PC2()+"");
				dto2.setRECOG_ENG_PC(dto.getRECOG_ENG_PC2());
				dto2.setWHATEVER(dto.getWHATEVER2());
				dto2.setWHATEVER_NOTE(dto.getWHATEVER_NOTE2());

				SLT_DETAIL_NUM=univService.findNextSltDetailNum(slt_num);
			    dto2.setSLT_DETAIL_NUM(SLT_DETAIL_NUM);				
				univService.insertSltDetailInfo(dto2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			HashMap<String, Object> map=new HashMap<>();
			map.put("SLT_NUM",slt_num);
			univService.deleteSltInfo(map);
			
			msg="추가에 실패했습니다. 관리자에게 문의해주세요.";
		}
		return msg;
	}		

	/**
	 * 편입전형 삭제
	 * @param SLT_NUM
	 */			
	@DeleteMapping("/admin/slt")
	@ResponseBody
	public String deleteSlt(@RequestParam int SLT_NUM, HttpSession session) 
	{
		String msg="";
		if (session.getAttribute("m") == null) {
			msg="로그인이 필요한 서비스입니다.";
			return msg;
		}		
		try {
			HashMap<String, Object> map=new HashMap<String, Object>();
			map.put("SLT_NUM", SLT_NUM);
			univService.deleteSltInfo(map);
			univService.deleteSltDetailInfo(map); //cascade이지만 2차 확인을 위해서
		} catch (Exception e) {
			msg="삭제에 실패했습니다. 관리자에게 문의해주세요.";
		}
		return msg;
	}	

	/**
	 *  편입전형 수정 페이지
	 *  @param SLT_NUM
	 */		
	@GetMapping("/admin/univ/slt_info_update")
	public void adSltInfoUpdate(Model model, HttpSession session, 
								@RequestParam int SLT_NUM) {
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
		}			
		
		//대학 리스트
		HashMap<String, Object> map2=new HashMap<>();
		model.addAttribute("univList",  univCacheService.getUnivList(map2));	

		HashMap<String, Object> map=new HashMap<>();
		map.put("SLT_NUM", SLT_NUM);		
		
		try {
			SltDetailInfoDto dto=univService.findSltInfo(map).get(0); 
			List<SltDetailInfoDto> list2 = new ArrayList<>();
			
			List<SltDetailInfoDto> detail=univService.findSltDetailInfo(map);
			int size=detail.size();
			dto.setSize(size);
		
			for(int i=0; i<size; i++) {
				SltDetailInfoDto dto2=detail.get(i);
				if(dto2.getSLT_STEP().indexOf("2") == -1) {
					dto.setSLT_DETAIL_NUM(dto2.getSLT_DETAIL_NUM());
					dto.setSLT_STEP(dto2.getSLT_STEP());
					dto.setENG_PC(dto2.getENG_PC());
					dto.setMATH_PC(dto2.getMATH_PC());
					dto.setMAJOR_PC(dto2.getMAJOR_PC());
					dto.setPREV_GRADE(dto2.getPREV_GRADE());
					dto.setINTV_PC(dto2.getINTV_PC());
					dto.setRECOG_ENG_PC(dto2.getRECOG_ENG_PC());
					dto.setWHATEVER(dto2.getWHATEVER());
					dto.setWHATEVER_NOTE(dto2.getWHATEVER_NOTE());
				}else{
					dto.setSLT_DETAIL_NUM2(dto2.getSLT_DETAIL_NUM());
					dto.setSLT_STEP2(dto2.getSLT_STEP());
					dto.setENG_PC2(dto2.getENG_PC());
					dto.setMATH_PC2(dto2.getMATH_PC());
					dto.setMAJOR_PC2(dto2.getMAJOR_PC());
					dto.setPREV_GRADE2(dto2.getPREV_GRADE());
					dto.setINTV_PC2(dto2.getINTV_PC());
					dto.setRECOG_ENG_PC2(dto2.getRECOG_ENG_PC());
					dto.setWHATEVER2(dto2.getWHATEVER());
					dto.setWHATEVER_NOTE2(dto2.getWHATEVER_NOTE());
				}
				
			}
			list2.add(dto);
			model.addAttribute("t", dto);
			model.addAttribute("list", list2);
			model.addAttribute("size", list2.size());
		} catch (Exception e) {
			model.addAttribute("error", "오류가 발생했습니다. 관리자에게 문의해주세요.");
		}		
		
	}			

	/**
	 * 편입전형 수정
	 * @param dto
	 * @param step S:단계별 A:일괄합산
	 */			
	@PutMapping("/admin/slt")
	@ResponseBody
	public String updateSlt(@ModelAttribute SltDetailInfoDto dto,
							@RequestParam String step,
							HttpSession session){
		String msg="";
		if (session.getAttribute("m") == null) {
			msg="로그인이 필요한 서비스입니다.";
			return msg;
		}	
		int slt_num=dto.getSLT_NUM();
		
		try {
			dto.setSLT_NUM(slt_num);
			//------slt_info 
			univService.updateSltInfo(dto);
			//------slt_detail_info
			univService.updateSltDetailInfo(dto);
			
			//1단계+2단계일 때 
			if(step.equals("S")) {
				SltDetailInfoDto dto2 = new SltDetailInfoDto();
				dto2.setSLT_NUM(slt_num);
				dto2.setSLT_STEP(dto.getSLT_STEP2());
				dto2.setENG_PC(dto.getENG_PC2());
				dto2.setMATH_PC(dto.getMATH_PC2());
				dto2.setMAJOR_PC(dto.getMAJOR_PC2());
				dto2.setPREV_GRADE(dto.getPREV_GRADE2());
				dto2.setINTV_PC(dto.getINTV_PC2()+"");
				dto2.setRECOG_ENG_PC(dto.getRECOG_ENG_PC2());
				dto2.setWHATEVER(dto.getWHATEVER2());
				dto2.setWHATEVER_NOTE(dto.getWHATEVER_NOTE2());
				
				//SLT_DETAIL_NUM2가 없으면 0으로 세팅됨
				if(dto.getSLT_DETAIL_NUM2()!= 0) {
					dto2.setSLT_DETAIL_NUM(dto.getSLT_DETAIL_NUM2());
					univService.updateSltDetailInfo(dto2);
					
				}else {
					int next=univService.findNextSltDetailNum(slt_num);
					
					dto2.setSLT_DETAIL_NUM(next);
					univService.insertSltDetailInfo(dto2);
				}
			}else {
				//2단계가 존재한다면
				if(dto.getSLT_DETAIL_NUM2()!= 0) {
					HashMap<String, Object> map=new HashMap<>();
					map.put("SLT_NUM", slt_num);
					map.put("SLT_DETAIL_NUM", dto.getSLT_DETAIL_NUM2());
					univService.deleteSltDetailInfo(map);
				}
			}
		} catch (Exception e) {
			msg="추가에 실패했습니다. 관리자에게 문의해주세요.";
		}
		return msg;
	}			
	
	//----------------------------사용자 정보---------------------------------------------
	
	/**
	 * 사용자 정보
	 */		
	@GetMapping("/admin/user/ad_user")
	public void adUser() {
	}	
	
	/**
	 * 사용자 정보 리스트
	 * @param SCH     검색어
	 * @param ROLE	  권한 USER/ADMIN
	 */		
	@GetMapping("/admin/user/user_list")
	public String adUserList(@RequestParam(defaultValue = "1") int nSelect, 
							 @RequestParam String SCH,
							 @RequestParam(defaultValue = "") String ROLE, 
							 Model model, HttpSession session) {
		String url="forward:/WEB-INF/views/admin/user/user_list.jsp";
		
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
			return url;		
		}else {
			if(!((UserDto) session.getAttribute("m")).getROLE().equals("ROLE_ADMIN")) {
				model.addAttribute("error", "권한이 없습니다.");
				return url;		
			}
		}
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("ROLE", ROLE);
		map.put("SCH", SCH);
		try {
			List<UserDto> list =userService.findUsersBySch(map);
			int nMaxRecordCnt = list.size();
			int nMaxVCnt = 10;

			map.put("nSelectPage", nSelect);
			map.put("nMaxVCnt", nMaxVCnt);

			list =userService.findUsersBySch(map);
			model.addAttribute("list", list);
			model.addAttribute("size", nMaxRecordCnt);
			model.addAttribute("nMaxVCnt", nMaxVCnt);
			model.addAttribute("nMaxRecordCnt", nMaxRecordCnt);
			model.addAttribute("nSelectPage", nSelect);

		} catch (Exception e) {
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}

		return url;		
	}
	
	/**
	 * 사용자 상세 정보
	 * @param USER_NUM 회원일련번호
	 * @throws Exception 
	 */		
	@GetMapping("/admin/user/user_detail")
	public void adUserDetail(HttpSession session,Model model,
							 @RequestParam String USER_NUM) throws Exception {
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
			return;
		}else {
			if(!((UserDto) session.getAttribute("m")).getROLE().equals("ROLE_ADMIN")) {
				model.addAttribute("error", "권한이 없습니다.");
				return;
			}
		}
		HashMap<String, Object> map=new HashMap<>();
		map.put("USER_NUM", USER_NUM);
		model.addAttribute("s", userService.findUserseByNum(map));
	}		
	
	/**
	 * 사용자 삭제
	 * @param USER_NUM 회원일련번호
	 * @throws Exception 
	 */			
	@DeleteMapping("/admin/user")
	@ResponseBody
	public String deleteUser(@RequestParam String USER_NUM, HttpSession session,HttpServletRequest req) throws Exception {
		String msg="";
		if (session.getAttribute("m") == null) {
			msg="로그인이 필요한 서비스입니다.";
			return msg;
		}else {
			if(!((UserDto) session.getAttribute("m")).getROLE().equals("ROLE_ADMIN")) {
				try {
					HashMap<String, Object> map=new HashMap<String, Object>();
					map.put("USER_NUM", USER_NUM);
					
					//관리자삭제로그
					UserDto user=userService.findUsersByNum(USER_NUM);
					String social=user.getSOCIAL_NM();
					String CLIENT_ID=user.getCLIENT_ID();
					userService.deleteUser(map, CLIENT_ID, social);
					
					logService.saveLog((UserDto) session.getAttribute("m"),"관리자가 탈퇴시킴",req);
					
				} catch (Exception e) {
					String error=e.getMessage();
					logService.insertErrorLog(req,session,error);
					msg="삭제에 실패했습니다.";
				}
				
			}else {
				msg="권한이 없습니다.";
				ErrorLogDto error=new ErrorLogDto();
				error.setERROR("관리자 권한없이 회원 탈퇴 시도");
				error.setERROR_URL(IpUtils.getIpFromRequest(req));
				return msg;
			}
		}

		return msg;
	}	
	
	/**
	 * 사용자 상세 정보
	 * @param USER_NUM 회원일련번호
	 * @throws Exception 
	 */		
	@GetMapping("/admin/user/user_update")
	public void adUserUpdate(HttpSession session,Model model,
							 @RequestParam String USER_NUM) throws Exception {
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
			return;
		}else {
			if(!((UserDto) session.getAttribute("m")).getROLE().equals("ROLE_ADMIN")) {
				model.addAttribute("error", "권한이 없습니다.");
				return;
			}
		}
		HashMap<String, Object> map=new HashMap<>();
		map.put("USER_NUM", USER_NUM);
		model.addAttribute("t", userService.findUserseByNum(map));
	}	
	
	/**
	 * 사용자 수정
	 * @param dto
	 */			
	@PutMapping("/admin/user")
	@ResponseBody
	public String updateeUser(
			@RequestParam String USER_NM,
			@RequestParam String USER_NUM,
			@RequestParam String ROLE,
			HttpSession session) {
		String msg="";
		UserDto dto=new UserDto();
		dto.setUSER_NM(USER_NM);
		dto.setUSER_NUM(USER_NUM);
		dto.setROLE(ROLE);
		
		if (session.getAttribute("m") == null) {
			msg="로그인이 필요한 서비스입니다.";
			return msg;
		}else {
			if(!((UserDto) session.getAttribute("m")).getROLE().equals("ROLE_ADMIN")) {
				msg="권한이 없습니다.";
				return msg;
			}
		}
		try {
			userService.updateUsers(dto);
		} catch (Exception e) {
			msg="수정에 실패했습니다. 관리자에게 문의해주세요.";
		}
		return msg;
	}		
	
	/**
	 * 로그 정보
	 */		
	@GetMapping("/admin/user/ad_acss_log")
	public void adAcssLog(HttpSession session) {
	}	

	/**
	 * 로그 정보 리스트
	 * @param SCH     검색어
	 */		
	@GetMapping("/admin/user/acss_log_list")
	public String adAcssLogList(@RequestParam(defaultValue = "1") int nSelect, 
								@RequestParam String SCH, 
								Model model, HttpSession session) {
		String url="forward:/WEB-INF/views/admin/user/acss_log_list.jsp";	
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
			return 	url;
		}else {
			if(!((UserDto) session.getAttribute("m")).getROLE().equals("ROLE_ADMIN")) {
				model.addAttribute("error", "권한이 없습니다.");
				return url;
			}
		}
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("SCH", SCH);

		try {
			List<AcssLogDto> list =logService.findAcssLog(map);
			int nMaxRecordCnt = list.size();
			int nMaxVCnt = 10;

			map.put("nSelectPage", nSelect);
			map.put("nMaxVCnt", nMaxVCnt);

			list =logService.findAcssLog(map);

			model.addAttribute("list", list);
			model.addAttribute("size", nMaxRecordCnt);
			model.addAttribute("nMaxVCnt", nMaxVCnt);
			model.addAttribute("nMaxRecordCnt", nMaxRecordCnt);
			model.addAttribute("nSelectPage", nSelect);

		} catch (Exception e) {
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}

		return url;		
	}	
		
	//----------------------------편입뉴스---------------------------------------------
	
	/**
	 * 편입뉴스
	 */		
	@GetMapping("/admin/board/ad_univ_news")
	public void adUnivNews(HttpSession session,Model model) {
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			model.addAttribute("USER_NUM", USER_NUM);
		}			
	}	
	
	/**
	 * 편입뉴스 리스트
	 * @param BRD_CTG 카테고리
	 * @param SCH     검색어
	 */		
	@GetMapping("/admin/board/univ_news_list")
	public String adUnivNewsList(@RequestParam String BRD_CTG, 
								 @RequestParam(defaultValue = "1") int nSelect, 
								 @RequestParam String SCH, 
								 Model model, HttpSession session) {
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");;
		}
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("SCH", SCH);
		map.put("BRD_CTG", BRD_CTG);

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
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}

		return "forward:/WEB-INF/views/admin/board/univ_news_list.jsp";		
	}		
	
	/**
	 * 편입뉴스 상세
	 * @param BRD_NUM 게시물_일련번호
	 */		
	@GetMapping("/admin/board/univ_news_detail")
	public void adUnivNewsDetail(@RequestParam String BRD_NUM, Model model, HttpSession session) {
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");;
		}
			
		HashMap<String, Object> map = new HashMap<>();
		map.put("BRD_NUM", BRD_NUM);

		try {
			BoardDto dto = boardService.findBoardByNum(map);

			if (dto.getFILE_YN().equals("Y")) {
				model.addAttribute("files", filesService.findFilesByNum(map));
			}

			model.addAttribute("b", dto);
			model.addAttribute("num", BRD_NUM);

		} catch (Exception e) {
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}
	}		

	/**
	 * 편입뉴스 추가 페이지
	 */		
	@GetMapping("/admin/board/univ_news_add_form")
	public String adUnivNewsAdd(HttpSession session,HttpServletRequest request, RedirectAttributes re) {
		if (session.getAttribute("m") != null) {
			return "redirect:/admin/board/univ_news_add";
		}else {
			String uri = request.getHeader("Referer");
			re.addFlashAttribute("referer",uri);
			return "redirect:/main/login_form";			
		}		
	}	
	
	@GetMapping("/admin/board/univ_news_add")
	public void univNewsAdd() {
		
	}
	
	//----------------------------편입이야기---------------------------------------------
	
	/**
	 * 편입이야기 페이지
	 */		
	@GetMapping("/admin/board/ad_talk")
	public void adTalk() {
	}	
	
	/**
	 * 편입이야기 리스트
	 * @param DV   구분   F 자유주제 / Q 질문하기 / N 전체
	 * @param SCH  검색어
	 * @param BRD_CTG 카테고리
	 */		
	@GetMapping("/admin/board/talk_list")
	public String adTalkList(@RequestParam String BRD_CTG,
							 @RequestParam(defaultValue = "N") String DV, 
							 @RequestParam(defaultValue = "1") int nSelect, 
							 @RequestParam String SCH, 
							 Model model, HttpSession session) {
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");;
		}
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("SCH", SCH);
		map.put("BRD_CTG", BRD_CTG);
		
		if (!DV.equals("N")) {
			map.put("DV", DV);
		}
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
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}

		return "forward:/WEB-INF/views/admin/board/talk_list.jsp";		
	}		
	
	/**
	 * 편입 이야기 게시물 수정
	 * @param b
	 * @param filenames summertnote통해 업로드된 파일
	 */			
	@PutMapping("/admin/talk")
	@ResponseBody
	public String updateTalk(@ModelAttribute BoardDto b,
							 @RequestParam String filenames,
							 @RequestParam String UPT_USER_NUM, 
							 HttpSession session,HttpServletRequest req) {
		String msg = "";
		if (session.getAttribute("m") != null) {
			try {
				//수정자와 로그인한 자가 동일한지 확인
				String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM();
				if(!USER_NUM.equals(UPT_USER_NUM)) {
					return msg="게시물 수정에 실패했습니다. 정보를 다시 확인해주세요";
				}
				
				//수정된 파일 삭제
				String content=b.getCONTENT();
				SummerUtils.deleteFilenames(content, filenames);
				
				b.setUPT_USER_NUM(USER_NUM);
				b.setUPT_IP(IpUtils.getRemoteIp(req));

				boardService.updateBoard(b);
				
			} catch (Exception e) {
				msg = "에러가 발생했습니다. 관리자에게 문의해주세요";
			}
		} else {
			msg = "로그인이 필요한 서비스입니다.";
		}

		return msg;
	}	
	/**
	 * 편입이야기 상세
	 */		
	@GetMapping("/admin/board/talk_detail")
	public void adTalkDetail(@RequestParam String BRD_NUM, Model model, HttpSession session) {
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");;
		}
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("BRD_NUM", BRD_NUM);
		
		try {
			//스터디
			model.addAttribute("t", boardService.findBoardByNum(map));
			
		} catch (Exception e) {
		}		
		
		try {
			//댓글
			List<RpyDto> list = boardService.findRpyByNum(BRD_NUM);
			int size = list.size();
			
			model.addAttribute("list", list);
			model.addAttribute("size", size);	
			
		} catch (Exception e) {
			model.addAttribute("error2", "에러가 발생했습니다. 관리자에게 문의해주세요");
		}
	}		
	
	/**
	 * 편입이야기 수정 페이지
	 */		
	@GetMapping("/admin/board/talk_update")
	public void adTalkUpdate(@RequestParam String BRD_NUM, Model model, HttpSession session) {
		HashMap<String, Object> map = new HashMap<>();
		
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			model.addAttribute("USER_NUM", USER_NUM);
			map.put("USER_NUM", USER_NUM);
		} else {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
		}
		
		map.put("BRD_NUM", BRD_NUM);
		
		try {
			BoardDto t = boardService.findBoardByNum(map);
			String content = t.getCONTENT();
			
			//내용에 이미지 파일을 가지고 있는 경우
			if(content.indexOf("img") > 0) {
				String filenames =SummerUtils.extractFilenames(content);
				model.addAttribute("filenames", filenames);
			}
			
			model.addAttribute("t", t);
			model.addAttribute("num", BRD_NUM);			
			
		} catch (Exception e) {
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요");
		}		
	}		
	
	//----------------------------편입스터디---------------------------------------------
	
	/**
	 * 편입스터디
	 */	
	@GetMapping("/admin/board/ad_study")
	public void adStudy( Model model) {
	}	
	
	/**
	 * 편입 스터디 게시물 리스트
	 * @param SCH  검색어
	 * @param ING  모집 중인 스터디만 확인 Y: 모집중 / N: 전체
	 * @param BRD_CTG 카테고리
	 */
	@GetMapping("/admin/board/study_list")
	public String adStudyList(@RequestParam String BRD_CTG, 
							  @RequestParam String SCH, 
							  @RequestParam(defaultValue = "1")int nSelect, 
							  @RequestParam String ING,  
							  Model model, HttpSession session) {
		HashMap<String, Object> map = new HashMap<>();
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");;
		}

		if (!ING.equals("N") && !ING.equals("") && ING != null) {
			map.put("ING", ING);
		}

		map.put("BRD_CTG", BRD_CTG);
		map.put("SCH", SCH);

		try {
			int nMaxRecordCnt = boardService.findBoardSize(map);
			int nMaxVCnt = 10;
			
			map.put("nSelectPage", nSelect);
			map.put("nMaxVCnt", nMaxVCnt);
			List<BoardDto> list = boardService.findStudyByCtg(map);

			model.addAttribute("list", list);
			model.addAttribute("size", nMaxRecordCnt);
			model.addAttribute("nMaxVCnt", nMaxVCnt);
			model.addAttribute("nMaxRecordCnt", nMaxRecordCnt);
			model.addAttribute("nSelectPage", nSelect);
			
		} catch (Exception e) {
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요");
		}

		return "forward:/WEB-INF/views/admin/board/study_list.jsp";
	}	
	
	/**
	 * 편입 스터디 게시물 상세
	 * @param BRD_NUM  게시글_일련번호
	 */	
	@GetMapping("/admin/board/study_detail")
	public void adStudyDetail(@RequestParam String BRD_NUM, Model model,HttpSession session) {
		HashMap<String, Object> map = new HashMap<>();
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			model.addAttribute("USER_NUM", USER_NUM);
		}
		
		map.put("BRD_NUM", BRD_NUM);
		map.put("BRD_CTG", "1");

		try {
			model.addAttribute("t", boardService.findStudyByNum(map));
			
		} catch (Exception e) {
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요");
		}
		
		try {
			//댓글
			List<RpyDto> list = boardService.findRpyByNum(BRD_NUM);
			int size = list.size();
			
			model.addAttribute("list", list);
			model.addAttribute("size", size);	
			
		} catch (Exception e) {
			model.addAttribute("error2", "에러가 발생했습니다. 관리자에게 문의해주세요");
		}		
	}	
	
	/**
	 * 편입 스터디 게시물 수정 페이지
	 */			
	@GetMapping("/admin/board/study_update")
	public void studyUpdateForm(@RequestParam String BRD_NUM, Model model, HttpSession session) {
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
			
		} catch (Exception e) {
			model.addAttribute("error", "에러가 발생했습니다. 관리제에 문의해주세요");
		}
	}	

	/**
	 * 편입 스터디 게시물 수정 
	 * @param b
	 * @param filenames summertnote통해 업로드된 파일
	 */			
	@PutMapping("/admin/study")
	@ResponseBody
	public String updateStudy(@ModelAttribute BoardDto b,
							  @RequestParam String filenames, 
							  HttpSession session,HttpServletRequest req) {
		String msg = "";

		if (session.getAttribute("m") != null) {
			try {
				String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM();
				
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
				msg = "에러가 발생했습니다. 관리자에게 문의해주세요";
			}
		} else {
			msg = "로그인이 필요한 서비스입니다.";
		}
		return msg;
	}	
	
	//----------------------------합격수기---------------------------------------------	
	/**
	 * 합격수기
	 */	
	@GetMapping("/admin/board/ad_success_story")
	public void adSuccessStory( Model model) {
	}		
	
	/**
	 * 합격수기 리스트
	 * @param SCH  검색어
	 * @param BRD_CTG 카테고리
	 */		
	@GetMapping("/admin/board/success_story_list")
	public String adSuccessStoryList(@RequestParam String BRD_CTG , 
									 @RequestParam(defaultValue = "1") int nSelect, 
									 @RequestParam String SCH, 
									 Model model, HttpSession session) {
		if (session.getAttribute("m") == null) {
			model.addAttribute("error", "로그인이 필요한 서비스입니다.");
		}
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("SCH", SCH);
		map.put("BRD_CTG", BRD_CTG);
		
		try {
			int nMaxRecordCnt = boardService.findBoardSize(map);
			int nMaxVCnt = 10;

			map.put("nSelectPage", nSelect);
			map.put("nMaxVCnt", nMaxVCnt);
			List<BoardDto> list = boardService.findSuccess(map);

			model.addAttribute("list", list);
			model.addAttribute("size", nMaxRecordCnt);
			model.addAttribute("nMaxVCnt", nMaxVCnt);
			model.addAttribute("nMaxRecordCnt", nMaxRecordCnt);
			model.addAttribute("nSelectPage", nSelect);

		} catch (Exception e) {
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}

		return "forward:/WEB-INF/views/admin/board/success_story_list.jsp";		
	}	
	
	/**
	 * 합격 수기 게시물 상세
	 * @param BRD_NUM  게시글_일련번호
	 */	
	@GetMapping("/admin/board/success_story_detail")
	public void adSucessStoryDetail(@RequestParam String BRD_NUM, Model model,HttpSession session) {
		HashMap<String, Object> map = new HashMap<>();
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			model.addAttribute("USER_NUM", USER_NUM);
		}
		
		map.put("BRD_NUM", BRD_NUM);
		map.put("BRD_CTG", "4");

		try {
			model.addAttribute("t", boardService.findSuccessByNum(map));
			
		} catch (Exception e) {
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요");
		}
	
		try {
			//댓글
			List<RpyDto> list = boardService.findRpyByNum(BRD_NUM);
			int size = list.size();
			
			model.addAttribute("list", list);
			model.addAttribute("size", size);	
			
		} catch (Exception e) {
			model.addAttribute("error2", "에러가 발생했습니다. 관리자에게 문의해주세요");
		}		
	}	
	
	/**
	 * 합격 수기 게시물 수정 페이지
	 */			
	@GetMapping("/admin/board/success_story_update")
	public void SuccessStoryUpdateForm(@RequestParam String BRD_NUM, Model model, HttpSession session) {
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
			
		} catch (Exception e) {
			model.addAttribute("error", "에러가 발생했습니다. 관리제에 문의해주세요");
		}
	}		
	
	/**
	 * 합격 수기 수정 
	 * @param b
	 * @param filenames summertnote통해 업로드된 파일
	 */			
	@PutMapping("/admin/success_story")
	@ResponseBody
	public String updateSuccessStory(@ModelAttribute BoardDto b,
									 @RequestParam String filenames, 
									 HttpSession session,HttpServletRequest req) {
		String msg = "";
		
		if (session.getAttribute("m") != null) {
			try {
				//수정된 파일 삭제
				String content=b.getCONTENT();
				SummerUtils.deleteFilenames(content, filenames);				
				
				String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM();
				b.setUPT_USER_NUM(USER_NUM);
				b.setUPT_IP(IpUtils.getRemoteIp(req));
				
				boardService.updateSuc(b);
				
			} catch (Exception e) {
				msg = "에러가 발생했습니다. 관리자에게 문의해주세요";
			}
		} else {
			msg = "로그인이 필요한 서비스입니다.";
		}
		return msg;
	}		
	//----------------------------합격 인증샷---------------------------------------------	
	/**
	 * 합격 인증샷
	 */	
	@GetMapping("/admin/board/ad_proof_shot")
	public void adProofShot( Model model) {
	}		
	
	/**
	 * 합격 인증샷 리스트
	 * @param BRD_CTG 카테고리 5
	 * @param SCH     검색어
	 */
	@GetMapping("/admin/board/proof_shot_list")
	public String adProofShotList(@RequestParam String BRD_CTG , 
								  @RequestParam(defaultValue = "1") int nSelect, 
								  @RequestParam String SCH, 
								  Model model, HttpSession session) {
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("BRD_CTG", BRD_CTG);
		
		if(session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto)session.getAttribute("m")).getUSER_NUM()+"";
			map.put("USER_NUM", USER_NUM);
		}
		map.put("SCH",SCH);
		
		try {
			List<SuccessDto> list=boardService.findProof(map);
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
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}
		return "forward:/WEB-INF/views/admin/board/proof_shot_list.jsp";
	}
	
	/**
	 * 합격 인증샷 상세
	 * @param BRD_NUM 게시물_일련번호
	 */		
	@GetMapping("/admin/board/proof_shot_detail")
	public void adProofShotDetail(@RequestParam String BRD_NUM, Model model, HttpSession session) {
		HashMap<String, Object> map = new HashMap<>();

		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			map.put("USER_NUM", USER_NUM);
			model.addAttribute("USER_NUM", USER_NUM);
		}
		map.put("BRD_NUM", BRD_NUM);

		try {
			SuccessDto dto = boardService.findProofByNum(map);
			model.addAttribute("t", dto);
			model.addAttribute("num", BRD_NUM);
		} catch (Exception e) {
			model.addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요.");
		}
		
		try {
			//댓글
			List<RpyDto> list = boardService.findRpyByNum(BRD_NUM);
			int size = list.size();
			
			model.addAttribute("list", list);
			model.addAttribute("size", size);	
			
		} catch (Exception e) {
			model.addAttribute("error2", "에러가 발생했습니다. 관리자에게 문의해주세요");
		}				
	}		
	
	/**
	 * 합격 인증샷 수정 페이지
	 * @throws Exception 
	 */		
	@GetMapping("/admin/board/proof_shot_update")
	public void adProofShotUpdateForm(HttpSession session, Model model,
									  @RequestParam String BRD_NUM) throws Exception {
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM() + "";
			model.addAttribute("USER_NUM", USER_NUM);
			
			HashMap<String, Object> map=new HashMap<>();
			map.put("BRD_NUM", BRD_NUM);
			model.addAttribute("s", boardService.findProofByNum(map));
		}	
	}		
	
	/**
	 * 합격 인증샷 게시물 수정 
	 * @param s
	 */			
	@PutMapping("/admin/proof_shot")
	@ResponseBody
	public String updateProofShot(@RequestParam(value="images",required = false) MultipartFile file ,
								  @ModelAttribute BoardDto s, 
								  @RequestParam String filename, 
								  HttpSession session,HttpServletRequest req) {
		String msg = "";
		
		if (session.getAttribute("m") != null) {
			String USER_NUM = ((UserDto) session.getAttribute("m")).getUSER_NUM();
			String BRD_NUM = s.getBRD_NUM();
			s.setUPT_USER_NUM(USER_NUM);
			s.setUPT_IP(IpUtils.getRemoteIp(req));
			
			try {
				if(file!=null) {
					//기존에 있던 파일 삭제
					int re=fileService.deleteAttachByBoard(filename, "2");
					if(re==1) {
						msg="기존 파일 삭제시 에러가 발생했습니다. 관리자에게 문의해주세요.";
						return msg;
					}
					
					HashMap<String, Object> map=new HashMap<>();
					map.put("FILE_NAME", filename);
					map.put("BRD_NUM", BRD_NUM);
					
					//수정된 파일 삽입
					FilesDto files_dto = fileService.getAttachByMultipart(file,"2");
					files_dto.setREG_USER_IP(IpUtils.getRemoteIp(req));
					files_dto.setREG_USER_NUM(USER_NUM);
					
					boardService.updateProofShoot(s, files_dto,map);
				}else {
					
				}
			} catch (Exception e) {
				msg="게시물 수정 에러가 발생했습니다. 관리자에게 문의해주세요.";
			}
			
		} else {
			msg = "로그인이 필요한 서비스입니다.";
		}
		
		return msg;
	}			
}
