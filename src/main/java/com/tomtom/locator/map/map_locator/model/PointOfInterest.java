package com.tomtom.locator.map.map_locator.model;

import lombok.Data;

@Data
public class PointOfInterest {
    Point center;
    BudgetType budgetType;
    int value;
    TravelMode travelMode;
}
