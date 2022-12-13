package com.mapofzones.zoneparametercrawler.services.zoneparameters.dto;

import com.mapofzones.zoneparametercrawler.domain.Zone;
import com.mapofzones.zoneparametercrawler.domain.ZoneParameters;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class FindZoneParametersDto {

    private Zone zone;
    private List<String> rpcAddresses;
    private ZoneParameters existingZoneParameters;

}
