package edu.example.dev_2_cc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration //뷰에서 upload 파일의 이미지를 불러오기 위한 경로 설정 입니다
public class WebConfig implements WebMvcConfigurer {
    private String connectPath = "/uploadPath/**";
    final Path FILE_ROOT = Paths.get("./").toAbsolutePath().normalize();
    private String uploadPath = FILE_ROOT.toString() + "/upload/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler(connectPath)
                .addResourceLocations("file:///"+uploadPath);
    }
}
