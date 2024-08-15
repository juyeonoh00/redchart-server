package server.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@OpenAPIDefinition(
        info = @Info(title = "ODIRO Swagger",
                description = "ODIRO API 명세서",
                version = "v1.0.0")
)

@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi openApi() {
        return GroupedOpenApi.builder()
                .group("api")
                .addOpenApiCustomizer(oac -> oac
                        .security(List.of(new SecurityRequirement().addList("Bearer Authentication"))))
                .pathsToMatch("/**").build();
    }
}
