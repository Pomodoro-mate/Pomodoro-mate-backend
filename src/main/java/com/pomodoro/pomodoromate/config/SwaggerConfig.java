package com.pomodoro.pomodoromate.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwtSchemeName = "JWT";

        return new OpenAPI()
                .info(apiInfo())
                .components(new Components()
                        .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("Bearer")
                                .bearerFormat("JWT")
                        ))
                .addSecurityItem(new SecurityRequirement().addList(jwtSchemeName));
    }

    private Info apiInfo() {
        return new Info()
                .title("뽀모도로 메이트 API")
                .description("뽀모도로 메이트 API 명세")
                .version("1.0.0");
    }
}
