package com.example.ntmyou.Config.Swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public OpenAPI openAPI () {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }
    private Info apiInfo() {
        return  new Info()
                .title("Mshoppingmall 테스트")
                .description("Springdoc을 이용한 Swagger UI 테스트 ")
                .version("1.0.0");
    }
}
