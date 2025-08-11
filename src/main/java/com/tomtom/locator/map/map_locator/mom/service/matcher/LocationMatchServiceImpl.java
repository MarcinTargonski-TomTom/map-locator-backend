package com.tomtom.locator.map.map_locator.mom.service.matcher;

import com.tomtom.locator.map.map_locator.exception.EmptyListException;
import com.tomtom.locator.map.map_locator.logger.MethodCallLogged;
import com.tomtom.locator.map.map_locator.model.Account;
import com.tomtom.locator.map.map_locator.model.LocationMatch;
import com.tomtom.locator.map.map_locator.model.MortonTileMatcher;
import com.tomtom.locator.map.map_locator.model.Point;
import com.tomtom.locator.map.map_locator.model.Region;
import com.tomtom.locator.map.map_locator.mok.repository.AccountRepository;
import com.tomtom.locator.map.map_locator.mom.repository.LocationMatchRepository;
import com.tomtom.locator.map.map_locator.mom.repository.MortonTileMatcherRepository;
import com.tomtom.locator.map.map_locator.mom.repository.PointOfInterestRepository;
import com.tomtom.locator.map.map_locator.mom.service.matcher.converter.PolygonConverter;
import com.tomtom.tti.nida.morton.geom.GeoBox;
import com.tomtom.tti.nida.morton.geom.MortonTile;
import com.tomtom.tti.nida.morton.geom.MortonTileLevel;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor
@MethodCallLogged
class LocationMatchServiceImpl implements LocationMatchService {

    private final AccountRepository accountRepository;
    private final LocationMatchRepository locationMatchRepository;
    private final PointOfInterestRepository pointOfInterestRepository;
    private final MortonTileMatcherRepository mortonTileMatcherRepository;
    private final PolygonConverter polygonConverter;
    private final double FILL_PERCENT = 0.35;

    @Async
    @Override
    public CompletableFuture<List<LocationMatch>> addToAccount(String login, List<LocationMatch> locationMatches) {
        updateHeatMap(locationMatches);
        Account account = accountRepository.findByLogin(login)
                .orElseThrow();

        List<LocationMatch> accountLocationMatches = locationMatches
                .stream()
                .map(locationMatch -> {
                    locationMatch.setAccount(account);
                    return locationMatch;
                })
                .toList();

        List<LocationMatch> saved = locationMatchRepository.saveAllAndFlush(accountLocationMatches);
        return CompletableFuture.completedFuture(saved);
    }

    @Override
    public List<LocationMatch> getAccountLocations(String login) {
        return locationMatchRepository.findAllByAccount_login(login);
    }

    @Async
    @Override
    public void updateHeatMap(List<LocationMatch> locationMatches) {
        if (locationMatches == null || locationMatches.isEmpty()) {
            throw new EmptyListException();
        }
        Map<Long, MortonTileMatcher> matchesFromDB = mortonTileMatcherRepository.findAll()
                .stream()
                .collect(Collectors.toMap(MortonTileMatcher::getMortonCode, Function.identity()));

        for (LocationMatch locationMatch : locationMatches) {
            if (!locationMatch.getResponseRegion().getBoundary().isEmpty()) {

                MortonTileLevel<MortonTile.M16> mortonTileLevel = MortonTileLevel.M16;

                List<MortonTile<?>> allMortonTiles = new ArrayList<>();
                List<GeoBox> optimizedGeoBoxes = createOptimizedGeoBoxes(locationMatch.getResponseRegion());

                for (GeoBox geoBox : optimizedGeoBoxes) {
                    List<MortonTile<?>> mortonTiles = mortonTileLevel.getMortonTileCoverage(geoBox).toList();
                    allMortonTiles.addAll(mortonTiles);
                }

                List<MortonTile<?>> uniqueMortonTiles = allMortonTiles.stream()
                        .collect(Collectors.toMap(
                                MortonTile::getCode,
                                Function.identity(),
                                (existing, replacement) -> existing))
                        .values()
                        .stream()
                        .toList();

                uniqueMortonTiles.forEach(
                        mortonTile -> findMatchOrAddNewMortonTileMatcher(mortonTile.getCode(), matchesFromDB)
                );
            }
        }
        mortonTileMatcherRepository.saveAllAndFlush(matchesFromDB.values().stream().toList());
    }

    private List<GeoBox> createOptimizedGeoBoxes(Region region) {
        List<GeoBox> geoBoxes = new ArrayList<>();

        Polygon polygon = polygonConverter.toPolygon(region);
        Envelope envelope = polygon.getEnvelopeInternal();

        double width = envelope.getWidth();
        double height = envelope.getHeight();

        int divisionsX = Math.min(4, Math.max(1, (int) Math.ceil(width / 0.01)));
        int divisionsY = Math.min(4, Math.max(1, (int) Math.ceil(height / 0.01)));

        double stepX = width / divisionsX;
        double stepY = height / divisionsY;

        GeometryFactory geomFactory = new GeometryFactory();

        for (int i = 0; i < divisionsX; i++) {
            for (int j = 0; j < divisionsY; j++) {
                double minX = envelope.getMinX() + i * stepX;
                double maxX = envelope.getMinX() + (i + 1) * stepX;
                double minY = envelope.getMinY() + j * stepY;
                double maxY = envelope.getMinY() + (j + 1) * stepY;

                Coordinate[] rectCoords = new Coordinate[]{
                        new Coordinate(minX, minY),
                        new Coordinate(maxX, minY),
                        new Coordinate(maxX, maxY),
                        new Coordinate(minX, maxY),
                        new Coordinate(minX, minY)
                };

                Polygon rect = geomFactory.createPolygon(rectCoords);

                if (polygon.intersects(rect)) {
                    Geometry intersection = polygon.intersection(rect);

                    double intersectionArea = intersection.getArea();
                    double rectArea = rect.getArea();

                    if (intersectionArea / rectArea > FILL_PERCENT) {
                        List<Point> boundaryPoints = new ArrayList<>();
                        boundaryPoints.add(new Point(minX, minY));
                        boundaryPoints.add(new Point(maxX, minY));
                        boundaryPoints.add(new Point(maxX, maxY));
                        boundaryPoints.add(new Point(minX, maxY));

                        GeoBox geoBox = GeoBox.of(boundaryPoints);
                        geoBoxes.add(geoBox);
                    }
                }
            }
        }

        if (geoBoxes.isEmpty()) {
            geoBoxes.add(GeoBox.of(region.getBoundary()));
        }

        return geoBoxes;
    }

    private void findMatchOrAddNewMortonTileMatcher(long mortonCode, Map<Long, MortonTileMatcher> matchesFromDB) {
        MortonTileMatcher foundMatch = matchesFromDB.getOrDefault(getMortonCodeAsQuaternary(mortonCode), new MortonTileMatcher(mortonCode, 0));
        foundMatch.incrementOccurrences();
        matchesFromDB.put(foundMatch.getMortonCode(), foundMatch);
    }

    private Long getMortonCodeAsQuaternary(long mortonCode) {
        return Long.valueOf(Long.toString(mortonCode, 4));
    }
}
