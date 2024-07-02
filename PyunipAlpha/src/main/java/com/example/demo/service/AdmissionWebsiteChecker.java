package com.example.demo.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AdmissionWebsiteChecker {
	private final JavaMailSender mailSender;
	//대학이름 입학처url 메소드번호
	private String[][]arr= {
			{"가천대","",""},
			{"가톨릭대","",""},
			{"건국대","",""},
			{"경기대","",""},
			{"경희대","",""},
			{"고려대","https://oku.korea.ac.kr/oku/cms/FR_CON/index.do?MENU_ID=660","2"},
			{"광운대","",""},
			{"국민대","https://admission.kookmin.ac.kr/transfer/application.php","1"},
			{"단국대","",""},
			{"덕성여대","",""},
			{"동국대","",""},
			{"동덕여대","",""},
			{"명지대","https://iphak.mju.ac.kr/pages/?p=26&mj=04","1"},
			{"상명대","",""},
			{"서강대","",""},
			{"서울과기대","",""},
			{"서울시립대","",""},
			{"서울여대","",""},
			{"성균관대","https://grad.skku.edu/admission/html/transfer/guide.html",""},
			{"성신여대","",""},
			{"세종대","",""},
			{"숙명여대","",""},
			{"숭실대","",""},
			{"아주대","",""},
			{"연세대","",""},
			{"이화여대","",""},
			{"인하대","",""},
			{"중앙대","",""},
			{"한국외대","",""},
			{"한국한공대","",""},
			{"한양대","",""},
			{"홍익대","",""}
	};
	
	public AdmissionWebsiteChecker(JavaMailSender mailSender) {
		this.mailSender=mailSender;
	}
	
	@Scheduled(cron="0 0 18 * * ?")
	public void checkForUpdate() {
	    long startTime = System.currentTimeMillis();

	    CompletableFuture<Void>[] futures = Stream.of(arr)
	        .filter(univ -> !univ[1].isEmpty())
	        .map(univ -> CompletableFuture.runAsync(() -> {
	            String univ_name = univ[0];
	            String univ_url = univ[1];
	            String method = univ[2];

	            switch (method) {
	                case "1": {
	                    checkMethod1(univ_url, univ_name); break;
	                }
	                case "2" :{
	                	checkMethod2(univ_url, univ_name); break;
	                }
	            }
	        }))
	        .toArray(CompletableFuture[]::new);
	    
	    CompletableFuture<Void> allOf = CompletableFuture.allOf(futures);
	    
	    try {
	        allOf.get(); 
	    } catch (InterruptedException | ExecutionException e) {
	        e.printStackTrace();
	    }

	    long endTime = System.currentTimeMillis();
	    System.out.println("총 실행 시간: " + (endTime - startTime) + " ms");//총 실행 시간: 3488 ms
	}
	
	/**
	 * SELECT OPTION에 2025년이 있는지 확인하는 메소드
	 * @param univ_url  입학처 URL
	 * @param univ_name 대학이름
	 */		
	private void checkMethod1(String univ_url, String univ_name) {
		try {
			Document doc=Jsoup.connect(univ_url).timeout(10*1000).get(); //timeout 설정
			Elements options=doc.select("option");
			for(Element option:options) {
				if(option.text().contains("2025")) {
					sendMail(univ_url,univ_name);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * SELECT OPTION에 2025년이 있는지 확인하는 메소드
	 * @param univ_url  입학처 URL
	 * @param univ_name 대학이름
	 */		
	private void checkMethod2(String univ_url, String univ_name) {
		try {
			System.out.println("univ_name:"+univ_name);
			Document doc=Jsoup.connect(univ_url).timeout(10*1000).get(); //timeout 설정
			Elements options=doc.getElementsByTag("script");
			for(Element option : options) {
				if(option.getElementsContainingText("2025 편입") != null) {
					sendMail(univ_url,univ_name);
					break;
				}
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 메일전송
	 * @param univ_url  입학처 URL
	 * @param univ_name 대학이름
	 */	
	private void sendMail(String univ_url, String univ_name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("");
        message.setSubject("새로운 입학년도 추가됨");
        message.setText(univ_url+":"+univ_name+" 2025학년도 옵션이 입학처 홈페이지에 추가되었습니다.");
        
        try {
        	mailSender.send(message);
		} catch (Exception e) {
			System.out.println("이메일 유효성 예외발생: "+e.getMessage());
		}
	}
}
