package com.mapofzones.zoneparametercrawler.services.zoneparameters.client;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class ZoneParametersDto {

    private Integer activeValidators;
    private String bondedTokens;
    private Double Inflation;
    private String unboundPeriod;
    private Set<String> delegatorAddresses;
    private Map<String, List<String>> validatorUndelegationMap;
    private Map<String, String> delegatorShares;
    private Set<String> delegators;


}
