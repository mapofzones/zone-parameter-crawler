package com.mapofzones.zoneparametercrawler.config;

import com.mapofzones.zoneparametercrawler.services.IZoneParametersFacade;
import com.mapofzones.zoneparametercrawler.services.ZoneParametersFacade;
import com.mapofzones.zoneparametercrawler.services.ZoneParametersFacadeProxy;
import com.mapofzones.zoneparametercrawler.services.tokenprices.TokenPricesRepository;
import com.mapofzones.zoneparametercrawler.services.zone.ZoneRepository;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.IZoneParametersService;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.OsmosisParametersService;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.ZoneParametersRepository;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.ZoneParametersService;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZoneParametersCrawlerConfig {

    @Bean
    public IZoneParametersService zoneParametersService(ZoneParametersRepository zoneParametersRepository,
                                                        OsmosisParametersService osmosisParametersService,
                                                        RestClient restClient) {
        return new ZoneParametersService(zoneParametersRepository, osmosisParametersService, restClient);
    }

    @Bean
    public OsmosisParametersService osmosisParametersService(ZoneParametersRepository zoneParametersRepository,
                                                             TokenPricesRepository tokenPricesRepository,
                                                             RestClient restClient) {
        return new OsmosisParametersService(zoneParametersRepository, tokenPricesRepository, restClient);
    }

    @Bean
    public IZoneParametersFacade zoneParametersFacade(IZoneParametersService zoneParametersService,
                                                     ZoneRepository zoneRepository) {
        return new ZoneParametersFacade(zoneParametersService, zoneRepository);
    }

    @Bean
    public IZoneParametersFacade zoneParametersFacadeProxy(IZoneParametersFacade zoneParameterFacade) {
        return new ZoneParametersFacadeProxy(zoneParameterFacade);
    }

}
