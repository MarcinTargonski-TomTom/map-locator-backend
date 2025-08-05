package com.tomtom.locator.map.map_locator.model;

import com.tomtom.tti.nida.morton.geom.Position;
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
public class Point extends AbstractEntity implements Position {
    private double latitude;
    private double longitude;
    private final static int MICRO_DEGREE_FACTOR = 1_000_000;

    @Override
    public int getLonMicroDegrees() {
        return (int) (longitude * MICRO_DEGREE_FACTOR);
    }

    @Override
    public int getLatMicroDegrees() {
        return (int) (latitude * MICRO_DEGREE_FACTOR);
    }
}
