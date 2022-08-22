package com.mapofzones.zoneparametercrawler.services.zoneparameters;

import com.mapofzones.zoneparametercrawler.domain.ZoneParameters;

import java.util.List;

public interface IZoneParametersService {

    void saveAll(List<ZoneParameters> zoneParameters);

    List<ZoneParameters> findZoneParametersFromAddress(String zone, String address);
}
