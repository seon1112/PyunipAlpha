package com.example.demo.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.UserDto;
import com.example.demo.repository.BoardRepository;
import com.example.demo.service.LogService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

//Mockito는 Java에서 모킹(mocking)을 통해 단위 테스트를 쉽게 작성할 수 있도록 돕는 프레임워크
//모킹을 통해 실제 객체 대신 가짜 객체를 사용하여 의존성을 분리하고, 특정 메서드 호출하여 검증
@ExtendWith(MockitoExtension.class) // Mockito와 JUnit을 함께 사용
public class BoardTestController {
    @Mock
    private BoardRepository boardRept; // 모킹할 객체
    @Mock
    private LogService errorService; 
    @Mock
    private HttpServletRequest request; 
    @Mock
    private HttpSession session; 
    @Mock
    private Model model;
    
    @InjectMocks
    private BoardController test;  // 실제 테스트할 객체, 의존성을 모킹 객체로 주입
    
    private HashMap<String, Object> map;
    
    @BeforeEach
    public void setUp() {
        map = new HashMap<>();
    }

    @Test
    public void testBoardTalkDetail() {
    	UserDto user=new UserDto();
    	user.setUSER_NUM("1");
    	when(session.getAttribute("m")).thenReturn(user);
    	
    	when(boardRept.findBoardByNum(any(HashMap.class))).thenReturn(new BoardDto());
    	verify(boardRept).updateViewCnt(any(HashMap.class));
    	verify(model).addAttribute(eq("t"),new BoardDto());
    	verify(model).addAttribute(eq("num"),anyInt());
    	verify(model).addAttribute(eq("nSelect"),anyInt());
    }
    
    @Test
    public void testBoardTalkList() throws Exception {
        // Given 테스트 조건
        String BRD_CTG = "testCategory";
        String SCH = "testSearch";
        String DV = "testDV";
        int nSelect = 1;
        String MINE = "N";
        String HEART = "N";
        
        UserDto user = new UserDto();
        user.setUSER_NUM("1");
        when(session.getAttribute("m")).thenReturn(user); //세션 정보를 찾으면 내가 가상으로 만든 user를 리턴한다
        
        when(boardRept.findBoardByCtg(any(HashMap.class))).thenReturn(List.of(new BoardDto()));
        
        // When 테스트하려는 메서드 호출
        String result = test.boardTalkList(BRD_CTG, SCH, DV, nSelect, MINE, HEART, model, session, request);

        // Then
        assertEquals("forward:/WEB-INF/views/board/talk_list.jsp", result);
        //boardRept.findBoardByCtg(map)
        verify(boardRept, times(2)).findBoardByCtg(any(HashMap.class));
        //model.addAttribute("talkList", list);
        verify(model).addAttribute(eq("talkList"), anyList());
        //model.addAttribute("size", nMaxRecordCnt);
        verify(model).addAttribute(eq("size"), anyInt());
        verify(model).addAttribute(eq("nMaxVCnt"), eq(10));
        verify(model).addAttribute(eq("nMaxRecordCnt"), anyInt());
    }

    @Test
    public void testBoardTalkList_withException() throws Exception {
        String BRD_CTG = "testCategory";
        String SCH = "testSearch";
        String DV = "testDV";
        int nSelect = 1;
        String MINE = "N";
        String HEART = "N";
        UserDto user = new UserDto();
        user.setUSER_NUM("1");
        when(session.getAttribute("m")).thenReturn(user);
        
        // boardRept.findBoardByCtg 메서드가 호출될 때 RuntimeException을 던지도록 설정
        when(boardRept.findBoardByCtg(any(HashMap.class))).thenThrow(new RuntimeException("Test Exception"));
        
        // When
        String result = test.boardTalkList(BRD_CTG, SCH, DV, nSelect, MINE, HEART, model, session, request);

        // Then
        assertEquals("forward:/WEB-INF/views/board/talk_list.jsp", result);
	    verify(errorService).insertErrorLog(eq(request), eq(session), eq("Test Exception"));
	    verify(model).addAttribute("error", "에러가 발생했습니다. 관리자에게 문의해주세요");
    }
    
    /**
     * - `assertEquals`: 두 값이 같은지 확인한다. 첫 번째 인자는 기대하는 값이고, 두 번째 인자는 실제 결과 값이다.
	   - `verify`: 특정 메서드가 예상대로 호출되었는지 확인한다. 첫 번째 인자는 모킹된 객체이고, 호출될 메서드와 인자들을 포함한다. 두 번째 인자는 호출 횟수이다.
	   - `any`, `eq`: Mockito의 ArgumentMatchers. `any(Class<T> type)`는 어떤 인자라도 매칭하고, `eq(T value)`는 정확히 그 값과 매칭한다.
	   - anyMap(): `Map` 타입의 어떤 객체에도 매칭
     * 
     * */
}
