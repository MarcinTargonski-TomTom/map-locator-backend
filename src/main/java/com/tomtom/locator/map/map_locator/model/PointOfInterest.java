package com.tomtom.locator.map.map_locator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    @Setter
    Point center;
    BudgetType budgetType;
    int value;
    TravelMode travelMode;
    private String name;

    /**
     * Returns the query string with spaces replaced by '+'.
     * This is necessary for URL encoding when making requests to the search API.
     *
     * @return the formatted query string
     */
    public String unifyQuery() {
        return name.replace(" ", "+");
    }
}
