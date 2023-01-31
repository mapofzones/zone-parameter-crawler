package com.mapofzones.zoneparametercrawler.services.zoneparameters.client;

import com.mapofzones.zoneparametercrawler.AbstractTest;
import com.mapofzones.zoneparametercrawler.config.RestClientConfig;
import com.mapofzones.zoneparametercrawler.config.TestConfig;
import com.mapofzones.zoneparametercrawler.domain.ZoneParameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@AutoConfigureWebClient
@ContextConfiguration(classes = {RestClientConfig.class, TestConfig.class})
class RestClientTest extends AbstractTest {

    @Autowired
    private RestClient restClient;

//    @Test
//    void findParameters_Positive_Test() {
//        ZoneParametersDto dto = restClient.findParameters(List.of("http://65.108.79.216:1317"));
//        assertAll(() -> {
//            assertNotNull(dto.getInflation());
//            assertNotNull(dto.getBondedTokens());
//            assertNotNull(dto.getUnboundPeriod());
//            assertNotNull(dto.getActiveValidators());
//        });
//    }
//
//    @Test
//    void findParameters_Negative_Test() {
//        ZoneParametersDto dto = restClient.findParameters(List.of("http://11.1.1.1:1317"));
//        assertAll(() -> {
//            assertNull(dto.getInflation());
//            assertNull(dto.getBondedTokens());
//            assertNull(dto.getUnboundPeriod());
//            assertNull(dto.getActiveValidators());
//        });
//    }
//
//    @Test
//    void findParameters_Positive_Test1() {
//        List<String> l = restClient.findValidatorsAddresses(List.of("http://65.108.79.216:1317"));
//        System.out.println(l);
//    }

//    @Test
//    void findDelegationsAddressesOfActiveValidatorsTest() {
//        List<String> addresses = List.of(
//        "http://51.77.117.110:1317",
//        "http://65.109.26.40:1317",
//        "http://43.153.101.118:1317",
//        "http://142.132.157.153:1317",
//        "http://188.34.144.4:1317",
//        "http://176.9.28.41:1317",
//        "http://188.42.195.167:1317",
//        "http://176.9.91.106:1317",
//        "http://157.245.115.42:1317",
//        "http://65.21.79.20:1317",
//        "http://162.55.253.41:1317",
//        "http://65.109.87.49:1317",
//        "http://34.105.246.121:1317",
//        "http://38.242.209.186:1317",
//        "http://128.199.151.241:1317",
//        "http://35.232.35.154:1317",
//        "http://141.95.45.245:1317",
//        "http://141.95.45.245:1317",
//        "http://34.159.83.74:1317",
//        "http://65.108.111.200:1317"
//        );
//
//        ZoneParametersDto dto = restClient.findDelegations(addresses);
//        System.out.println(dto);
//    }

    @Test
    void find() {
        ZoneParametersDto dto = restClient.findParameters("stride-1", List.of(""));
        ZoneParameters zp = new ZoneParameters(new ZoneParameters.ZoneParametersId());
        zp.setBaseZoneParameters(dto);

        System.out.println();
    }

//    @Test
//    void findDelegations() {
//        ZoneParametersDto dto = restClient.findDelegatorShares(List.of("https://injective-lcd.quickapi.com:443"));
//        ZoneParameters zp = new ZoneParameters(new ZoneParameters.ZoneParametersId());
//        zp.setValidatorsShares(dto);
//
//        System.out.println();
//    }
//
//    @Test
//    void findUnDelegations() {
//        ZoneParametersDto dto = restClient.findUndelegations(List.of("http://46.4.28.38:1317"));
//        ZoneParameters zp = new ZoneParameters(new ZoneParameters.ZoneParametersId());
//        zp.setUndelegationsAmount(dto);
//
//        System.out.println();
//    }
//
//    @Test
//    void findDelegatorAddresses() {
//        ZoneParametersDto dto = restClient.findDelegatorAddresses(List.of("https://injective-lcd.quickapi.com:443"));
//        ZoneParameters zp = new ZoneParameters(new ZoneParameters.ZoneParametersId());
//        zp.setDelegatorAddressesCount(dto);
//
//        System.out.println();
//    }

}