package com.mapofzones.zoneparametercrawler.services.zoneparameters.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapofzones.zoneparametercrawler.common.properties.EndpointsProperties;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

public class RestClient {

    private final RestTemplate restClientRestTemplate;
    private final EndpointsProperties endpointsProperties;

    public RestClient(RestTemplate restClientRestTemplate, EndpointsProperties endpointsProperties) {
        this.restClientRestTemplate = restClientRestTemplate;
        this.endpointsProperties = endpointsProperties;
    }

    public ZoneParametersDto findParameters(String address) {
        ZoneParametersDto zoneParametersDto = new ZoneParametersDto();
        zoneParametersDto.setActiveValidators(findActiveValidatorsQuantity(address));
        zoneParametersDto.setBondedTokens(findBondedTokens(address));
        zoneParametersDto.setInflation(findInflation(address));
        zoneParametersDto.setUnboundPeriod(findUnboundPeriod(address));
        return zoneParametersDto;
    }

    private Integer findActiveValidatorsQuantity(String address) {
        URI uri = URI.create(address + endpointsProperties.getValidatorsQuantity());
        String value = callApi(uri, new String[]{"result", "total"}).orElse(null);

        if (value == null) {
            uri = URI.create(address + endpointsProperties.getValidatorsQuantityExtra());
            value = callApi(uri, new String[]{"pagination", "total"}).orElse(null);
        }

        return value != null ? Integer.parseInt(value) : null;
    }

    private String findBondedTokens(String address) {
        URI uri = URI.create(address + endpointsProperties.getAmountOfBonded());
        return callApi(uri, new String[]{"pool", "bonded_tokens"}).orElse(null);
    }

    private Double findInflation(String address) {
        URI uri = URI.create(address + endpointsProperties.getInflation());
        String value = callApi(uri, new String[]{"inflation"}).orElse(null);
        return value != null ? Double.parseDouble(value) : null;
    }

    private String findUnboundPeriod(String address) {
        URI uri = URI.create(address + endpointsProperties.getUnboundPeriod());
        return callApi(uri, new String[]{"params", "unbonding_time"}).orElse(null);
    }

    private Optional<String> callApi(URI uri, String[] params) {
        try {
            Optional<String> json = Optional.ofNullable(restClientRestTemplate.getForEntity(uri, String.class).getBody());
            return findValue(json.orElse(null), params);
        } catch (Exception x) {
            return Optional.empty();
        }
    }

    private Optional<String> findValue(String json, String[] params) {

        if (json == null || params == null) {
            return Optional.empty();
        }

        try {
            JsonNode node = new ObjectMapper().readValue(json, JsonNode.class);
            for (String param : params) {
                node = node.get(param);
            }
            return Optional.of(node.asText(null));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
