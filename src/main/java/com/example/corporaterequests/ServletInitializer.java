package com.example.corporaterequests;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
/**
 * Servlet initializer for configuring Spring Boot application.
 * This class extends {@code SpringBootServletInitializer} to support
 * traditional WAR deployment.
 */
public class ServletInitializer extends SpringBootServletInitializer {

    /**
     * Configures the application. It is invoked automatically when
     * the application is deployed as a WAR file.
     *
     * @param application the Spring Boot application builder
     * @return the configured application builder
     */
    @Override
    protected SpringApplicationBuilder configure(
            final SpringApplicationBuilder application
    ) {
        return application.sources(CorporateRequestsApplication.class);
    }
}

