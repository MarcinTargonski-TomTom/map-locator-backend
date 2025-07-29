package com.tomtom.locator.map.map_locator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "points_of_interest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = false)
@Getter
public class PointOfInterest extends AbstractEntity {
    @OneToOne
    Point center;
    BudgetType budgetType;
    int value;
    TravelMode travelMode;
    private String name;
}
