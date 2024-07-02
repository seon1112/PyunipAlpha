package com.example.demo.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

@Component
public class ThreadPoolManager {
	
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	
	public ExecutorService getExecutorService() {
		return executorService;
	}
	
	// Bean이 소멸되기 직전 호출
	@PreDestroy
	public void onDestroy() {
		//새로운 작업을 더 이상 받지 않고, 이미 제출된 작업들을 모두 처리한 후에 종료
		executorService.shutdown(); 
		try {
		// ExecutorService가 종료될 때까지 대기. 모든 작업이 완료되기를 기다림.(60초)
		//	  - `true`: 모든 작업이 지정된 시간 내에 완료된 경우.
		//	  - `false`: 지정된 시간 내에 모든 작업이 완료되지 않은 경우.	
			if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
				//실행 중인 작업 종료
				executorService.shutdownNow();
			}
		} catch (InterruptedException e) {
			executorService.shutdownNow();
		}
	}
}
