package com.mapofzones.zoneparametercrawler.scheduler;

import com.mapofzones.zoneparametercrawler.services.ZoneParametersFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ZoneParametersScheduler {

    private final ZoneParametersFacade zoneParametersFacade;

    public ZoneParametersScheduler(ZoneParametersFacade zoneParametersFacade) {
        this.zoneParametersFacade = zoneParametersFacade;
    }

    @Scheduled(fixedDelayString = "#{baseProperties.syncTime}", initialDelay = 10)
    public void callFindZoneParameters() {
        zoneParametersFacade.findZoneParameters();
    }
}
