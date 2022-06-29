package com.mapofzones.zoneparametercrawler.service;

import com.mapofzones.zoneparametercrawler.domain.Zone;
import com.mapofzones.zoneparametercrawler.domain.ZoneParameters;
import com.mapofzones.zoneparametercrawler.service.token.TokenRepository;
import com.mapofzones.zoneparametercrawler.service.zone.ZoneRepository;
import com.mapofzones.zoneparametercrawler.service.zoneparameters.IZoneParametersService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ZoneParametersFacade {

    private final IZoneParametersService zoneParametersService;
    private final ZoneRepository zoneRepository;
    private final TokenRepository tokenRepository;

    public ZoneParametersFacade(IZoneParametersService zoneParametersService,
                                ZoneRepository zoneRepository,
                                TokenRepository tokenRepository) {
        this.zoneParametersService = zoneParametersService;
        this.zoneRepository = zoneRepository;
        this.tokenRepository = tokenRepository;
    }

    public void findZoneParameters() {
        System.out.println("Start...");

        List<Zone> zones = zoneRepository.findAllByEnabledIsTrue();

        for (Zone zone : zones) {
            String address = zoneRepository.findLcdAddressWithHightestBlockByChainId(zone.getChainId());
            if (address != null) {
                List<ZoneParameters> foundZoneParameters = zoneParametersService.findZoneParametersFromAddress(zone.getChainId(), address);
                zoneParametersService.saveAll(foundZoneParameters);
            }
        }

        System.out.println("Finish...");
    }

    private Map<String, List<String>> findAll() {
        Map<String, List<String>> zoneDenomMap = new HashMap<>();
        List<Zone> zones = zoneRepository.findAll();

        zones.forEach(zone -> {
            zoneDenomMap.put(zone.getChainId(), tokenRepository.findByTokenId_Zone(zone.getChainId()).stream().map(t -> t.getTokenId().getBaseDenom()).collect(Collectors.toList()));
        });

        return zoneDenomMap;
    }

}
