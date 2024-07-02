package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.BoardStudyDto;
import com.example.demo.dto.FilesDto;
import com.example.demo.dto.RpyDto;
import com.example.demo.dto.SuccessDto;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.FilesRepository;
import com.example.demo.util.EncryptionUtil;

@Service
public class BoardService {
	@Autowired
	private BoardRepository boardRpt;
	@Autowired
	private FilesService filesService;
    @Value("${encryption.key}")
    private String encryptionKey;
	
    public List<String> findBoardNum() throws Exception {
        List<String> list = boardRpt.findBoardNum();
        
        List<String> encryptedList = new ArrayList<>();
        for (String num : list) {
            String encryptedNum = EncryptionUtil.encrypt(num, encryptionKey);
            encryptedList.add(encryptedNum);
        }
        return encryptedList;
    }   
	
    public List<BoardDto> findBoardByView(String BRD_CTG) throws Exception {
    	List<BoardDto> list=boardRpt.findBoardByView(BRD_CTG);
    	for(BoardDto dto:list) {
    		dto.setBRD_NUM(EncryptionUtil.encrypt(dto.getBRD_NUM(), encryptionKey));
    	}
        return list;
    }
    
	public List<BoardStudyDto> findBoardStudyByView(String BRD_CTG) throws Exception {
    	List<BoardStudyDto> list=boardRpt.findBoardStudyByView(BRD_CTG);
    	for(BoardStudyDto dto:list) {
    		dto.setBRD_NUM(EncryptionUtil.encrypt(dto.getBRD_NUM(), encryptionKey));
    	}
        return list;
	}
	
    public List<BoardDto> findBoardByCtg(HashMap<String, Object> map) throws Exception {
    	String USER_NUM=(String)map.get("USER_NUM");
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		String ENCRY_USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		map.put("USER_NUM", ENCRY_USER_NUM);
    	}
    	
    	List<BoardDto> list=boardRpt.findBoardByCtg(map);
    	for(BoardDto dto:list) {
    		dto.setBRD_NUM(EncryptionUtil.encrypt(dto.getBRD_NUM(), encryptionKey));
    		dto.setREG_USER_NUM(EncryptionUtil.encrypt(dto.getREG_USER_NUM(), encryptionKey));
    	}
    	
