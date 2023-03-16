package com.mapofzones.zoneparametercrawler.services.tokenprices;

import com.mapofzones.zoneparametercrawler.domain.TokenPrices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenPricesRepository extends JpaRepository<TokenPrices, TokenPrices.ZoneParametersId> {

    @Query(value = "SELECT tp.symbol_supply FROM token_prices tp " +
            "WHERE tp.zone = ?1 AND tp.base_denom = ?2 AND tp.symbol_supply IS NOT NULL ORDER BY tp.datetime DESC LIMIT 1", nativeQuery = true)
    Long getLastKnownTotalSupply(String zone, String denom);

}
