package com.mapofzones.zoneparametercrawler.services.zoneparameters;

import com.mapofzones.zoneparametercrawler.domain.ZoneParameters;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.client.RestClient;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.client.ZoneParametersDto;
import com.mapofzones.zoneparametercrawler.utils.TimeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ZoneParametersService implements IZoneParametersService {

    private final ZoneParametersRepository zoneParametersRepository;
    private final RestClient restClient;

    public ZoneParametersService(ZoneParametersRepository zoneParametersRepository,
                                 RestClient restClient) {
        this.zoneParametersRepository = zoneParametersRepository;
        this.restClient = restClient;
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

        ZoneParametersDto foundZoneParameters = restClient.findParameters(address);

        ZoneParameters zoneParameters = new ZoneParameters(foundZoneParameters);
        zoneParameters.setZoneParametersId(zoneParametersId);
        zoneParametersList.add(zoneParameters);

        return zoneParametersList;
    }
}
