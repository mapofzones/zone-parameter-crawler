package com.mapofzones.zoneparametercrawler.services;

import com.mapofzones.zoneparametercrawler.common.properties.multitheads.Mutex;

import java.util.HashMap;
import java.util.Map;

public class ZoneParametersFacadeProxy implements IZoneParametersFacade {

    private final String FIND_BASE_ZONE_PARAMETERS = "findBaseZoneParameters";
    private final String FIND_DELEGATIONS_AMOUNT = "findDelegationsAmount";

    private final IZoneParametersFacade zoneParameterFacade;

    private final Map<String, Mutex> mutexMap;

    public ZoneParametersFacadeProxy(IZoneParametersFacade zoneParameterFacade) {
        this.zoneParameterFacade = zoneParameterFacade;
        this. mutexMap = new HashMap<>();

        mutexMap.put(FIND_BASE_ZONE_PARAMETERS, new Mutex());
        mutexMap.put(FIND_DELEGATIONS_AMOUNT, new Mutex());
    }

    @Override
    public void createEmptyZoneParameters() {
        zoneParameterFacade.createEmptyZoneParameters();
    }

    @Override
    public void findBaseZoneParameters() {
        Mutex mutex = mutexMap.get(FIND_BASE_ZONE_PARAMETERS);
        if (mutex.isAvailable()) {
            mutex.block();
            zoneParameterFacade.findBaseZoneParameters();
            mutex.release();
        }
    }

    @Override
    public void findDelegationsAmount() {
        Mutex mutex = mutexMap.get(FIND_DELEGATIONS_AMOUNT);
        if (mutex.isAvailable()) {
            mutex.block();
            zoneParameterFacade.findDelegationsAmount();
            mutex.release();
        }
    }
}
