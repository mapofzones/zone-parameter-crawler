package com.mapofzones.zoneparametercrawler.service.zoneparameters;

import com.mapofzones.zoneparametercrawler.domain.ZoneParameters;
import com.mapofzones.zoneparametercrawler.service.zoneparameters.client.LcdClient;
import com.mapofzones.zoneparametercrawler.service.zoneparameters.client.ZoneParametersDto;
import com.mapofzones.zoneparametercrawler.utils.TimeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ZoneParametersService implements IZoneParametersService {

    private final ZoneParametersRepository zoneParametersRepository;
    private final LcdClient lcdClient;

    public ZoneParametersService(ZoneParametersRepository zoneParametersRepository,
                                 LcdClient lcdClient) {
        this.zoneParametersRepository = zoneParametersRepository;
        this.lcdClient = lcdClient;
    }

    @Override
    @Transactional
    public void saveAll(List<ZoneParameters> zoneParameters) {
        zoneParametersRepository.saveAll(zoneParameters);
    }

    @Override
    public List<ZoneParameters> findZoneParametersFromAddress(String zone, String address) {

        List<ZoneParameters> zoneParametersList = new ArrayList<>();
        ZoneParameters.ZoneParametersId zoneParametersId = new ZoneParameters.ZoneParametersId();
        zoneParametersId.setZone(zone);
        zoneParametersId.setDatetime(TimeHelper.nowAroundHours());

        ZoneParametersDto foundZoneParameters = lcdClient.findParameters(address);

        ZoneParameters zoneParameters = new ZoneParameters(foundZoneParameters);
        zoneParameters.setZoneParametersId(zoneParametersId);
        zoneParametersList.add(zoneParameters);

        return zoneParametersList;
    }

    private long getCountOfMissedEntries(LocalDateTime dateTime) {
        LocalDateTime nowAroundHour = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        return ChronoUnit.HOURS.between(dateTime, nowAroundHour);
    }
}
