package com.mapofzones.zoneparametercrawler.services.zoneparameters;

import com.mapofzones.zoneparametercrawler.services.zoneparameters.client.RestClient;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.client.ZoneParametersDto;

import java.util.List;

public class CelestiaParametersService {

    private final RestClient restClient;

    public CelestiaParametersService(RestClient restClient) {
        this.restClient = restClient;
    }

    void calculateInflation(ZoneParametersDto foundZoneParameters, List<String> addresses) {
        String response = (String) restClient.callApi(addresses, "/cosmos/mint/v1beta1/inflation_rate", "/inflation_rate", false, 2).orElse(null);
        Double value = response != null ? Double.parseDouble(response) : null;

        foundZoneParameters.setInflation(value);
    }
}
