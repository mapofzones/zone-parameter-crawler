package com.mapofzones.zoneparametercrawler.service.zoneparameters.client;

import lombok.Data;

@Data
public class ZoneParametersDto {

    private Boolean isSuccess;
    private Integer activeValidators;
    private String bondedTokens;
    private Double Inflation;
    private String unboundPeriod;

}
