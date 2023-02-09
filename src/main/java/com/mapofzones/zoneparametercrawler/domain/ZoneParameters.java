package com.mapofzones.zoneparametercrawler.domain;

import com.mapofzones.zoneparametercrawler.services.zoneparameters.client.ZoneParametersDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = {"zoneParametersId"})
@Table(name = "ZONE_PARAMETERS")
public class ZoneParameters {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    @Embeddable
    public static class ZoneParametersId implements Serializable {

        @Column(name = "ZONE")
        private String zone;

        @Column(name = "DATETIME")
        private LocalDateTime datetime;
    }

    @EmbeddedId
    private ZoneParametersId zoneParametersId;

    @Column(name = "ACTIVE_VALIDATORS_QUANTITY")
    private Integer activeValidatorsQuantity;

    @Column(name = "INFLATION")
    private BigDecimal inflation;

    @Column(name = "AMOUNT_OF_BONDED")
    private BigInteger amountOfBonded;

    @Column(name = "UNBOUND_PERIOD")
    private Integer unboundPeriod;

    @Column(name = "DELEGATION_AMOUNT")
    private BigDecimal delegationAmount;

    @Column(name = "UNDELEGATION_AMOUNT")
    private BigDecimal undelegationAmount;

    @Column(name = "DELEGATORS_COUNT")
    private Integer delegatorAddressesCount;

    public ZoneParameters(ZoneParametersId zoneParametersId) {
        this.zoneParametersId = zoneParametersId;
    }

    public void setBaseZoneParameters(ZoneParametersDto zoneParametersDto) {
        this.activeValidatorsQuantity = zoneParametersDto.getValidatorsQuantity();
        this.amountOfBonded = zoneParametersDto.getBondedTokens() != null ? new BigInteger(zoneParametersDto.getBondedTokens()) : null;
        this.inflation = zoneParametersDto.getInflation() != null ? BigDecimal.valueOf(zoneParametersDto.getInflation()) : null;
        this.unboundPeriod = zoneParametersDto.getUnboundPeriod() != null ? Integer.parseInt(zoneParametersDto.getUnboundPeriod().replaceAll("\\D", "")) : null;
    }

    public void setValidatorsShares(ZoneParametersDto zoneParametersDto) {
        if (zoneParametersDto.getDelegatorShares() != null && !zoneParametersDto.getDelegatorShares().isEmpty()) {
            BigDecimal amountShares = BigDecimal.ZERO;
            for (Map.Entry<String, String> curretEntry : zoneParametersDto.getDelegatorShares().entrySet()) {
                if (curretEntry != null) {
                    amountShares = amountShares.add(BigDecimal.valueOf(Double.parseDouble(curretEntry.getValue())));
                }
            }
            this.delegationAmount = amountShares;
        }
    }

    public void setDelegatorAddressesCount(ZoneParametersDto zoneParametersDto) {
        if (validateDelegatorAddressesCount(zoneParametersDto.getDelegatorAddresses(), zoneParametersDto.getValidatorsQuantity())) {
            this.delegatorAddressesCount = delegatorAddressesCountCalculation(zoneParametersDto.getDelegatorAddresses());
        }
    }

    private Integer delegatorAddressesCountCalculation(Map<String, List<String>> validatorDelegatorAddressesMap) {

        Set<String> delegatorsAddress = new HashSet<>();

        for (Map.Entry<String, List<String>> entry : validatorDelegatorAddressesMap.entrySet())
            if (entry.getValue() != null)
                delegatorsAddress.addAll(entry.getValue());
        return delegatorsAddress.size();
    }

    private boolean validateDelegatorAddressesCount(Map<String, List<String>> validatorDelegatorAddressesMap, Integer activeValidators) {
        return (validatorDelegatorAddressesMap != null && !validatorDelegatorAddressesMap.isEmpty()) &&
                (activeValidators != null && validatorDelegatorAddressesMap.size() == activeValidators);
    }

    public void setUndelegationsAmount(ZoneParametersDto zoneParametersDto) {
        if (validateValidatorUndelegationMap(zoneParametersDto.getValidatorUndelegationMap(), zoneParametersDto.getValidatorsQuantity())) {
            this.undelegationAmount = undelegationAmountCalculation(zoneParametersDto.getValidatorUndelegationMap());
        }
    }

    private BigDecimal undelegationAmountCalculation(Map<String, List<String>> validatorDelegationMap) {
        double totalAmount = 0D;
        for (Map.Entry<String, List<String>> entry : validatorDelegationMap.entrySet())
            if (entry.getValue() != null)
                for (String amount : entry.getValue())
                    totalAmount += Double.parseDouble(amount);
        return BigDecimal.valueOf(totalAmount);
    }

    private boolean validateValidatorUndelegationMap(Map<String, List<String>> validatorUndelegationMap, Integer activeValidators) {
        return (validatorUndelegationMap != null && !validatorUndelegationMap.isEmpty()) &&
                (activeValidators != null && validatorUndelegationMap.size() == activeValidators);
    }
}
