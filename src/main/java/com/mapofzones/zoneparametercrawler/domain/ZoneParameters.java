package com.mapofzones.zoneparametercrawler.domain;

import com.mapofzones.zoneparametercrawler.services.zoneparameters.client.ZoneParametersDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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

@Data
@Entity
@NoArgsConstructor
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

    public ZoneParameters(ZoneParametersDto zoneParametersDto) {
        this.activeValidatorsQuantity = zoneParametersDto.getActiveValidators();
        this.amountOfBonded = zoneParametersDto.getBondedTokens() != null ? new BigInteger(zoneParametersDto.getBondedTokens()) : null;
        this.inflation = zoneParametersDto.getInflation() != null ? BigDecimal.valueOf(zoneParametersDto.getInflation()) : null;
        this.unboundPeriod = zoneParametersDto.getUnboundPeriod() != null ? Integer.parseInt(zoneParametersDto.getUnboundPeriod().replaceAll("\\D", "")) : null ;
    }
}
