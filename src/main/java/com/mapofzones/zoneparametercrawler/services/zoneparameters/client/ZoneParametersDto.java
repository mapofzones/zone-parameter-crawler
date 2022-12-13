package com.mapofzones.zoneparametercrawler.services.zoneparameters.client;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ZoneParametersDto {

    private Integer activeValidators;
    private String bondedTokens;
    private Double Inflation;
    private String unboundPeriod;
    private Map<String, List<String>> validatorDelegationMap;


}
