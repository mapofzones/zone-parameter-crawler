package com.mapofzones.zoneparametercrawler.config;

import com.mapofzones.zoneparametercrawler.common.properties.BaseProperties;
import com.mapofzones.zoneparametercrawler.common.properties.EndpointProperties;
import com.mapofzones.zoneparametercrawler.service.zoneparameters.client.LcdClient;
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
    public EndpointProperties endpointProperties() {
        return new EndpointProperties();
    }

    @Bean
    public BaseProperties baseProperties() {
        return new BaseProperties();
    }

    @Bean
    public RestTemplate lcdClientRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .additionalMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8))
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }

    @Bean
    public LcdClient lcdClient(RestTemplate lcdClientRestTemplate,
                               EndpointProperties endpointProperties) {
        return new LcdClient(lcdClientRestTemplate, endpointProperties);
    }

}
