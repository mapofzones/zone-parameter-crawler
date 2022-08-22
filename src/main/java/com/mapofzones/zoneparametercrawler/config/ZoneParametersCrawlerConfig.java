package com.mapofzones.zoneparametercrawler.config;

import com.mapofzones.zoneparametercrawler.common.properties.BaseProperties;
import com.mapofzones.zoneparametercrawler.common.properties.EndpointsProperties;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.client.RestClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Configuration
public class ZoneParametersCrawlerConfig {

    @Bean
    public EndpointsProperties endpointProperties() {
        return new EndpointsProperties();
    }

    @Bean
    public BaseProperties baseProperties() {
        return new BaseProperties();
    }

    @Bean
    public RestTemplate restClientRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .additionalMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8))
                .setConnectTimeout(Duration.ofSeconds(2))
                .setReadTimeout(Duration.ofSeconds(2))
                .build();
    }

    @Bean
    public RestClient restClient(RestTemplate restClientRestTemplate,
                                 EndpointsProperties endpointsProperties) {
        return new RestClient(restClientRestTemplate, endpointsProperties);
    }

}
