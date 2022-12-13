package com.mapofzones.zoneparametercrawler.services.zoneparameters.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapofzones.zoneparametercrawler.common.properties.EndpointsProperties;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class RestClient {

    private final RestTemplate restClientRestTemplate;
    private final EndpointsProperties endpointsProperties;

    public RestClient(RestTemplate restClientRestTemplate, EndpointsProperties endpointsProperties) {
        this.restClientRestTemplate = restClientRestTemplate;
        this.endpointsProperties = endpointsProperties;
    }

    public ZoneParametersDto findParameters(List<String> addresses) {
        ZoneParametersDto zoneParametersDto = new ZoneParametersDto();
        zoneParametersDto.setActiveValidators(findActiveValidatorsQuantity(addresses));
        zoneParametersDto.setBondedTokens(findBondedTokens(addresses));
        zoneParametersDto.setInflation(findInflation(addresses));
        zoneParametersDto.setUnboundPeriod(findUnboundPeriod(addresses));
        return zoneParametersDto;
    }

    public ZoneParametersDto findDelegations(List<String> addresses) {
        ZoneParametersDto zoneParametersDto = new ZoneParametersDto();
        zoneParametersDto.setActiveValidators(findActiveValidatorsQuantity(addresses));

        List<String> findValidatorsAddresses = findValidatorsAddresses(addresses);

        if (findValidatorsAddresses != null)
            zoneParametersDto.setValidatorDelegationMap(findDelegationsAddressesOfActiveValidators(addresses, findValidatorsAddresses));
        else zoneParametersDto.setValidatorDelegationMap(new HashMap<>());

        return zoneParametersDto;
    }

    private Integer findActiveValidatorsQuantity(List<String> addresses) {
        String value = (String) callApi(addresses, endpointsProperties.getValidatorsQuantity(), "/result/total", false, 2).orElse(null);

        if (value == null)
            value =  (String) callApi(addresses, endpointsProperties.getValidatorList(), "/pagination/total", false, 2).orElse(null);

        return value != null ? Integer.parseInt(value) : null;
    }

    private List<String> findValidatorsAddresses(List<String> addresses) {
        return (List<String>) callApi(addresses, endpointsProperties.getValidatorList(), "validators/operator_address", true, 2).orElse(null);
    }

    private Map<String, List<String>> findDelegationsAddressesOfActiveValidators(List<String> addresses, List<String> validatorAddresses) {
        Map<String, List<String>> delegationsAddressesOfActiveValidatorsMap = new HashMap<>();

        ForkJoinPool forkJoinPool = new ForkJoinPool(50);

        AtomicInteger iter = new AtomicInteger();
        Runnable doWork = () -> validatorAddresses.stream().parallel().forEach(vAddr -> {
            List<String> amount1 = (List<String>) callApi(addresses,
                    String.format(endpointsProperties.getDelegations(), vAddr),
                    "delegation_responses/balance/amount", true, 2).orElse(null);
            delegationsAddressesOfActiveValidatorsMap.put(vAddr, amount1);
            iter.getAndIncrement();
        });

        try {
            forkJoinPool.submit(doWork).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            forkJoinPool.shutdown();
        }
        return delegationsAddressesOfActiveValidatorsMap;
    }

    private String findBondedTokens(List<String> addresses) {
        return (String) callApi(addresses, endpointsProperties.getAmountOfBonded(), "/pool/bonded_tokens", false, 2).orElse(null);
    }

    private Double findInflation(List<String> addresses) {
        String value = (String) callApi(addresses, endpointsProperties.getInflation(), "/inflation", false, 2).orElse(null);
        return value != null ? Double.parseDouble(value) : null;
    }

    private String findUnboundPeriod(List<String> addresses) {
        return (String) callApi(addresses, endpointsProperties.getUnboundPeriod(), "/params/unbonding_time", false, 2).orElse(null);
    }

    private Optional<Object> callApiWithPagination(List<String> addresses, String endpoint, String jsonPath, boolean multipleValue) {
        String paginationTotalCountTrueParam = "pagination.count_total=true";
        String paginationLimitZeroParam = "pagination.limit=1";
        String paginationLimitParam = "pagination.limit=%s";
        String parameterSeparator = hasParameters(endpoint) ? "&" : "?";

        String endpointForTotalCount = endpoint + parameterSeparator + paginationTotalCountTrueParam + "&" + paginationLimitZeroParam;

        String totalCount = (String) callApi(addresses, endpointForTotalCount, "/pagination/total", false, 2).orElse(null);

        if (totalCount == null)
            return Optional.empty();
        else {
            return callApi(addresses, endpoint + parameterSeparator + String.format(paginationLimitParam, totalCount), jsonPath, multipleValue, 2);
        }

    }

    private Optional<Object> callApi(List<String> addresses, String endpoint, String jsonPath, boolean multipleValue, int tryingCall) {

        List<URI> uris = addresses.stream().map(addr -> URI.create(addr + endpoint)).collect(Collectors.toList());

        for (int i = 0; i < tryingCall; i++) {
            for (URI uri : uris) {
                Optional<String> json = Optional.empty();
                try {
                    json = Optional.ofNullable(restClientRestTemplate.getForEntity(uri, String.class).getBody());
                } catch (Exception e) {
                    //e.printStackTrace();
                }
                if (json.isPresent()) {
                    if (multipleValue) {
                        JsonNode rootNode;
                        try {
                            rootNode = new ObjectMapper().readValue(json.orElse(null), JsonNode.class);
                            return findMultipleValue(rootNode, Arrays.asList(jsonPath.split("/")), Optional.empty());
                        } catch (Exception e) {

                        }
                    } else return findSingleValue(json.orElse(null), jsonPath);
                }
            }
        }


        return Optional.empty();
    }

    private Optional<Object> findSingleValue(String json, String jsonPath) {

        if (!StringUtils.hasText(json) || !StringUtils.hasText(jsonPath))
            return Optional.empty();

        try {
            JsonNode node = new ObjectMapper().readValue(json, JsonNode.class).at(jsonPath);
            return Optional.of(node.asText(null));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Object> findMultipleValue(JsonNode rootNode, List<String> jsonPath, Optional<Object> acc) {

        if (rootNode == null || jsonPath == null)
            return Optional.empty();

        List<String> accInStr = (List<String>) acc.orElse(new ArrayList<>());

        for (String key : jsonPath) {
            JsonNode node = rootNode.get(key);
            if (node != null && node.isArray()) {
                node.elements().forEachRemaining(element -> {
                    findMultipleValue(element, jsonPath.subList(1, jsonPath.size()), Optional.of(accInStr));
                    if (element != null && element.isValueNode())
                        accInStr.add(element.asText());
                });
            } else {
                if (node != null && node.isContainerNode())
                    findMultipleValue(node, jsonPath.subList(1, jsonPath.size()), Optional.of(accInStr));
                if (node != null && node.isValueNode())
                    accInStr.add(node.asText());
            }
        }
        return Optional.of(accInStr);
    }

    private boolean hasParameters(String endpoint) {
        return endpoint.contains("?");
    }
}
