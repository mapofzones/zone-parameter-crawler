package com.mapofzones.zoneparametercrawler.services;

import com.mapofzones.zoneparametercrawler.domain.Zone;
import com.mapofzones.zoneparametercrawler.domain.ZoneParameters;
import com.mapofzones.zoneparametercrawler.services.zone.ZoneRepository;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.IZoneParametersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ZoneParametersFacade {

    private final IZoneParametersService zoneParametersService;
    private final ZoneRepository zoneRepository;

    public ZoneParametersFacade(IZoneParametersService zoneParametersService,
                                ZoneRepository zoneRepository) {
        this.zoneParametersService = zoneParametersService;
        this.zoneRepository = zoneRepository;
    }

    public void findZoneParameters() {
        log.info("Start...");

        List<Zone> zones = zoneRepository.findAll();

        for (Zone zone : zones) {
            String address = zoneRepository.findRestAddressWithHightestBlockByChainId(zone.getChainId());
            if (address != null) {
                List<ZoneParameters> foundZoneParameters = zoneParametersService.findZoneParametersFromAddress(zone.getChainId(), address);
                zoneParametersService.saveAll(foundZoneParameters);
            }
        }

        log.info("Finish...");
    }
}
