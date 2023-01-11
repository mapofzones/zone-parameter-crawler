package com.mapofzones.zoneparametercrawler.services;

import com.mapofzones.zoneparametercrawler.domain.Zone;
import com.mapofzones.zoneparametercrawler.domain.ZoneParameters;
import com.mapofzones.zoneparametercrawler.services.zone.ZoneRepository;
import com.mapofzones.zoneparametercrawler.services.zoneparameters.IZoneParametersService;
import com.mapofzones.zoneparametercrawler.utils.TimeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class ZoneParametersFacade implements IZoneParametersFacade {

    private final IZoneParametersService zoneParametersService;
    private final ZoneRepository zoneRepository;

    public ZoneParametersFacade(IZoneParametersService zoneParametersService,
                                ZoneRepository zoneRepository) {
        this.zoneParametersService = zoneParametersService;
        this.zoneRepository = zoneRepository;
    }


    @Async
    @Override
    @Transactional
    public void createEmptyZoneParameters() {
        log.info("Start CreateEmptyZoneParameters...");
        List<Zone> zones = zoneRepository.getZoneByIsMainnetTrue();
        List<ZoneParameters> zoneParameters = new ArrayList<>();

        zones.forEach(zone -> {
            ZoneParameters.ZoneParametersId zoneParametersId = new ZoneParameters.ZoneParametersId();
            zoneParametersId.setZone(zone.getChainId());
            zoneParametersId.setDatetime(TimeHelper.nowAroundHours());
            zoneParameters.add(new ZoneParameters(zoneParametersId));

        });
        zoneParametersService.saveAll(zoneParameters);
        log.info("Finish CreateEmptyZoneParameters...");
    }

    @Async
    @Override
    public void findBaseZoneParameters() {
        List<ZoneParameters> zoneParameters = zoneParametersService.findEmptyZoneParameters();
        log.info("Start FindBaseZoneParameters...");

        Map<ZoneParameters, List<String>> zoneParametersAddressesMap = zoneParameters.stream()
                .collect(Collectors.toConcurrentMap(Function.identity(), key ->
                        zoneRepository.findRestAddressesWithHightestBlockByChainId(key.getZoneParametersId().getZone())));

        Runnable getBaseZoneParametersTask = () -> zoneParametersAddressesMap.entrySet().stream().parallel().forEach(entry -> {
            if (!entry.getValue().isEmpty()) {
                log.info("FindBaseZoneParameters: Started: " + entry.getKey().getZoneParametersId().getZone());
                zoneParametersService.findBaseZoneParametersFromAddresses(entry.getKey(), entry.getValue());
                log.info("FindBaseZoneParameters: FINISHED: " + entry.getKey().getZoneParametersId().getZone());
            }
        });

        findZoneParameters(getBaseZoneParametersTask);
        zoneParametersService.saveBaseParameters(zoneParameters);
        log.info("Finish FindBaseZoneParameters...");
    }

    @Async
    @Override
    public void findDelegationsAmount() {
        List<ZoneParameters> zoneParameters = zoneParametersService.findEmptyZoneParameters();
        log.info("Start FindDelegationsAmount...");

        Map<ZoneParameters, List<String>> zoneParametersAddressesMap = zoneParameters.stream()
                .collect(Collectors.toConcurrentMap(Function.identity(), key ->
                        zoneRepository.findRestAddressesWithHightestBlockByChainId(key.getZoneParametersId().getZone())));

        Runnable getBaseZoneParametersTask = () -> zoneParametersAddressesMap.entrySet().stream().parallel().forEach(entry -> {
            if (!entry.getValue().isEmpty()) {
                log.info("FindDelegationsAmount: Started: " + entry.getKey().getZoneParametersId().getZone());
                if (entry.getKey() != null && entry.getValue() != null)
                    zoneParametersService.findDelegationsAmountFromAddresses(entry.getKey(), entry.getValue());
                log.info("FindDelegationsAmount: FINISHED: " + entry.getKey().getZoneParametersId().getZone());
            }
        });

        findZoneParameters(getBaseZoneParametersTask);
        zoneParametersService.saveDelegationAmount(zoneParameters);
        log.info("Finish FindDelegationsAmount...");


    }

    @Override
    @Async
    public void findUndelegationsAmount() {
        List<ZoneParameters> zoneParameters = zoneParametersService.findEmptyZoneParameters();
        log.info("Start FindUndelegationsAmount...");

        Map<ZoneParameters, List<String>> zoneParametersAddressesMap = zoneParameters.stream()
                .collect(Collectors.toConcurrentMap(Function.identity(), key ->
                        zoneRepository.findRestAddressesWithHightestBlockByChainId(key.getZoneParametersId().getZone())));

        Runnable getBaseZoneParametersTask = () -> zoneParametersAddressesMap.entrySet().stream().parallel().forEach(entry -> {
            if (!entry.getValue().isEmpty()) {
                log.info("FindUndelegationsAmount: Started: " + entry.getKey().getZoneParametersId().getZone());
                if (entry.getKey() != null && entry.getValue() != null)
                    zoneParametersService.findUndelegationsAmountFromAddresses(entry.getKey(), entry.getValue());
                log.info("FindUndelegationsAmount: FINISHED: " + entry.getKey().getZoneParametersId().getZone());
            }
        });

        findZoneParameters(getBaseZoneParametersTask);
        zoneParametersService.saveUndelegationAmount(zoneParameters);
        log.info("Finish FindUndelegationsAmount...");

    }

    @Override
    @Async
    public void findDelegatorAddressesCount() {
        List<ZoneParameters> zoneParameters = zoneParametersService.findEmptyZoneParameters();
        log.info("Start FindDelegatorAddressesCount...");

        Map<ZoneParameters, List<String>> zoneParametersAddressesMap = zoneParameters.stream()
                .collect(Collectors.toConcurrentMap(Function.identity(), key ->
                        zoneRepository.findRestAddressesWithHightestBlockByChainId(key.getZoneParametersId().getZone())));

        Runnable getBaseZoneParametersTask = () -> zoneParametersAddressesMap.entrySet().stream().parallel().forEach(entry -> {
            if (!entry.getValue().isEmpty()) {
                log.info("FindDelegatorAddressesCount: Started: " + entry.getKey().getZoneParametersId().getZone());
                if (entry.getKey() != null && entry.getValue() != null)
                    zoneParametersService.findDelegatorAddressesCountFromAddresses(entry.getKey(), entry.getValue());
                log.info("FindDelegatorAddressesCount: FINISHED: " + entry.getKey().getZoneParametersId().getZone());
            }
        });

        findZoneParameters(getBaseZoneParametersTask);
        zoneParametersService.saveDelegatorAddressesCount(zoneParameters);
        log.info("Finish FindDelegatorAddressesCount...");

    }

    protected void findZoneParameters(Runnable function) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(15);

        try {
            forkJoinPool.submit(function).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            forkJoinPool.shutdown();
        }
    }

}
