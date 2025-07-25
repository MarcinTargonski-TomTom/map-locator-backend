package com.tomtom.locator.map.map_locator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CalculatedRoutes {
    private String formatVersion;
    private ReachableRange reachableRange;
}
