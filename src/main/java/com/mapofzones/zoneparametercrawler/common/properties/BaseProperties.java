package com.mapofzones.zoneparametercrawler.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "zone-parameters")
public class BaseProperties {

    private Duration syncTime;

}
