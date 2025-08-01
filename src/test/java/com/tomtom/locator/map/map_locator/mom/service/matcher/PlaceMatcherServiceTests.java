package com.tomtom.locator.map.map_locator.mom.service.matcher;

import com.tomtom.locator.map.map_locator.model.Point;
import com.tomtom.locator.map.map_locator.model.Region;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PlaceMatcherServiceTests {

    @InjectMocks
    private PlaceMatcherServiceImpl placeMatcherService;


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
        Region outputRegion = new Region(
                new Point(60.0, 60.0), List.of(
                new Point(50.0, 70.0),
                new Point(70.0, 70.0),
                new Point(70.0, 50.0),
                new Point(50.0, 50.0),
                new Point(50.0, 70.0)));

        assertEquals(1, placeMatcherService.getOverlappingRegions(List.of(inputRegion1, inputRegion2)).size());
        assertEquals(List.of(outputRegion), placeMatcherService.getOverlappingRegions(List.of(inputRegion1, inputRegion2)));
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
        Region outputRegion = new Region(null, List.of());

        //then
        assertEquals(1, placeMatcherService.getOverlappingRegions(List.of(inputRegion1, inputRegion2)).size());
        assertEquals(List.of(outputRegion), placeMatcherService.getOverlappingRegions(List.of(inputRegion1, inputRegion2)));
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
        Region outputRegion = new Region(
                new Point(13.333333333333334, 8.75),
                List.of(
                        new Point(15.0, 11.25),
                        new Point(15.0, 5.0),
                        new Point(10.0, 10.0),
                        new Point(15.0, 11.25)
                )
        );
        Region outputRegion2 = new Region(
                new Point(13.333333333333334, -8.75),
                List.of(
                        new Point(15.0, -5.0),
                        new Point(15.0, -11.25),
                        new Point(10.0, -10.0),
                        new Point(15.0, -5.0)
                )
        );

        assertEquals(List.of(outputRegion, outputRegion2), placeMatcherService.getOverlappingRegions(List.of(inputRegion1, inputRegion2)));
    }
}
