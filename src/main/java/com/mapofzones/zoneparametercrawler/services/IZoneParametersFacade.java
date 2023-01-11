package com.mapofzones.zoneparametercrawler.services;

public interface IZoneParametersFacade {

    void createEmptyZoneParameters();
    void findBaseZoneParameters();
    void findDelegationsAmount();
    void findUndelegationsAmount();
    void findDelegatorAddressesCount();

}
