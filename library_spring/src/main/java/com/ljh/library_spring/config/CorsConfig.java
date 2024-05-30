package com.ljh.library_spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//跨域配置
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private String filePath = "D:/code/MyWorks/LibraryManagementSystem/library_spring/src/main/resources/static/";
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //将前者的路径映射到后者的static路径,保证图片的读取，即前端访问服务器static的资源时，会映射访问到本地实际地址部分的资源
        registry.addResourceHandler("/static/**").addResourceLocations("file:" + filePath);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
      // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 是否允许cookie
                .allowCredentials(true)
                // 设置允许的请求方式
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                // 设置允许的header属性
                .allowedHeaders("*")
                // 跨域允许时间
                .maxAge(3600);
    }
}
