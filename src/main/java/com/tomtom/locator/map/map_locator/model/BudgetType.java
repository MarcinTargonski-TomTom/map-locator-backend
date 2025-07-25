package com.tomtom.locator.map.map_locator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BudgetType {
    DISTANCE("distanceBudgetInMeters"), TIME("timeBudgetInSec");
    private final String queryParamName;
}
