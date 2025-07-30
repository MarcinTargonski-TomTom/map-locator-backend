package com.tomtom.locator.map.map_locator.e2e;

import com.tomtom.locator.map.map_locator.config.BaseIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.*;

public class UserSearchPlacesTest extends BaseIntegrationTest {
    @Test
    public void userShouldBeAbleToFindPlaces() {
        //given
        var accountUsername = "john_doe";
        var accountEmail = "john.doe@example.com";
        var accountPassword = "password";

        //expect to not allow to search when not authenticated
        given()
            .contentType(ContentType.JSON)
            .body("""
            {
              "name": "Central Park",
              "center": {
                "latitude": 40.785091,
                "longitude": -73.968285
              },
              "value": 100,
              "budgetType": "PREMIUM",
              "travelMode": "CAR"
            }
            """)
        .when()
            .post("/locations/v1/matchLocation")
        .then()
        .   statusCode(HttpStatus.UNAUTHORIZED.value());

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
            .statusCode(HttpStatus.OK.value());
    }
}
