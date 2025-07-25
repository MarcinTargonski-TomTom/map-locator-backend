package com.tomtom.locator.map.map_locator.dto;

import com.tomtom.locator.map.map_locator.model.BudgetType;
import com.tomtom.locator.map.map_locator.model.TravelMode;

public record PointOfInterestDTO(
        PointDTO center,
        int value,
        BudgetType budgetType,
        TravelMode travelMode
) {

}
