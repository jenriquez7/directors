package com.directa24.challenge.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "endpoints.external")
public class ExternalEndpointsConstants {

    private String eronMoviesUrl;
}
