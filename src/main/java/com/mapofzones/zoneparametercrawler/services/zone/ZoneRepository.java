package com.mapofzones.zoneparametercrawler.services.zone;

import com.mapofzones.zoneparametercrawler.domain.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ZoneRepository extends JpaRepository<Zone, String> {

    @Query(value = "SELECT zn.lcd_addr from zones z " +
            "JOIN zone_nodes zn on z.chain_id = zn.zone and z.chain_id = zone " +
            "    WHERE z.chain_id = ?1 " +
            "        AND zn.last_block_height is not null " +
            "        AND zn.is_lcd_addr_active = true order by zn.last_block_height DESC LIMIT 5", nativeQuery = true)
    List<String> findRestAddressesWithHightestBlockByChainId(String chainId);

    List<Zone> getZoneByIsMainnetTrueOrIsZoneNewTrue();

}
