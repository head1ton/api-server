package com.youwiz.demoapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*
http://localhost:8080/swagger-ui.html
 */

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
//    @Bean // Bean을 넣어주면 안나옴..-_-;
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(swaggerInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.rest.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false);
    }

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder().title("Spring API Documentation")
                .description("앱 개발 시 사용되는 서버 API에 대한 연동 문서입니다.")
                .license("YouWiz").licenseUrl("http://youwiz.com").version("1").build();
    }
}
