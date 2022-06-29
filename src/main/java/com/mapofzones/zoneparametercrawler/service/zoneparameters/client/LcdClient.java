package com.mapofzones.zoneparametercrawler.service.zoneparameters.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapofzones.zoneparametercrawler.common.properties.EndpointProperties;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

public class LcdClient {

    private final RestTemplate lcdClientRestTemplate;
    private final EndpointProperties endpointProperties;

    public LcdClient(RestTemplate lcdClientRestTemplate, EndpointProperties endpointProperties) {
        this.lcdClientRestTemplate = lcdClientRestTemplate;
        this.endpointProperties = endpointProperties;
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
        URI uri = URI.create(address + endpointProperties.getValidatorsQuantity());
        String value = callApi(uri, new String[]{"result", "total"}).orElse(null);

        if (value == null) {
            uri = URI.create(address + endpointProperties.getValidatorsQuantityExtra());
            value = callApi(uri, new String[]{"pagination", "total"}).orElse(null);
        }

        return value != null ? Integer.parseInt(value) : null;
    }

    private String findBondedTokens(String address) {
        URI uri = URI.create(address + endpointProperties.getAmountOfBonded());
        return callApi(uri, new String[]{"pool", "bonded_tokens"}).orElse(null);
    }

    private Double findInflation(String address) {
        URI uri = URI.create(address + endpointProperties.getInflation());
        String value = callApi(uri, new String[]{"inflation"}).orElse(null);
        return value != null ? Double.parseDouble(value) : null;
    }

    private String findUnboundPeriod(String address) {
        URI uri = URI.create(address + endpointProperties.getUnboundPeriod());
        return callApi(uri, new String[]{"params", "unbonding_time"}).orElse(null);
    }

    private Optional<String> callApi(URI uri, String[] params) {
        try {
            Optional<String> json = Optional.ofNullable(lcdClientRestTemplate.getForEntity(uri, String.class).getBody());
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
