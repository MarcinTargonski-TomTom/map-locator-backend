package com.tomtom.locator.map.map_locator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MapLocatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MapLocatorApplication.class, args);
	}

}
