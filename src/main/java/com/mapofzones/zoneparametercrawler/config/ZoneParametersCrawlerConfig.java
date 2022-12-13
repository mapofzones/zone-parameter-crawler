package com.mapofzones.zoneparametercrawler.config;

import com.mapofzones.zoneparametercrawler.common.properties.BaseProperties;
import com.mapofzones.zoneparametercrawler.common.properties.EndpointsProperties;
import com.mapofzones.zoneparametercrawler.services.IZoneParametersFacade;
import com.mapofzones.zoneparametercrawler.services.ZoneParametersFacade;
import com.mapofzones.zoneparametercrawler.services.ZoneParametersFacadeProxy;
import com.mapofzones.zoneparametercrawler.services.zone.ZoneRepository;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.IZoneParametersService;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.ZoneParametersRepository;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.ZoneParametersService;
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
                .setConnectTimeout(Duration.ofSeconds(20))
                .setReadTimeout(Duration.ofSeconds(20))
                .build();
    }

    @Bean
    public RestClient restClient(RestTemplate restClientRestTemplate,
                                 EndpointsProperties endpointsProperties) {
        return new RestClient(restClientRestTemplate, endpointsProperties);
    }

    @Bean
    public IZoneParametersService zoneParametersService(ZoneParametersRepository zoneParametersRepository,
                                                        RestClient restClient) {
        return new ZoneParametersService(zoneParametersRepository, restClient);
    }

    @Bean
    public IZoneParametersFacade zoneParametersFacade(IZoneParametersService zoneParametersService,
                                                     ZoneRepository zoneRepository) {
        return new ZoneParametersFacade(zoneParametersService, zoneRepository);
    }

    @Bean
    public IZoneParametersFacade zoneParametersFacadeProxy(IZoneParametersFacade zoneParameterFacade) {
        return new ZoneParametersFacadeProxy(zoneParameterFacade);
    }

}
