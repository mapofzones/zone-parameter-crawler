package com.mapofzones.zoneparametercrawler.services.zoneparameters;

import com.mapofzones.zoneparametercrawler.domain.ZoneParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ZoneParametersRepository extends JpaRepository<ZoneParameters, ZoneParameters.ZoneParametersId> {

    @Modifying
    @Query(value = "UPDATE zone_parameters SET active_validators_quantity = ?3, inflation =?4, amount_of_bonded = ?5, unbound_period = ?6 " +
            "WHERE zone = ?1 AND datetime = ?2", nativeQuery = true)
    void saveBaseParameters(String zone, LocalDateTime dateTime, Integer activeValidatorsQuantity,
                            BigDecimal inflation, BigInteger amountOfBonded, Integer unboundPeriod);

    @Modifying
    @Query(value = "UPDATE zone_parameters SET delegation_amount = ?3 WHERE zone = ?1 AND datetime = ?2", nativeQuery = true)
    void saveDelegationAmount(String zone, LocalDateTime dateTime, BigDecimal delegationAmount);

    @Modifying
    @Query(value = "UPDATE zone_parameters SET undelegation_amount = ?3 WHERE zone = ?1 AND datetime = ?2", nativeQuery = true)
    void saveUndelegationAmount(String zone, LocalDateTime dateTime, BigDecimal delegationAmount);

    @Modifying
    @Query(value = "UPDATE zone_parameters SET delegators_count = ?3 WHERE zone = ?1 AND datetime = ?2", nativeQuery = true)
    void saveDelegatorAddressesCountAmount(String zone, LocalDateTime dateTime, Integer delegationAmount);

    @Query(value = "select * from zone_parameters zp where zp.datetime = " +
            "(select zp1.datetime from zone_parameters zp1 ORDER BY zp1.datetime DESC LIMIT 1)", nativeQuery = true)
    List<ZoneParameters> findEmptyZoneParameters();

    @Query(value = "SELECT zp.amount_of_bonded FROM zone_parameters zp " +
            "WHERE zp.zone = ?1 AND zp.amount_of_bonded IS NOT NULL ORDER BY zp.datetime DESC LIMIT 1", nativeQuery = true)
    Long getLastKnownBondedTokens(String zone);
}
