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
    private final OsmosisParametersService osmosisParametersService;
    private final CelestiaParametersService celestiaParametersService;
    private final RestClient restClient;

    public ZoneParametersService(ZoneParametersRepository zoneParametersRepository,
                                 OsmosisParametersService osmosisParametersService,
                                 CelestiaParametersService celestiaParametersService,
                                 RestClient restClient) {
        this.zoneParametersRepository = zoneParametersRepository;
        this.osmosisParametersService = osmosisParametersService;
        this.celestiaParametersService = celestiaParametersService;
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
    @Transactional
    public void saveUndelegationAmount(List<ZoneParameters> zoneParametersList) {
        for (ZoneParameters zoneParameters : zoneParametersList) {
            zoneParametersRepository.saveUndelegationAmount(zoneParameters.getZoneParametersId().getZone(), zoneParameters.getZoneParametersId().getDatetime(), zoneParameters.getUndelegationAmount());
        }
    }

    @Override
    @Transactional
    public void saveDelegatorAddressesCount(List<ZoneParameters> zoneParametersList) {
        for (ZoneParameters zoneParameters : zoneParametersList) {
            zoneParametersRepository.saveDelegatorAddressesCountAmount(zoneParameters.getZoneParametersId().getZone(), zoneParameters.getZoneParametersId().getDatetime(), zoneParameters.getDelegatorAddressesCount());
        }
    }

    @Override
    public List<ZoneParameters> findEmptyZoneParameters() {
        return zoneParametersRepository.findEmptyZoneParameters();
    }

    @Override
    public void findBaseZoneParametersFromAddresses(ZoneParameters zoneParameters, List<String> addresses) {

        ZoneParametersDto foundZoneParameters = restClient.findParameters(zoneParameters.getZoneParametersId().getZone(), addresses);

        if (zoneParameters.getZoneParametersId().getZone().equals("osmosis-1")) {
            osmosisParametersService.calculateInflation(foundZoneParameters);
            osmosisParametersService.calculateApr(foundZoneParameters);
        }

        if (zoneParameters.getZoneParametersId().getZone().equals("celestia")) {
            celestiaParametersService.calculateInflation(foundZoneParameters, addresses);
        }

        zoneParameters.setBaseZoneParameters(foundZoneParameters);
    }

    @Override
    public void findDelegationsAmountFromAddresses(ZoneParameters zoneParameters, List<String> addresses) {
        ZoneParametersDto foundZoneParameters = restClient.findDelegatorShares(zoneParameters.getZoneParametersId().getZone(), addresses);
        zoneParameters.setValidatorsShares(foundZoneParameters);
    }

    @Override
    public void findUndelegationsAmountFromAddresses(ZoneParameters zoneParameters, List<String> addresses) {
        ZoneParametersDto foundZoneParameters = restClient.findUndelegations(zoneParameters.getZoneParametersId().getZone(), addresses);
        zoneParameters.setUndelegationsAmount(foundZoneParameters);
    }

    @Override
    public void findDelegatorAddressesCountFromAddresses(ZoneParameters zoneParameters, List<String> addresses) {
        ZoneParametersDto foundZoneParameters = restClient.findDelegatorAddresses(zoneParameters.getZoneParametersId().getZone(), addresses);
        zoneParameters.setDelegatorAddressesCount(foundZoneParameters);
    }

}
