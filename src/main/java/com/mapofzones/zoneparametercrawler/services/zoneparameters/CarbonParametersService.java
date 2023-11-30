package com.mapofzones.zoneparametercrawler.services.zoneparameters;

import com.mapofzones.zoneparametercrawler.services.zoneparameters.client.RestClient;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.client.ZoneParametersDto;

import java.util.List;

public class CarbonParametersService {

    private final RestClient restClient;

    public CarbonParametersService(RestClient restClient) {
        this.restClient = restClient;
    }

    void calculateInflation(ZoneParametersDto foundZoneParameters, List<String> addresses) {
        String response = (String) restClient.callApi(addresses, "/carbon/inflation/v1/mint_data", "/mint_data/inflation_rate", false, 2).orElse(null);
        Double value = response != null ? Double.parseDouble(response) : null;

        foundZoneParameters.setInflation(value);
    }
}
