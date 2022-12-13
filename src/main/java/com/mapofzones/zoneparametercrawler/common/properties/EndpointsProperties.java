package com.mapofzones.zoneparametercrawler.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "endpoint")
public class EndpointsProperties {

    private String inflation;
    private String validatorsQuantity;
    private String validatorList;
    private String amountOfBonded;
    private String stakingApr;
    private String unboundPeriod;
    private String supply;
    private String delegations;

}
