package com.unisul.product_storage.config;

import com.unisul.product_storage.config.properties.SwaggerProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfig {

    private final SwaggerProperties swaggerProperties;

    public SwaggerConfig(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    @Bean
    public OpenAPI customOpenAPI() {

        SwaggerProperties.Contact contactProps = swaggerProperties.safeContact();

        Contact contact = new Contact()
                .name(contactProps.name())
                .url(contactProps.url())
                .email(contactProps.email());

        License license = new License()
                .name(swaggerProperties.licenseName())
                .url(swaggerProperties.licenseUrl());

        Info info = new Info()
                .title(swaggerProperties.title())
                .version(swaggerProperties.version())
                .description(swaggerProperties.description())
                .termsOfService(swaggerProperties.termsOfServiceUrl())
                .contact(contact)
                .license(license);

        List<Server> servers = swaggerProperties.safeServers().stream()
                .map(s -> new Server().url(s.url()).description(s.description()))
                .toList();

        return new OpenAPI().info(info).servers(servers);
    }
}
