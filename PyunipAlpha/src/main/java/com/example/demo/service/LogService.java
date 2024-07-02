package com.example.demo.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AcssLogDto;
import com.example.demo.dto.ErrorLogDto;
import com.example.demo.dto.UserDto;
import com.example.demo.repository.LogRepository;
import com.example.demo.util.EncryptionUtil;
import com.example.demo.util.IpUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class LogService {
	@Autowired
	private LogRepository logRept;
    @Value("${encryption.key}")
    private String encryptionKey;
	
	
	/** 
	 * 에러로그 
	 * @param error 에러사유
	 * @throws Exception 
	 */
	public void insertErrorLog(HttpServletRequest request,HttpSession session,String error) throws Exception {
		ErrorLogDto dto=new ErrorLogDto();
		if(session.getAttribute("m")!=null) {
			String USER_NUM=((UserDto) session.getAttribute("m")).getUSER_NUM();
			if(USER_NUM != null && !USER_NUM.equals("")) {
				dto.setUSER_NUM(EncryptionUtil.decrypt(USER_NUM, encryptionKey));
			}
		}
		String url=request.getRequestURL().toString();
		dto.setERROR_URL(url);
		dto.setERROR(error);
		dto.setERROR_NUM(logRept.getNextErrorNum());
		logRept.insertErrorLog(dto);
	}
	
	/** 
	 * 에러로그 
	 * @param error 에러사유
	 */
	public void insertErrorLog2(ErrorLogDto dto) {
		dto.setERROR_NUM(logRept.getNextErrorNum());
		logRept.insertErrorLog(dto);
	}

	/** 
	 * 로그 저장 
	 * @param logNote 로그내용
	 * @throws Exception 
	 */
    public Integer saveLog(UserDto m, String logNote, HttpServletRequest req) throws Exception {
        int re = 0;
        String remoteIp = IpUtils.getIpFromRequest(req);
        String userName = m.getUSER_NM();
        String userNum = m.getUSER_NUM();

        HashMap<String, Object> map = new HashMap<>();
		if(userNum!=null) {
			map.put("USER_NUM", EncryptionUtil.decrypt(userNum, encryptionKey));
		}
        map.put("ACSS_IP", remoteIp);
        map.put("USER_NM", userName);
        map.put("ACSS_LOG_NUM", logRept.getNextAcssLogNum());
        map.put("LOG", logNote);
        re = logRept.insertAcssLog(map);

        return re;
    }
    
	public Integer insertAcssLog(HashMap<String, Object> map) {
		return logRept.insertAcssLog(map);
	}
	
    @Transactional
	public Integer insertAcssFailLog(HashMap<String, Object> map) {
    	map.put("ACSS_FAIL_NUM", logRept.getNextAcssFailNum());
		return logRept.insertAcssFailLog(map);
	}
	
	public List<AcssLogDto> findAcssLog(HashMap<String, Object> map){
		return logRept.findAcssLog(map);
	}
}
