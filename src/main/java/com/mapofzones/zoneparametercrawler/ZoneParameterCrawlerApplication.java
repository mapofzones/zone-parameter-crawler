package com.mapofzones.zoneparametercrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZoneParameterCrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZoneParameterCrawlerApplication.class, args);
	}

}
