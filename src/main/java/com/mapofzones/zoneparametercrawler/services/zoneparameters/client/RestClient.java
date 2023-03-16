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
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class RestClient {

    private final RestTemplate restClientRestTemplate;
    private final EndpointsProperties endpointsProperties;

    public RestClient(RestTemplate restClientRestTemplate, EndpointsProperties endpointsProperties) {
        this.restClientRestTemplate = restClientRestTemplate;
        this.endpointsProperties = endpointsProperties;
    }

    public ZoneParametersDto findParameters(String zone, List<String> addresses) {
        ZoneParametersDto zoneParametersDto = new ZoneParametersDto();
        zoneParametersDto.setValidatorsQuantity(findActiveValidatorsQuantity(zone, addresses));
        zoneParametersDto.setBondedTokens(findBondedTokens(addresses));
        zoneParametersDto.setInflation(findInflation(zone, addresses));
        zoneParametersDto.setUnboundPeriod(findUnboundPeriod(addresses));
        return zoneParametersDto;
    }

    public ZoneParametersDto findDelegatorAddresses(String zone, List<String> addresses) {
        ZoneParametersDto zoneParametersDto = new ZoneParametersDto();
        zoneParametersDto.setValidatorsQuantity(findAllValidatorsQuantity(zone, addresses));

        List<String> findValidatorsAddresses = findAllValidatorsAddresses(addresses);

        if (findValidatorsAddresses != null)
            zoneParametersDto.setDelegatorAddresses(findDelegatorAddressesOfAllValidators(addresses, findValidatorsAddresses));
        else zoneParametersDto.setDelegatorAddresses(new HashMap<>());

        return zoneParametersDto;
    }

    public ZoneParametersDto findDelegatorShares(String zone, List<String> addresses) {

        ZoneParametersDto zoneParametersDto = new ZoneParametersDto();
        zoneParametersDto.setValidatorsQuantity(findAllValidatorsQuantity(zone, addresses));

        List<String> findValidatorsAddresses = findAllValidatorsAddresses(addresses);

        if (findValidatorsAddresses != null) {
            zoneParametersDto.setDelegatorShares(findDelegatorSharesOfActiveValidators(addresses, findValidatorsAddresses));
        } else zoneParametersDto.setDelegatorShares(new HashMap<>());

        return zoneParametersDto;
    }

    public ZoneParametersDto findUndelegations(String zone, List<String> addresses) {
        ZoneParametersDto zoneParametersDto = new ZoneParametersDto();
        zoneParametersDto.setValidatorsQuantity(findAllValidatorsQuantity(zone, addresses));

        List<String> findValidatorsAddresses = findAllValidatorsAddresses(addresses);

        if (findValidatorsAddresses != null)
            zoneParametersDto.setValidatorUndelegationMap(findUndelegationsAddressesOfActiveValidators(addresses, findValidatorsAddresses));
        else zoneParametersDto.setValidatorUndelegationMap(new HashMap<>());

        return zoneParametersDto;
    }

    public Long findTotalSupply(List<String> addresses, String denom) {
        String value = (String) callApi(addresses, String.format(endpointsProperties.getSupply(), denom), "/amount/amount", false, 2).orElse(null);
        return value != null ? Long.parseLong(value) : null;
    }

    private Integer findActiveValidatorsQuantity(String zone, List<String> addresses) {

        String value;

        if (zone.equals("stride-1")) {
            value = (String) callApi(List.of("https://stride.nodejumper.io"), "/validators", "/result/total", false, 1).orElse(null);
        } else {
            value = (String) callApi(addresses, endpointsProperties.getActiveValidatorsQuantity(), "/result/total", false, 2).orElse(null);
            if (value == null)
                value = (String) callApi(addresses, endpointsProperties.getActiveValidatorsQuantityExtra(), "/pagination/total", false, 2).orElse(null);
        }

        return value != null ? Integer.parseInt(value) : null;
    }

    private Integer findAllValidatorsQuantity(String zone, List<String> addresses) {

        String value;

        if (zone.equals("stride-1")) {
            value = (String) callApi(List.of("https://stride.nodejumper.io"), "/validators", "/result/total", false, 1).orElse(null);
        } else {
            value = (String) callApi(addresses, endpointsProperties.getAllValidatorsQuantity(), "/pagination/total", false, 2).orElse(null);
        }
        return value != null ? Integer.parseInt(value) : null;
    }

    private List<String> findActiveValidatorsAddresses(List<String> addresses) {
        return (List<String>) callApi(addresses, endpointsProperties.getActiveValidatorList(), "validators/operator_address", true, 2).orElse(null);
    }

    private List<String> findAllValidatorsAddresses(List<String> addresses) {
        return (List<String>) callApi(addresses, endpointsProperties.getAllValidatorList(), "validators/operator_address", true, 2).orElse(null);
    }

    private Map<String, List<String>> findDelegatorAddressesOfAllValidators(List<String> addresses, List<String> validatorAddresses) {
        Map<String, List<String>> delegationsAddressesOfAllValidatorsMap = new HashMap<>();

        ForkJoinPool forkJoinPool = new ForkJoinPool(50);

        Runnable doWork = () -> validatorAddresses.stream().parallel().forEach(vAddr -> {
            List<String> delegatorAddressesOfValidator = (List<String>) callApi(addresses,
                    String.format(endpointsProperties.getDelegations(), vAddr),
                    "delegation_responses/delegation/delegator_address", true, 3).orElse(null);
            if (delegatorAddressesOfValidator != null) {
                delegationsAddressesOfAllValidatorsMap.put(vAddr, delegatorAddressesOfValidator);
            }
        });

        try {
            forkJoinPool.submit(doWork).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            forkJoinPool.shutdown();
        }
        return delegationsAddressesOfAllValidatorsMap;
    }

    private Map<String, List<String>> findUndelegationsAddressesOfActiveValidators(List<String> addresses, List<String> validatorAddresses) {
        Map<String, List<String>> undelegationsAddressesOfActiveValidatorsMap = new HashMap<>();

        ForkJoinPool forkJoinPool = new ForkJoinPool(50);

        Runnable doWork = () -> validatorAddresses.stream().parallel().forEach(vAddr -> {
            List<String> amount1 = (List<String>) callApi(addresses,
                    String.format(endpointsProperties.getUndelegations(), vAddr),
                    "unbonding_responses/entries/initial_balance", true, 3).orElse(null);
            if (amount1 != null) {
                undelegationsAddressesOfActiveValidatorsMap.put(vAddr, amount1);
            }
        });

        try {
            forkJoinPool.submit(doWork).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            forkJoinPool.shutdown();
        }
        return undelegationsAddressesOfActiveValidatorsMap;
    }

    private Map<String, String> findDelegatorSharesOfActiveValidators(List<String> addresses, List<String> validatorAddresses) {
        Map<String, String> delegatorSharesOfActiveValidatorsMap = new HashMap<>();

        for (String currentValidator : validatorAddresses) {
            String shares = (String) callApi(addresses, String.format(endpointsProperties.getValidatorParam(), currentValidator), "/validator/delegator_shares", false, 2).orElse(null);
            delegatorSharesOfActiveValidatorsMap.put(currentValidator, shares);
        }

        return delegatorSharesOfActiveValidatorsMap;
    }

    private String findBondedTokens(List<String> addresses) {
        return (String) callApi(addresses, endpointsProperties.getAmountOfBonded(), "/pool/bonded_tokens", false, 2).orElse(null);
    }

    private Double findInflation(String zone, List<String> addresses) {
        String value;
//        if (zone.equals("axelar-dojo-1")) {
//            value = (String) callApi(List.of("https://api.axelarscan.io"), "/inflation", "/inflation", false, 1).orElse(null);
//        } else
            value = (String) callApi(addresses, endpointsProperties.getInflation(), "/inflation", false, 2).orElse(null);
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
