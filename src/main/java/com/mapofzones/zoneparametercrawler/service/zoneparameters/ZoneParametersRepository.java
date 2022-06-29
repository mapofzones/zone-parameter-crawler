package com.mapofzones.zoneparametercrawler.service.zoneparameters;

import com.mapofzones.zoneparametercrawler.domain.ZoneParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ZoneParametersRepository extends JpaRepository<ZoneParameters, ZoneParameters.ZoneParametersId> {

    @Modifying
    @Query(value =
            "INSERT INTO zone_parameters (ZONE, DATETIME, ACTIVE_VALIDATORS_QUANTITY, INFLATION, AMOUNT_OF_BONDED, UNBOUND_PERIOD) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6) ON CONFLICT DO NOTHING", nativeQuery = true)
    void saveAll(String zone, LocalDateTime datetime, Integer activeValidatorsQuantity, BigDecimal inflation, BigInteger amountOfBonded, Integer unboundPeriod);
    Optional<LocalDateTime> getFirstByZoneParametersId_ZoneOrderByZoneParametersId_DatetimeDesc(String zone);

}
