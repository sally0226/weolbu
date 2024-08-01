package com.bada.weolbu.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .version("v0.1")
                    .title("weolbu-bada")
                    .description("월급쟁이 부자들 과제 API 문서입니다.")
            )
    }
}