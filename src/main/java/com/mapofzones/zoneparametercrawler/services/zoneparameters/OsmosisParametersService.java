package com.mapofzones.zoneparametercrawler.services.zoneparameters;

import com.mapofzones.zoneparametercrawler.services.tokenprices.TokenPricesRepository;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.client.RestClient;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.client.ZoneParametersDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class OsmosisParametersService {

    private final ZoneParametersRepository zoneParametersRepository;
    private final TokenPricesRepository tokenPricesRepository;
    private final RestClient restClient;

    public OsmosisParametersService(ZoneParametersRepository zoneParametersRepository, TokenPricesRepository tokenPricesRepository, RestClient restClient) {
        this.zoneParametersRepository = zoneParametersRepository;
        this.tokenPricesRepository = tokenPricesRepository;
        this.restClient = restClient;
    }

    void calculateInflation(ZoneParametersDto foundZoneParameters) {
        BigDecimal second_year = BigDecimal.valueOf(300_000_000);


        BigDecimal totalSupply = BigDecimal.valueOf(tokenPricesRepository.getLastKnownTotalSupply("osmosis-1", "uosmo"));
        totalSupply = totalSupply.divide(BigDecimal.valueOf(1000000), 0, RoundingMode.HALF_UP);

        BigDecimal inflation = second_year.multiply(calcYearFactor()).divide(totalSupply, 6, RoundingMode.HALF_UP).setScale(4, RoundingMode.DOWN);
        foundZoneParameters.setInflation(inflation.doubleValue());
    }

    void calculateApr(ZoneParametersDto foundZoneParameters) {

        BigDecimal second_year = BigDecimal.valueOf(300_000_000);

        BigDecimal bondedTokens;
        if (foundZoneParameters.getBondedTokens() != null) {
            bondedTokens = BigDecimal.valueOf(Long.parseLong(foundZoneParameters.getBondedTokens()));
        } else
            bondedTokens = BigDecimal.valueOf(zoneParametersRepository.getLastKnownBondedTokens("osmosis-1"));

        bondedTokens = bondedTokens.divide(BigDecimal.valueOf(1000000), 0, RoundingMode.HALF_UP);

        BigDecimal apr = second_year.multiply(calcYearFactor()).multiply(BigDecimal.valueOf(0.25D)).divide(bondedTokens, 5, RoundingMode.HALF_UP);
    }

    private BigDecimal calcYearFactor() {
        BigDecimal inflationFactor = BigDecimal.valueOf(2 / 3d);

        int osmosisLaunchYear = 2021;
        int yearAddition = -1;

        if (LocalDateTime.now().getDayOfYear() > 171 ) {
            yearAddition = 0;
        }

        return inflationFactor.pow(LocalDateTime.now().getYear() - osmosisLaunchYear + yearAddition);
    }

}
