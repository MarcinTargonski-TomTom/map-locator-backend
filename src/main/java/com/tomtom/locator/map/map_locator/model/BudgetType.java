package com.tomtom.locator.map.map_locator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BudgetType {
    DISTANCE("distanceBudgetInMeters"),
    TIME("timeBudgetInSec"),
    ENERGY("energyBudgetInkWh"),
    FUEL("fuelBudgetInLiters");
    private final String queryParamName;
}
