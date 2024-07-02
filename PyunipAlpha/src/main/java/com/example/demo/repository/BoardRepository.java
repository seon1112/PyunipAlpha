package com.example.demo.repository;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.BoardStudyDto;
import com.example.demo.dto.RpyDto;
import com.example.demo.dto.SuccessDto;
import com.example.demo.mapper.BoardMapper;

@Repository
public class BoardRepository {
    @Autowired
    private BoardMapper boardMapper;
    
	public List<String> findBoardNum(){
		return boardMapper.findBoardNum();
	}
	
    public List<BoardDto> findBoardByView(String BRD_CTG) {
        RowBounds rowBounds = new RowBounds(0, 4);
        return boardMapper.findBoardByView(BRD_CTG, rowBounds);
    }
    
	public List<BoardStudyDto> findBoardStudyByView(String BRD_CTG) {
		RowBounds rowBounds = new RowBounds(0, 4);	
		return  boardMapper.findBoardStudyByView(BRD_CTG , rowBounds);
	}
	
    public List<BoardDto> findBoardByCtg(HashMap<String, Object> map) {
        if (map.get("nMaxVCnt") != null) {
            int pageSize = (int) map.get("nMaxVCnt");
            int pageNumber = (int) map.get("nSelectPage");
            int offset = (pageNumber - 1) * pageSize;
            RowBounds rowBounds = new RowBounds(offset, pageSize);
            return boardMapper.findBoardByCtg(map, rowBounds);
        } else {
            return boardMapper.findBoardByCtg(map, RowBounds.DEFAULT);
        }
    }
    
	public BoardDto findBoardByNum(HashMap<String, Object> map) {
		return boardMapper.findBoardByNum(map);
	}
	
	public List<RpyDto> findRpyByNum(String BRD_NUM){
		return boardMapper.findRpyByNum(BRD_NUM);
	}
	
	public Integer insertRpy(HashMap<String, Object> map) {
		return boardMapper.insertRpy(map);
	}
	
	public Integer insertReRpy(HashMap<String, Object> map) {
		return boardMapper.insertRpy(map);
	}
	
	public Integer deleteRpy(HashMap<String, Object> map) {
		return boardMapper.deleteRpy(map);
	}
	
	public Integer insertBoardLike(HashMap<String, Object> map) {
		return boardMapper.insertBoardLike(map);
	}
	
	public Integer deleteBoardLike(HashMap<String, Object> map) {
		return boardMapper.deleteBoardLike(map);
	}
	
	public Integer addBoardLike(HashMap<String, Object> map) {
		return boardMapper.addBoardLike(map);
	}
	
	public Integer minusBoardLike(HashMap<String, Object> map) {
		return boardMapper.minusBoardLike(map);
	}
	
	public Integer updateViewCnt(HashMap<String, Object> map) {
		return boardMapper.updateViewCnt(map);
	}
	
	public List<BoardDto> findBoardStudy(HashMap<String, Object> map){
		return boardMapper.findBoardStudy(map);
	}
	
	public String getNextNum() {
		return boardMapper.getNextNum();
	}
	
	public Integer insertBoard(BoardDto dto) {
		return boardMapper.insertBoard(dto);
	}
	
	public Integer deleteBoard(HashMap<String, Object> map) {
		return boardMapper.deleteBoard(map);
	}
	
	public void updateBoard(BoardDto dto) {
		boardMapper.updateBoard(dto);
	}
	
	public List<BoardDto> findStudyByCtg(HashMap<String, Object> map){
        if (map.get("nMaxVCnt") != null) {
            int pageSize = (int) map.get("nMaxVCnt");
            int pageNumber = (int) map.get("nSelectPage");
            int offset = (pageNumber - 1) * pageSize;
            RowBounds rowBounds = new RowBounds(offset, pageSize);
            return boardMapper.findStudyByCtg(map, rowBounds);
        } else {
            return boardMapper.findStudyByCtg(map, RowBounds.DEFAULT);
        }
	}
	
	public void insertBoardStudy(BoardDto dto) {
		boardMapper.insertBoardStudy(dto);
	}
	
	public BoardDto findStudyByNum(HashMap<String, Object> map) {
		return boardMapper.findStudyByNum(map);
	}
	
	public void updateBoardStudy(BoardDto dto) {
		boardMapper.updateBoardStudy(dto);
	}
	
	public Integer deleteStudy(HashMap<String, Object> map) {
		return boardMapper.deleteStudy(map);
	}
	
	public List<BoardDto> findSuccess(HashMap<String, Object> map){
        if (map.get("nMaxVCnt") != null) {
            int pageSize = (int) map.get("nMaxVCnt");
            int pageNumber = (int) map.get("nSelectPage");
            int offset = (pageNumber - 1) * pageSize;
            RowBounds rowBounds = new RowBounds(offset, pageSize);
            return boardMapper.findSuccess(map, rowBounds);
        } else {
            return boardMapper.findSuccess(map, RowBounds.DEFAULT);
        }
	}
	
	public BoardDto findSuccessByNum(HashMap<String, Object> map){
		return boardMapper.findSuccessByNum(map);
	}
	
	public void insertSuc(BoardDto dto) {
		boardMapper.insertSuc(dto);
	}
	
	public void updateSuc(BoardDto dto) {
		boardMapper.updateSuc(dto);
	}
	
	public List<SuccessDto> findProof(HashMap<String, Object> map){
        if (map.get("nMaxVCnt") != null) {
            int pageSize = (int) map.get("nMaxVCnt");
            int pageNumber = (int) map.get("nSelectPage");
            int offset = (pageNumber - 1) * pageSize;
            RowBounds rowBounds = new RowBounds(offset, pageSize);
            return boardMapper.findProof(map, rowBounds);
        } else {
            return boardMapper.findProof(map, RowBounds.DEFAULT);
        }
	}
	
	public SuccessDto findProofByNum(HashMap<String, Object> map) {
		return boardMapper.findProofByNum(map);
	}
	
	public Integer getNextReRpyNum() {
		return boardMapper.getNextReRpyNum();
	}
	
	public Integer getNextRpyNum() {
		return boardMapper.getNextRpyNum();
	}
	
	public Integer findBoardSize(HashMap<String, Object> map) {
		return boardMapper.findBoardSize(map);
	}
	
	public String findRegUserNumByBrdNum(String BRD_NUM) {
		return boardMapper.findRegUserNumByBrdNum(BRD_NUM);
	}
	
    public void selectBoardStudyForUpdate(String BRD_NUM) {
        boardMapper.selectBoardStudyForUpdate(BRD_NUM);
    }
    
    public void selectSucForUpdate(String BRD_NUM) {
        boardMapper.selectSucForUpdate(BRD_NUM);
    }	
	
    public void selectBoardForUpdate(String BRD_NUM) {
        boardMapper.selectBoardForUpdate(BRD_NUM);
    }    
}
