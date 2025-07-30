package com.tomtom.locator.map.map_locator.e2e;

import com.tomtom.locator.map.map_locator.config.BaseIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.*;


public class UserSearchPlacesTest extends BaseIntegrationTest {
    @BeforeAll
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    public void userShouldBeAbleToFindPlaces() {
        //given
        var accountUsername = "john_doe";
        var accountEmail = "john.doe@example.com";
        var accountPassword = "password";

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
            .body(String.format("""
                {
                  "login": "%s",
                  "email": "%s",
                  "password": "%s"
                }
            """, accountUsername, accountEmail, accountPassword))
        .when()
            .post("/accounts/register")
        .then()
            .statusCode(HttpStatus.CREATED.value());

        //then can log in
        var response =
        given()
            .contentType(ContentType.JSON)
            .body(String.format("""
                {
                  "login": "%s",
                  "password": "%s"
                }
            """, accountUsername, accountPassword))
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
    }

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
