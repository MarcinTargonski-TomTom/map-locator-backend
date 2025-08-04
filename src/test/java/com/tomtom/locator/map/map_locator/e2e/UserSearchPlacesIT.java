package com.tomtom.locator.map.map_locator.e2e;

import com.tomtom.locator.map.map_locator.config.BaseE2ETest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class UserSearchPlacesIT extends BaseE2ETest {

    @Test
    public void userShouldBeAbleToFindPlaces() {
        //expect to not allow to search when not authenticated
        given()
            .contentType(ContentType.JSON)
            .body(EXAMPLE_POI_LIST_JSON)
        .when()
            .post("/locations/v1/matchLocation")
        .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());

        //when register account
        given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "login", ACCOUNT_USERNAME,
                        "email", ACCOUNT_EMAIL,
                        "password", ACCOUNT_PASSWORD
                ))
        .when()
            .post("/accounts/register")
        .then()
            .statusCode(HttpStatus.CREATED.value());

        //then can log in
        var response =
        given()
            .contentType(ContentType.JSON)
            .body(Map.of(
                    "login", ACCOUNT_USERNAME,
                    "password", ACCOUNT_PASSWORD
            ))
        .when()
            .post("/auth/login")
        .then()
            .statusCode(HttpStatus.OK.value())
        .extract();

        //and
        var authToken = response.body().jsonPath().getString("auth");
        Assertions.assertNotNull(authToken);

        //expect to allow search when authenticated
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + authToken)
            .body(EXAMPLE_POI_LIST_JSON)
        .when()
            .post("/locations/v1/matchLocation")
        .then()
            .statusCode(HttpStatus.OK.value());

        //expect to get history of searches after w8 for async processing
        await().atMost(Duration.ofSeconds(3)).untilAsserted(() ->
            given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + authToken)
                    .when()
                    .get("/locations/v1/accountLocations")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", greaterThanOrEqualTo(1))
                    .body("[0].requestRegions[0].pointOfInterest.name", equalTo("Central Park"))
                    .body("[0].requestRegions[0].pointOfInterest.value", equalTo(100))
                    .body("[0].requestRegions[0].pointOfInterest.travelMode", equalTo("CAR"))
                    .body("[0].requestRegions[0].pointOfInterest.budgetType", equalTo("DISTANCE")));
    }

    @BeforeAll
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    private static final String ACCOUNT_USERNAME = "john_doe";
    private static final String ACCOUNT_EMAIL = "john.doe@example.com";
    private static final String ACCOUNT_PASSWORD = "password";
    private static final String EXAMPLE_POI_LIST_JSON = """
            [
                {
                  "name": "Central Park",
                  "center": {
                    "latitude": 40.785091,
                    "longitude": -73.968285
                  },
                  "value": 100,
                  "budgetType": "DISTANCE",
                  "travelMode": "CAR"
                }
            ]
            """;
}
