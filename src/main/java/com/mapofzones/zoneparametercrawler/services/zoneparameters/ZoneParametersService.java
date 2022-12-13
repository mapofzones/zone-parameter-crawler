package com.mapofzones.zoneparametercrawler.services.zoneparameters;

import com.mapofzones.zoneparametercrawler.domain.ZoneParameters;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.client.RestClient;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.client.ZoneParametersDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
public class ZoneParametersService implements IZoneParametersService {

    private final ZoneParametersRepository zoneParametersRepository;
    private final RestClient restClient;

    public ZoneParametersService(ZoneParametersRepository zoneParametersRepository,
                                 RestClient restClient) {
        this.zoneParametersRepository = zoneParametersRepository;
        this.restClient = restClient;
    }

    @Override
    @Transactional
    public void saveAll(List<ZoneParameters> zoneParametersList) {
        zoneParametersRepository.saveAll(zoneParametersList);
    }

    @Override
    @Transactional
    public void saveBaseParameters(List<ZoneParameters> zoneParametersList) {
        for (ZoneParameters zoneParameters : zoneParametersList) {
            zoneParametersRepository.saveBaseParameters(zoneParameters.getZoneParametersId().getZone(), zoneParameters.getZoneParametersId().getDatetime(), zoneParameters.getActiveValidatorsQuantity(),
                    zoneParameters.getInflation(), zoneParameters.getAmountOfBonded(), zoneParameters.getUnboundPeriod());
        }
    }

    @Override
    @Transactional
    public void saveDelegationAmount(List<ZoneParameters> zoneParametersList) {
        for (ZoneParameters zoneParameters : zoneParametersList) {
            zoneParametersRepository.saveDelegationAmount(zoneParameters.getZoneParametersId().getZone(), zoneParameters.getZoneParametersId().getDatetime(), zoneParameters.getDelegationAmount());
        }
    }

    @Override
    public List<ZoneParameters> findEmptyZoneParameters() {
        return zoneParametersRepository.findEmptyZoneParameters();
    }

    @Override
    public void findBaseZoneParametersFromAddresses(ZoneParameters zoneParameters, List<String> addresses) {
        ZoneParametersDto foundZoneParameters = restClient.findParameters(addresses);
        zoneParameters.setBaseZoneParameters(foundZoneParameters);
    }

    @Override
    public void findDelegationsAmountFromAddresses(ZoneParameters zoneParameters, List<String> addresses) {
        ZoneParametersDto foundZoneParameters = restClient.findDelegations(addresses);
        zoneParameters.setDelegationsAmount(foundZoneParameters);
    }

}
