package com.mapofzones.zoneparametercrawler.scheduler;

import com.mapofzones.zoneparametercrawler.services.IZoneParametersFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ZoneParametersScheduler {

    private final IZoneParametersFacade zoneParametersFacadeProxy;

    public ZoneParametersScheduler(IZoneParametersFacade zoneParametersFacadeProxy) {
        this.zoneParametersFacadeProxy = zoneParametersFacadeProxy;
    }

    @Scheduled(cron = "1 0 * * * *")
//    @Scheduled(fixedDelayString = "3600000", initialDelay = 0)
    public void createEmptyZoneParameters() {
        this.zoneParametersFacadeProxy.createEmptyZoneParameters();
    }

    @Scheduled(cron = "30 0 * * * *")
//    @Scheduled(fixedDelayString = "3600000", initialDelay = 3000)
    public void callFindBaseZoneParameters() {
        this.zoneParametersFacadeProxy.findBaseZoneParameters();
    }

    @Scheduled(cron = "0 2 * * * *")
//    @Scheduled(fixedDelayString = "3600000", initialDelay = 5000)
    public void callFindDelegationsAmount() {
        this.zoneParametersFacadeProxy.findDelegationsAmount();
    }
}
