package com.mapofzones.zoneparametercrawler.services.zoneparameters.client;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class ZoneParametersDto {

    private Integer validatorsQuantity;
    private String bondedTokens;
    private Double Inflation;
    private String unboundPeriod;
    private Map<String, List<String>> delegatorAddresses;
    private Map<String, List<String>> validatorUndelegationMap;
    private Map<String, String> delegatorShares;
    private Set<String> delegators;


}
