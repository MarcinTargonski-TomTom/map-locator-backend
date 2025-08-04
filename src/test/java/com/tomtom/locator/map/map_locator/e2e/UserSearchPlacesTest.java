package com.tomtom.locator.map.map_locator.e2e;

import com.tomtom.locator.map.map_locator.config.BaseE2ETest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserSearchPlacesTest extends BaseE2ETest {
    @Test
    public void userShouldBeAbleToFindPlaces() {
        //expect to not allow to search when not authenticated
        given()
            .contentType(ContentType.JSON)
            .body(EXAMPLE_POI_LIST_JSON)
        .when()
            .post("/locations/v1/matchLocation")
        .then()
            .statusCode(HttpStatus.FORBIDDEN.value());//FIXME - should be 401 rather than 403
//            .statusCode(HttpStatus.UNAUTHORIZED.value());

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

        //and expect to get historical location matches
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + authToken)
        .when()
            .get("/locations/v1/accountLocations")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("size()", org.hamcrest.Matchers.greaterThan(0));
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
                "value": 1200,
                "budgetType": "DISTANCE",
                "travelMode": "CAR",
                "center": {
                  "latitude": 51.77898167145611,
                  "longitude": 19.480903494554923
                }
              },
              {
                "value": 1200,
                "budgetType": "DISTANCE",
                "travelMode": "CAR",
                "center": {
                  "latitude": 51.77691661006759,
                  "longitude": 19.455535847660116
                }
              }
            ]
            """;
//    private static final String EXAMPLE_POI_LIST_JSON = """
//            [
//                {
//                  "name": "Central Park",
//                  "center": {
//                    "latitude": 40.785091,
//                    "longitude": -73.968285
//                  },
//                  "value": 100,
//                  "budgetType": "DISTANCE",
//                  "travelMode": "CAR"
//                },
//                {
//                  "name": "North Hudson Park",
//                  "center": {
//                    "latitude": 40.80083,
//                    "longitude": -73.99738
//                  },
//                  "value": 10,
//                  "budgetType": "DISTANCE",
//                  "travelMode": "CAR"
//                },
//                {
//                  "name": "George Washington Bridge",
//                  "value": 20,
//                  "budgetType": "DISTANCE",
//                  "travelMode": "CAR"
//                }
//            ]
//            """;
}