    	map.put("USER_NUM", USER_NUM);
    	 return list;
    }
    
	public BoardDto findBoardByNum(HashMap<String, Object> map) throws Exception {
    	String USER_NUM=(String)map.get("USER_NUM");
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		String ENCRY_USER_NUM =EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		map.put("USER_NUM", ENCRY_USER_NUM);
    	}
    	
    	String BRD_NUM=(String)map.get("BRD_NUM");
    	if(BRD_NUM != null && !BRD_NUM.equals("")) {
    		String ENCRY_BRD_NUM=EncryptionUtil.decrypt(BRD_NUM, encryptionKey);
    		map.put("BRD_NUM", ENCRY_BRD_NUM);
    	}
    	
		BoardDto dto=boardRpt.findBoardByNum(map);
		dto.setBRD_NUM(EncryptionUtil.encrypt(dto.getBRD_NUM(), encryptionKey));
		dto.setREG_USER_NUM(EncryptionUtil.encrypt(dto.getREG_USER_NUM(), encryptionKey));
		map.put("USER_NUM", USER_NUM);
		map.put("BRD_NUM", BRD_NUM);
		
		return dto;
	}
	
	public List<RpyDto> findRpyByNum(String BRD_NUM) throws Exception{
    	if(BRD_NUM != null && !BRD_NUM.equals("")) {
    		BRD_NUM=EncryptionUtil.decrypt(BRD_NUM, encryptionKey);
    	}
    	
    	List<RpyDto> list=boardRpt.findRpyByNum(BRD_NUM);
    	for(RpyDto dto:list) {
    		dto.setREG_USER_NUM(EncryptionUtil.encrypt(dto.getREG_USER_NUM(), encryptionKey));
    	}
		return list;
	}
	
	@Transactional
	public Integer insertRpy(HashMap<String, Object> map) throws Exception {
		int nextNum = boardRpt.getNextRpyNum();
		map.put("NEXT_RPY_NUM", nextNum);
		
    	String REG_USER_NUM=(String)map.get("REG_USER_NUM");
    	if(REG_USER_NUM != null && !REG_USER_NUM.equals("")) {
    		REG_USER_NUM=EncryptionUtil.decrypt(REG_USER_NUM, encryptionKey);
    		map.put("REG_USER_NUM", REG_USER_NUM);
    	}
    	
    	String BRD_NUM=(String)map.get("BRD_NUM");
    	if(BRD_NUM != null && !BRD_NUM.equals("")) {
    		BRD_NUM=EncryptionUtil.decrypt(BRD_NUM, encryptionKey);
    		map.put("BRD_NUM", BRD_NUM);
    	}
    	
		return boardRpt.insertRpy(map);
	}
	
	@Transactional
	public Integer insertReRpy(HashMap<String, Object> map) throws Exception {
		int nextNum=boardRpt.getNextReRpyNum();
		map.put("NEXT_RE_RPY_NUM", nextNum);
		
    	String REG_USER_NUM=(String)map.get("REG_USER_NUM");
    	if(REG_USER_NUM != null && !REG_USER_NUM.equals("")) {
    		REG_USER_NUM=EncryptionUtil.decrypt(REG_USER_NUM, encryptionKey);
    		map.put("REG_USER_NUM", REG_USER_NUM);
    	}
    	
    	String BRD_NUM=(String)map.get("BRD_NUM");
    	if(BRD_NUM != null && !BRD_NUM.equals("")) {
    		BRD_NUM=EncryptionUtil.decrypt(BRD_NUM, encryptionKey);
    		map.put("BRD_NUM", BRD_NUM);
    	}
    	
		return boardRpt.insertRpy(map);
	}
	
	public Integer deleteRpy(HashMap<String, Object> map) {
		return boardRpt.deleteRpy(map);
	}
	
	@Transactional
	public void insertBoardLike(HashMap<String, Object> map) throws Exception {
    	String USER_NUM=(String)map.get("USER_NUM");
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		map.put("USER_NUM", USER_NUM);
    	}
    	
    	String BRD_NUM=(String)map.get("BRD_NUM");
    	if(BRD_NUM != null && !BRD_NUM.equals("")) {
    		BRD_NUM=EncryptionUtil.decrypt(BRD_NUM, encryptionKey);
    		map.put("BRD_NUM", BRD_NUM);
    	}
    	
		//좋아요 목록에 추가
		boardRpt.insertBoardLike(map);
		//게시판 좋아요 개수 +1
		boardRpt.addBoardLike(map);
	}
	
	@Transactional
	public void deleteBoardLike(HashMap<String, Object> map) throws Exception {
    	String USER_NUM=(String)map.get("USER_NUM");
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		map.put("USER_NUM", USER_NUM);
    	}
    	
    	String BRD_NUM=(String)map.get("BRD_NUM");
    	if(BRD_NUM != null && !BRD_NUM.equals("")) {
    		BRD_NUM=EncryptionUtil.decrypt(BRD_NUM, encryptionKey);
    		map.put("BRD_NUM", BRD_NUM);
    	}
    	
		//좋아요 목록에서 삭제
		boardRpt.deleteBoardLike(map);
		//게시판 좋아요 개수 -1
		boardRpt.minusBoardLike(map);
	}
	
	public void updateViewCnt(HashMap<String, Object> map) throws Exception {
    	String BRD_NUM=(String)map.get("BRD_NUM");
    	if(BRD_NUM != null && !BRD_NUM.equals("")) {
    		String ENCRY_BRD_NUM=EncryptionUtil.decrypt(BRD_NUM, encryptionKey);
    		map.put("BRD_NUM", ENCRY_BRD_NUM);
    	}
		boardRpt.updateViewCnt(map);
		map.put("BRD_NUM", BRD_NUM);
	}
	
