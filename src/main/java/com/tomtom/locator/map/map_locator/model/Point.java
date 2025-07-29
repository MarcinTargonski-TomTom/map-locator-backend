package com.tomtom.locator.map.map_locator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "points")
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
public class Point extends AbstractEntity {
    private double latitude;
    private double longitude;
}
