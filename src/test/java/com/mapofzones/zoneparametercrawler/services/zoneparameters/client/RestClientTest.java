package com.mapofzones.zoneparametercrawler.services.zoneparameters.client;

import com.mapofzones.zoneparametercrawler.AbstractTest;
import com.mapofzones.zoneparametercrawler.config.TestConfig;
import com.mapofzones.zoneparametercrawler.config.ZoneParametersCrawlerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureWebClient
@ContextConfiguration(classes = {ZoneParametersCrawlerConfig.class, TestConfig.class})
class RestClientTest extends AbstractTest {

    @Autowired
    private RestClient restClient;

    @Test
    void findParameters_Positive_Test() {
        ZoneParametersDto dto = restClient.findParameters("http://65.108.79.216:1317");
        assertAll(() -> {
            assertNotNull(dto.getInflation());
            assertNotNull(dto.getBondedTokens());
            assertNotNull(dto.getUnboundPeriod());
            assertNotNull(dto.getActiveValidators());
        });
    }

    @Test
    void findParameters_Negative_Test() {
        ZoneParametersDto dto = restClient.findParameters("http://11.1.1.1:1317");
        assertAll(() -> {
            assertNull(dto.getInflation());
            assertNull(dto.getBondedTokens());
            assertNull(dto.getUnboundPeriod());
            assertNull(dto.getActiveValidators());
        });
    }

}