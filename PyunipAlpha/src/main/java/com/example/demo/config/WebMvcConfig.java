package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	//web root가 아닌 외부 경로에 있는 리소스를 url로 불러올 수 있도록 설정
    //현재 localhost:8080/summernoteImage/1234.jpg
    //로 접속하면 C:/summer/1234.jpg 파일을 불러온다.
	// real --> file:/vosej2241/tomcat/webapps/ROOT/upload/image/summer/
	// loca --> file://C:/summer/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/summernoteImage/**")
                .addResourceLocations("file:/vosej2241/tomcat/webapps/ROOT/upload/image/summer/");
    }
}