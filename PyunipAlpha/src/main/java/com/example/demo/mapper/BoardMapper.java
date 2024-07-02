package com.example.demo.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.BoardStudyDto;
import com.example.demo.dto.RpyDto;
import com.example.demo.dto.SuccessDto;

@Mapper
public interface BoardMapper {
    List<String> findBoardNum();
    List<BoardDto> findBoardByView(String BRD_CTG, RowBounds rowBounds);
    List<BoardStudyDto> findBoardStudyByView(String BRD_CTG, RowBounds rowBounds);
    List<BoardDto> findBoardByCtg(HashMap<String, Object> map, RowBounds rowBounds);
    BoardDto findBoardByNum(HashMap<String, Object> map);
    List<RpyDto> findRpyByNum(String BRD_NUM);
    Integer insertRpy(HashMap<String, Object> map);
    Integer deleteRpy(HashMap<String, Object> map);
    Integer insertBoardLike(HashMap<String, Object> map);
    Integer deleteBoardLike(HashMap<String, Object> map);
    Integer addBoardLike(HashMap<String, Object> map);
    Integer minusBoardLike(HashMap<String, Object> map);
    Integer updateViewCnt(HashMap<String, Object> map);
    List<BoardDto> findBoardStudy(HashMap<String, Object> map);
    String getNextNum();
    Integer insertBoard(BoardDto dto);
    Integer deleteBoard(HashMap<String, Object> map);
    Integer updateBoard(BoardDto dto);
    List<BoardDto> findStudyByCtg(HashMap<String, Object> map, RowBounds rowBounds);
    Integer insertBoardStudy(BoardDto dto);
    BoardDto findStudyByNum(HashMap<String, Object> map);
    Integer updateBoardStudy(BoardDto dto);
    Integer deleteStudy(HashMap<String, Object> map);
    List<BoardDto> findSuccess(HashMap<String, Object> map, RowBounds rowBounds);
    BoardDto findSuccessByNum(HashMap<String, Object> map);
    Integer insertSuc(BoardDto dto);
    Integer updateSuc(BoardDto dto);
    List<SuccessDto> findProof(HashMap<String, Object> map, RowBounds rowBounds);
    SuccessDto findProofByNum(HashMap<String, Object> map);
    Integer getNextReRpyNum();
    Integer getNextRpyNum();
    Integer findBoardSize(HashMap<String, Object> map);
    String findRegUserNumByBrdNum(String BRD_NUM);
    
    //잠금
    BoardDto selectBoardForUpdate(String BRD_NUM);
    BoardStudyDto selectBoardStudyForUpdate(String BRD_NUM);
    BoardDto selectSucForUpdate(String BRD_NUM);
}
