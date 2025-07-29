package com.tomtom.locator.map.map_locator.mom.service.matcher;

import com.tomtom.locator.map.map_locator.model.BudgetType;
import com.tomtom.locator.map.map_locator.model.CalculatedRoute;
import com.tomtom.locator.map.map_locator.model.LocationMatch;
import com.tomtom.locator.map.map_locator.model.Point;
import com.tomtom.locator.map.map_locator.model.PointOfInterest;
import com.tomtom.locator.map.map_locator.model.Region;
import com.tomtom.locator.map.map_locator.model.TravelMode;
import com.tomtom.locator.map.map_locator.mom.service.map.MapService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlaceMatcherServiceTests {

    @InjectMocks
    private PlaceMatcherServiceImpl placeMatcherService;

    @Mock
    private MapService mapService;

    @Test
    void shouldReturnRectangle() {
        //given
        Point point1 = new Point(50.0, 50.0);
        Point point2 = new Point(70.0, 70.0);
        Region inputRegion1 = new Region(point1, List.of(
                new Point(30.0, 30.0),
                new Point(70.0, 30.0),
                new Point(70.0, 70.0),
                new Point(30.0, 70.0)
        ));
        Region inputRegion2 = new Region(point2, List.of(
                new Point(50.0, 50.0),
                new Point(90.0, 50.0),
                new Point(90.0, 90.0),
                new Point(50.0, 90.0)
        ));
        List<Region> outputRegions = List.of(new Region(null, List.of(
                new Point(50.0, 70.0),
                new Point(70.0, 70.0),
                new Point(70.0, 50.0),
                new Point(50.0, 50.0),
                new Point(50.0, 70.0))));
        LocationMatch expected = new LocationMatch(List.of(inputRegion1, inputRegion2), (outputRegions));
        PointOfInterest poi1 = new PointOfInterest(point1, BudgetType.DISTANCE, 20, TravelMode.PEDESTRIAN);
        PointOfInterest poi2 = new PointOfInterest(point2, BudgetType.DISTANCE, 20, TravelMode.PEDESTRIAN);
        when(mapService.getRegionForPoint(poi1)).thenReturn(new CalculatedRoute("0.0.1",
                inputRegion1));
        when(mapService.getRegionForPoint(poi2)).thenReturn(new CalculatedRoute("0.0.1",
                inputRegion2));
        //when
        LocationMatch regionMatches = placeMatcherService.findRegionForPlaces(List.of(poi1, poi2));
        //then
        assertEquals(expected, regionMatches);
    }

    @Test
    void shouldReturnNull() {
        //given
        Point point1 = new Point(50.0, 50.0);
        Point point2 = new Point(70.0, 70.0);
        Region inputRegion1 = new Region(point1, List.of(
                new Point(45.0, 45.0),
                new Point(55.0, 45.0),
                new Point(55.0, 55.0),
                new Point(45.0, 55.0)
        ));
        Region inputRegion2 = new Region(point2, List.of(
                new Point(65.0, 65.0),
                new Point(75.0, 65.0),
                new Point(75.0, 75.0),
                new Point(65.0, 75.0)
        ));
        LocationMatch expected = new LocationMatch(List.of(inputRegion1, inputRegion2), List.of(new Region(null, List.of())));
        PointOfInterest poi1 = new PointOfInterest(point1, BudgetType.DISTANCE, 5, TravelMode.PEDESTRIAN);
        PointOfInterest poi2 = new PointOfInterest(point2, BudgetType.DISTANCE, 5, TravelMode.PEDESTRIAN);
        when(mapService.getRegionForPoint(poi1)).thenReturn(new CalculatedRoute("0.0.1",
                inputRegion1));
        when(mapService.getRegionForPoint(poi2)).thenReturn(new CalculatedRoute("0.0.1",
                inputRegion2));
        //when
        LocationMatch regionMatches = placeMatcherService.findRegionForPlaces(List.of(poi1, poi2));
        //then
        assertEquals(expected, regionMatches);
    }

    @Test
    void shouldReturnTwoPolygons() {
        //given
        Point point1 = new Point(50.0, 50.0);
        Point point2 = new Point(70.0, 70.0);
        Region inputRegion1 = new Region(point1, List.of(
                new Point(0.0, 0.0),
                new Point(15.0, 15.0),
                new Point(15.0, -15.0)
        ));
        Region inputRegion2 = new Region(point2, List.of(
                new Point(10.0, -10.0),
                new Point(20.0, 0.0),
                new Point(10.0, 10.0),
                new Point(30.0, 15.0),
                new Point(30.0, -15.0)
        ));
        Region outputRegion1 = new Region(
                null,
                List.of(
                        new Point(15.0, 11.25),
                        new Point(15.0, 5.0),
                        new Point(10.0, 10.0),
                        new Point(15.0, 11.25)
                )
        );
        Region outputRegion2 = new Region(
                null,
                List.of(
                        new Point(15.0, -5.0),
                        new Point(15.0, -11.25),
                        new Point(10.0, -10.0),
                        new Point(15.0, -5.0)
                )
        );
        List<Region> outputRegions = List.of(outputRegion1, outputRegion2);
        LocationMatch expected = new LocationMatch(List.of(inputRegion1, inputRegion2), outputRegions);
        PointOfInterest poi1 = new PointOfInterest(point1, BudgetType.DISTANCE, 5, TravelMode.PEDESTRIAN);
        PointOfInterest poi2 = new PointOfInterest(point2, BudgetType.DISTANCE, 5, TravelMode.PEDESTRIAN);
        when(mapService.getRegionForPoint(poi1)).thenReturn(new CalculatedRoute("0.0.1",
                inputRegion1));
        when(mapService.getRegionForPoint(poi2)).thenReturn(new CalculatedRoute("0.0.1",
                inputRegion2));
        //when
        LocationMatch regionMatches = placeMatcherService.findRegionForPlaces(List.of(poi1, poi2));
        //then
        assertEquals(expected, regionMatches);
    }
}
