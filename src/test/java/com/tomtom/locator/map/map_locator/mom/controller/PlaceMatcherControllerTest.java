package com.tomtom.locator.map.map_locator.mom.controller;

import com.tomtom.locator.map.map_locator.config.BaseIntegrationTest;
import com.tomtom.locator.map.map_locator.model.Account;
import com.tomtom.locator.map.map_locator.model.BudgetType;
import com.tomtom.locator.map.map_locator.model.TravelMode;
import com.tomtom.locator.map.map_locator.mom.dto.PointDTO;
import com.tomtom.locator.map.map_locator.mom.dto.PointOfInterestDTO;
import com.tomtom.locator.map.map_locator.security.jwt.JwtHelper;
import com.tomtom.locator.map.map_locator.security.model.Credentials;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlaceMatcherControllerIT extends BaseIntegrationTest {

    public static final String MATCH_LOCATION_PATH = "/locations/v1/matchLocation";

    @LocalServerPort
    private int port;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    private EntityManager em;

    private TransactionTemplate txTemplate;

    @BeforeEach
    void setUp() {
        txTemplate = new TransactionTemplate(txManager);
    }

    @AfterEach
    void tearDown() {
        txTemplate.execute(status -> {
            em.createQuery("DELETE FROM Account").executeUpdate();
            em.createQuery("DELETE FROM LocationMatch").executeUpdate();
            em.createQuery("DELETE FROM Region").executeUpdate();
            em.createQuery("DELETE FROM Point").executeUpdate();

            return status;
        });
    }

    @BeforeAll
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Should add location match to account")
    void shouldCreateNewNotEnabledAccount() {
        var point = new PointDTO(21, 37);
        var poi = new PointOfInterestDTO(point, 10, BudgetType.TIME, TravelMode.CAR);
        var givenRequest = List.of(poi);

        var givenLogin = "user";
        var account = Account.withEmailAndCredentials("email", new Credentials(givenLogin, "password"));
        account.activate();

        txTemplate.execute(status -> {
            em.persist(account);
            em.flush();
            return status;
        });
        String givenToken = jwtHelper.generateAuthTokenForAnAccount(account);


        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + givenToken)
                .body(givenRequest)
                .when()
                .post(MATCH_LOCATION_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract();

        txTemplate.execute(status -> {
            Account a = em.find(Account.class, account.getId());
            em.flush();
            assertThat(a.getLocationMatches())
                    .hasSize(1);

            return status;
        });


    }
}