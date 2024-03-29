package com.mapofzones.zoneparametercrawler.services.zoneparameters;

import com.mapofzones.zoneparametercrawler.services.zoneparameters.client.RestClient;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.client.ZoneParametersDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ZoneParametersServiceTest {

    @Mock
    private ZoneParametersRepository zoneParametersRepository;

    @Mock
    private RestClient restClient;

    @Test
    void findZoneParametersFromAddressTest() {

        ZoneParametersDto dto = new ZoneParametersDto();
        dto.setInflation(0.01d);
        dto.setValidatorsQuantity(150);
        dto.setBondedTokens("123456789");
        dto.setUnboundPeriod("27000s");

        when(restClient.findParameters(anyString(),any())).thenReturn(dto);

//        ZoneParametersService zoneParametersService = new ZoneParametersService(zoneParametersRepository, restClient);
//        zoneParametersService.findBaseZoneParametersFromAddresses(new ZoneParameters(new ZoneParameters.ZoneParametersId("network-1", LocalDateTime.now())), List.of(""));

//        assertAll(() -> {
//            assertEquals(1, zoneParameters.size());
//            assertEquals("network-1", zoneParameters.get(0).getZoneParametersId().getZone());
//            assertEquals(BigDecimal.valueOf(dto.getInflation()), zoneParameters.get(0).getInflation());
//            assertEquals(27000, zoneParameters.get(0).getUnboundPeriod());
//            assertEquals(new BigInteger(dto.getBondedTokens()), zoneParameters.get(0).getAmountOfBonded());
//            assertEquals(dto.getActiveValidators(), zoneParameters.get(0).getActiveValidatorsQuantity());
//        });
    }
}