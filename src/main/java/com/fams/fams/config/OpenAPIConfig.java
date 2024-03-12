package com.fams.fams.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(contact = @Contact(

), description = "OpenAPI documentation for Spring Security", title = "OpenAPI specification", version = "1.0"), servers = {

                @Server(description = "PROD ENV", url = "https://fams-api.azurewebsites.net:8080/"
                // "https://68.183.178.176:8080/"
                ),
                @Server(description = "DEV ENV", url = "https://localhost:8080/"

                )
})
@SecurityScheme(name = "Bear Authentication", description = "JWT auth description", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer", in = SecuritySchemeIn.HEADER)
public class OpenAPIConfig {
}
