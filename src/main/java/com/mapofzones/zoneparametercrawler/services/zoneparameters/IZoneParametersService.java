package com.mapofzones.zoneparametercrawler.services.zoneparameters;

import com.mapofzones.zoneparametercrawler.domain.ZoneParameters;

import java.util.List;

public interface IZoneParametersService {

    void saveAll(List<ZoneParameters> zoneParameters);
    void saveBaseParameters(List<ZoneParameters> zoneParameters);
    void saveDelegationAmount(List<ZoneParameters> zoneParameters);
    void saveUndelegationAmount(List<ZoneParameters> zoneParameters);
    void saveDelegatorAddressesCount(List<ZoneParameters> zoneParameters);

    List<ZoneParameters> findEmptyZoneParameters();
    void findBaseZoneParametersFromAddresses(ZoneParameters zoneParameters, List<String> addresses);
    void findDelegationsAmountFromAddresses(ZoneParameters zoneParameters, List<String> addresses);
    void findUndelegationsAmountFromAddresses(ZoneParameters zoneParameters, List<String> addresses);
    void findDelegatorAddressesCountFromAddresses(ZoneParameters zoneParameters, List<String> addresses);

}