package com.tomtom.locator.map.map_locator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PointOfInterest {
    Point center;
    BudgetType budgetType;
    int value;
    TravelMode travelMode;
}