//	public List<BoardDto> findBoardStudy(HashMap<String, Object> map){
//		return boardRpt.findBoardStudy(map);
//	}
//	
	@Transactional
	public Integer insertBoard(BoardDto dto) throws Exception {
		String BRD_NUM=boardRpt.getNextNum();
		dto.setBRD_NUM(BRD_NUM);
		
		String USER_NUM=dto.getREG_USER_NUM();
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		dto.setREG_USER_NUM(USER_NUM);
    	}
		
		return boardRpt.insertBoard(dto);
	}
	
	public Integer deleteBoard(HashMap<String, Object> map) throws Exception {
    	String BRD_NUM=(String)map.get("BRD_NUM");
    	if(BRD_NUM != null && !BRD_NUM.equals("")) {
    		BRD_NUM=EncryptionUtil.decrypt(BRD_NUM, encryptionKey);
    		map.put("BRD_NUM", BRD_NUM);
    	}
    	
		return boardRpt.deleteBoard(map);
	}
	
	@Transactional
	public void updateBoard(BoardDto dto) throws Exception {
		String USER_NUM=dto.getREG_USER_NUM();
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		dto.setREG_USER_NUM(USER_NUM);
    	}
    	
    	String BRD_NUM=dto.getBRD_NUM();
    	if(BRD_NUM != null && !BRD_NUM.equals("")) {
    		BRD_NUM=EncryptionUtil.decrypt(BRD_NUM, encryptionKey);
    		dto.setBRD_NUM(BRD_NUM);
    	}
		
		String UPT_USER_NUM=dto.getUPT_USER_NUM();
    	if(UPT_USER_NUM != null && !UPT_USER_NUM.equals("")) {
    		UPT_USER_NUM=EncryptionUtil.decrypt(UPT_USER_NUM, encryptionKey);
    		dto.setUPT_USER_NUM(UPT_USER_NUM);
    	}
		//데이터 잠금
		boardRpt.selectBoardForUpdate(dto.getBRD_NUM());
		// 데이터 업데이트
		boardRpt.updateBoard(dto);
	}
	
	public List<BoardDto> findStudyByCtg(HashMap<String, Object> map) throws Exception{
    	String USER_NUM=(String)map.get("USER_NUM");
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		String ENCRY_USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		map.put("USER_NUM", ENCRY_USER_NUM);
    	}
    	
    	List<BoardDto> list=boardRpt.findStudyByCtg(map);
    	for(BoardDto dto:list) {
    		dto.setBRD_NUM(EncryptionUtil.encrypt(dto.getBRD_NUM(), encryptionKey));
    		dto.setREG_USER_NUM(EncryptionUtil.encrypt(dto.getREG_USER_NUM(), encryptionKey));
    	}
    	
    	map.put("USER_NUM", USER_NUM);
		return list;
	}
	
	@Transactional
	public void insertBoardStudy(BoardDto dto) throws Exception {
		String BRD_NUM=boardRpt.getNextNum();
		dto.setBRD_NUM(BRD_NUM);
		
		String USER_NUM=dto.getREG_USER_NUM();
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		dto.setREG_USER_NUM(USER_NUM);
    	}
		
		int re=boardRpt.insertBoard(dto);
		
		if(re==1) {
			boardRpt.insertBoardStudy(dto);
		}
	}
	
	public BoardDto findStudyByNum(HashMap<String, Object> map) throws Exception {
    	String USER_NUM=(String)map.get("USER_NUM");
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		String ENCRY_USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		map.put("USER_NUM", ENCRY_USER_NUM);
    	}
    	
    	String BRD_NUM=(String)map.get("BRD_NUM");
    	if(BRD_NUM != null && !BRD_NUM.equals("")) {
    		String ENCRY_BRD_NUM=EncryptionUtil.decrypt(BRD_NUM, encryptionKey);
    		map.put("BRD_NUM", ENCRY_BRD_NUM);
    	}
    	
		BoardDto dto=boardRpt.findStudyByNum(map);
		dto.setBRD_NUM(EncryptionUtil.encrypt(dto.getBRD_NUM(), encryptionKey));
		dto.setREG_USER_NUM(EncryptionUtil.encrypt(dto.getREG_USER_NUM(), encryptionKey));
		map.put("USER_NUM", USER_NUM);
		map.put("BRD_NUM", BRD_NUM);
		
		return dto;
	}
	
	@Transactional
	public void updateBoardStudy(BoardDto dto) throws Exception {
		String USER_NUM=dto.getREG_USER_NUM();
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		dto.setREG_USER_NUM(USER_NUM);
    	}
    	
    	String BRD_NUM=dto.getBRD_NUM();
    	if(BRD_NUM != null && !BRD_NUM.equals("")) {
    		BRD_NUM=EncryptionUtil.decrypt(BRD_NUM, encryptionKey);
    		dto.setBRD_NUM(BRD_NUM);
    	}
		
		String UPT_USER_NUM=dto.getUPT_USER_NUM();
    	if(UPT_USER_NUM != null && !UPT_USER_NUM.equals("")) {
    		UPT_USER_NUM=EncryptionUtil.decrypt(UPT_USER_NUM, encryptionKey);
    		dto.setUPT_USER_NUM(UPT_USER_NUM);
    	}
    	
		//board수정
		boardRpt.selectBoardForUpdate(dto.getBRD_NUM());
		boardRpt.updateBoard(dto);
		
		//boardstudy수정
		boardRpt.selectBoardStudyForUpdate(dto.getBRD_NUM());
		boardRpt.updateBoardStudy(dto);
	}
	
	public List<BoardDto> findSuccess(HashMap<String, Object> map) throws Exception{
    	String USER_NUM=(String)map.get("USER_NUM");
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		String ENCRY_USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		map.put("USER_NUM", ENCRY_USER_NUM);
    	}
    	
    	List<BoardDto> list= boardRpt.findSuccess(map);
    	for(BoardDto dto:list) {
    		dto.setBRD_NUM(EncryptionUtil.encrypt(dto.getBRD_NUM(), encryptionKey));
    		dto.setREG_USER_NUM(EncryptionUtil.encrypt(dto.getREG_USER_NUM(), encryptionKey));
    	}
    	
    	map.put("USER_NUM", USER_NUM);
		return list; 
	}
	
	public BoardDto findSuccessByNum(HashMap<String, Object> map) throws Exception{
    	String USER_NUM=(String)map.get("USER_NUM");
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		map.put("USER_NUM", USER_NUM);
    	}
    	
    	String BRD_NUM=(String)map.get("BRD_NUM");
    	if(BRD_NUM != null && !BRD_NUM.equals("")) {
    		BRD_NUM=EncryptionUtil.decrypt(BRD_NUM, encryptionKey);
    		map.put("BRD_NUM", BRD_NUM);
    	}
    	
		BoardDto dto=boardRpt.findSuccessByNum(map);
		dto.setBRD_NUM(EncryptionUtil.encrypt(dto.getBRD_NUM(), encryptionKey));
		dto.setREG_USER_NUM(EncryptionUtil.encrypt(dto.getREG_USER_NUM(), encryptionKey));
		return dto;
	}
	
	@Transactional
	public void insertSuc(BoardDto dto) throws Exception {
		String BRD_NUM=boardRpt.getNextNum();
		dto.setBRD_NUM(BRD_NUM);
		
		String USER_NUM=dto.getREG_USER_NUM();
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		dto.setREG_USER_NUM(USER_NUM);
    	}
		
		int re=boardRpt.insertBoard(dto);
		
		if(re==1) {
			boardRpt.insertSuc(dto);
		}
	}
	
	@Transactional
	public void updateSuc(BoardDto dto) throws Exception {
		String USER_NUM=dto.getREG_USER_NUM();
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		dto.setREG_USER_NUM(USER_NUM);
    	}
    	
    	String BRD_NUM=dto.getBRD_NUM();
    	if(BRD_NUM != null && !BRD_NUM.equals("")) {
    		BRD_NUM=EncryptionUtil.decrypt(BRD_NUM, encryptionKey);
    		dto.setBRD_NUM(BRD_NUM);
    	}
		
		String UPT_USER_NUM=dto.getUPT_USER_NUM();
    	if(UPT_USER_NUM != null && !UPT_USER_NUM.equals("")) {
    		UPT_USER_NUM=EncryptionUtil.decrypt(UPT_USER_NUM, encryptionKey);
    		dto.setUPT_USER_NUM(UPT_USER_NUM);
    	}
    	
		//board수정
		boardRpt.selectBoardForUpdate(dto.getBRD_NUM());
		boardRpt.updateBoard(dto);
		//suc수정
		boardRpt.selectSucForUpdate(dto.getBRD_NUM());
		boardRpt.updateSuc(dto);
	}
	
	public List<SuccessDto> findProof(HashMap<String, Object> map) throws Exception{
    	String USER_NUM=(String)map.get("USER_NUM");
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		String ENCRY_USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		map.put("USER_NUM", ENCRY_USER_NUM);
    	}
    	
    	List<SuccessDto> list=boardRpt.findProof(map);
    	for(SuccessDto dto:list) {
    		dto.setBRD_NUM(EncryptionUtil.encrypt(dto.getBRD_NUM(), encryptionKey));
    		dto.setREG_USER_NUM(EncryptionUtil.encrypt(dto.getREG_USER_NUM(), encryptionKey));
    	}
    	map.put("USER_NUM", USER_NUM);
		return list; 
	}
	
	public SuccessDto findProofByNum(HashMap<String, Object> map) throws Exception {
    	String USER_NUM=(String)map.get("USER_NUM");
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		map.put("USER_NUM", USER_NUM);
    	}
    	
    	String BRD_NUM=(String)map.get("BRD_NUM");
    	if(BRD_NUM != null && !BRD_NUM.equals("")) {
    		BRD_NUM=EncryptionUtil.decrypt(BRD_NUM, encryptionKey);
    		map.put("BRD_NUM", BRD_NUM);
    	}
    	
    	SuccessDto dto=boardRpt.findProofByNum(map);
		dto.setBRD_NUM(EncryptionUtil.encrypt(dto.getBRD_NUM(), encryptionKey));
		dto.setREG_USER_NUM(EncryptionUtil.encrypt(dto.getREG_USER_NUM(), encryptionKey));
		
		return dto;
	}
	
	public Integer findBoardSize(HashMap<String, Object> map) throws Exception {
    	String USER_NUM=(String)map.get("USER_NUM");
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		String ENCRY_USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		map.put("USER_NUM", ENCRY_USER_NUM);
    	}
    	
    	map.put("USER_NUM", USER_NUM);
		return boardRpt.findBoardSize(map);
	}
	
	public String findRegUserNumByBrdNum(String BRD_NUM) throws Exception {
    	if(BRD_NUM != null && !BRD_NUM.equals("")) {
    		BRD_NUM=EncryptionUtil.decrypt(BRD_NUM, encryptionKey);
    	}
    	
    	String REG_USER_NUM=EncryptionUtil.encrypt(boardRpt.findRegUserNumByBrdNum(BRD_NUM), encryptionKey);
		return REG_USER_NUM;
	}	
	
	@Transactional
	public void insertProofShot(BoardDto board_dto,FilesDto files_dto) throws Exception {
		String BRD_NUM=boardRpt.getNextNum();
		board_dto.setBRD_NUM(BRD_NUM);
		
		String USER_NUM=board_dto.getREG_USER_NUM();
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		board_dto.setREG_USER_NUM(USER_NUM);
    	}
    	
		//게시물 추가
		int re=boardRpt.insertBoard(board_dto);
		//파일 추가
		if(re==1) {
			files_dto.setBRD_NUM(BRD_NUM);
			files_dto.setREG_USER_NUM(USER_NUM);
			filesService.insertFiles(files_dto);
		}
	}
	
	@Transactional
	public void updateProofShoot(BoardDto board_dto,FilesDto files_dto,HashMap<String, Object> map) throws Exception {
		String USER_NUM=board_dto.getREG_USER_NUM();
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		board_dto.setREG_USER_NUM(USER_NUM);
    		files_dto.setREG_USER_NUM(USER_NUM);
    	}
    	
    	String BRD_NUM=board_dto.getBRD_NUM();
    	if(BRD_NUM != null && !BRD_NUM.equals("")) {
    		BRD_NUM=EncryptionUtil.decrypt(BRD_NUM, encryptionKey);
    		board_dto.setBRD_NUM(BRD_NUM);
    		files_dto.setBRD_NUM(BRD_NUM);
    		map.put("BRD_NUM", BRD_NUM);
    	}
		
		String UPT_USER_NUM=board_dto.getUPT_USER_NUM();
    	if(UPT_USER_NUM != null && !UPT_USER_NUM.equals("")) {
    		UPT_USER_NUM=EncryptionUtil.decrypt(UPT_USER_NUM, encryptionKey);
    		board_dto.setUPT_USER_NUM(UPT_USER_NUM);
    	}
    	
		//게시물 수정
		boardRpt.updateBoard(board_dto);
		//기존 파일 삭제
		filesService.deleteFilesByName(map);
		//새로운 파일 추가
		filesService.insertFiles(files_dto);
	}
	
	@Transactional
	public void insertBoardWithFile(BoardDto board_dto, List<FilesDto> files_list) throws Exception {
		String BRD_NUM=boardRpt.getNextNum();
		board_dto.setBRD_NUM(BRD_NUM);
		String USER_NUM=board_dto.getREG_USER_NUM();
		
    	if(USER_NUM != null && !USER_NUM.equals("")) {
    		USER_NUM=EncryptionUtil.decrypt(USER_NUM, encryptionKey);
    		board_dto.setREG_USER_NUM(USER_NUM);
    	}
		
		//게시물 추가
		int re=boardRpt.insertBoard(board_dto);
		//파일 추가
		if(re==1) {
			for(FilesDto dto : files_list) {
				dto.setBRD_NUM(BRD_NUM);
				dto.setREG_USER_NUM(USER_NUM);
				filesService.insertFiles(dto);
			}
		}		
	}
}
