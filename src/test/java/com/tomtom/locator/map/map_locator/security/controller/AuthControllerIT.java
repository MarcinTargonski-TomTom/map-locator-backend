package com.tomtom.locator.map.map_locator.security.controller;

import com.tomtom.locator.map.map_locator.config.BaseIntegrationTest;
import com.tomtom.locator.map.map_locator.model.Account;
import com.tomtom.locator.map.map_locator.security.model.Credentials;
import com.tomtom.locator.map.map_locator.security.repository.AuthRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerIT extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
            em.createQuery("DELETE FROM Account ").executeUpdate();
            return status;
        });
    }

    @BeforeAll
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Should authenticate with correct credentials")
    void shouldAuthenticateWithCorrectCredentials() {
        var correctPassword = "adminadmin";
        var correctEncryptedCredentials = new Credentials("john123", passwordEncoder.encode(correctPassword));
        var correctCredentials = new Credentials("john123", correctPassword);
        var account = Account.withEmailAndCredentials("email", correctEncryptedCredentials);
        account.activate();

        txTemplate.execute(status -> {
            em.persist(account);
            em.flush();
            return status;
        });

        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(correctCredentials)
                .when()
                    .post("/auth/login")
                .then()
                    .statusCode(HttpStatus.OK.value());
    }
}