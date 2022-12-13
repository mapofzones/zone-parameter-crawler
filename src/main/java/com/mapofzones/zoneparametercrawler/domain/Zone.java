package com.mapofzones.zoneparametercrawler.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "zones", schema = "public")
public class Zone {

    @Id
    @Column(name = "chain_id")
    private String chainId;

    @Column(name = "is_mainnet")
    private Boolean isMainnet;
}