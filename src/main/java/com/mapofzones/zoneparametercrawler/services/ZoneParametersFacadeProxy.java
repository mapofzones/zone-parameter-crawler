package com.mapofzones.zoneparametercrawler.services;

import com.mapofzones.zoneparametercrawler.common.properties.multitheads.Mutex;

import java.util.HashMap;
import java.util.Map;

public class ZoneParametersFacadeProxy implements IZoneParametersFacade {

    private final String FIND_BASE_ZONE_PARAMETERS = "findBaseZoneParameters";
    private final String FIND_DELEGATIONS_AMOUNT = "findDelegationsAmount";
    private final String FIND_UNDELEGATIONS_AMOUNT = "findUndelegationsAmount";
    private final String FIND_DELEGATOR_ADDRESSES_COUNT = "findDelegatorAddressesCount";

    private final IZoneParametersFacade zoneParameterFacade;

    private final Map<String, Mutex> mutexMap;

    public ZoneParametersFacadeProxy(IZoneParametersFacade zoneParameterFacade) {
        this.zoneParameterFacade = zoneParameterFacade;
        this. mutexMap = new HashMap<>();

        mutexMap.put(FIND_BASE_ZONE_PARAMETERS, new Mutex());
        mutexMap.put(FIND_DELEGATIONS_AMOUNT, new Mutex());
        mutexMap.put(FIND_UNDELEGATIONS_AMOUNT, new Mutex());
        mutexMap.put(FIND_DELEGATOR_ADDRESSES_COUNT, new Mutex());
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

    @Override
    public void findUndelegationsAmount() {
        Mutex mutex = mutexMap.get(FIND_UNDELEGATIONS_AMOUNT);
        if (mutex.isAvailable()) {
            mutex.block();
            zoneParameterFacade.findUndelegationsAmount();
            mutex.release();
        }
    }

    @Override
    public void findDelegatorAddressesCount() {
        Mutex mutex = mutexMap.get(FIND_DELEGATOR_ADDRESSES_COUNT);
        if (mutex.isAvailable()) {
            mutex.block();
            zoneParameterFacade.findDelegatorAddressesCount();
            mutex.release();
        }
    }
}
