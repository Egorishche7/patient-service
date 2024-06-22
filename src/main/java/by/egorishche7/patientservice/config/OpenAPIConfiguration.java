package by.egorishche7.patientservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8081");
        server.description("Patient Service");

        Info information = new Info()
                .title("Patient Service API")
                .version("1.0")
                .description("API for managing patients.");

        Components components = new Components();
        components.addSecuritySchemes("bearer-key", new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
        );

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearer-key");

        return new OpenAPI()
                .info(information)
                .servers(List.of(server))
                .components(components)
                .security(List.of(securityRequirement));
    }
}
