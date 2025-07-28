package com.tomtom.locator.map.map_locator.mok;

import com.tomtom.locator.map.map_locator.config.BaseIntegrationTest;
import com.tomtom.locator.map.map_locator.mok.dto.NewAccountDto;
import com.tomtom.locator.map.map_locator.mok.model.Account;
import com.tomtom.locator.map.map_locator.mok.model.Credentials;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountServiceImplIT extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private AccountRepository accountRepository;

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
    @DisplayName("Should create new not enabled account")
    @Transactional
    void shouldCreateNewNotEnabledAccount() {
        String givenLogin = "newuser";
        var newAccount = new NewAccountDto(givenLogin, "newuser@example.com", "securePassword");

        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(newAccount)
                .when()
                    .post("/accounts/register")
                .then()
                    .statusCode(HttpStatus.CREATED.value());

        Account account = accountRepository.findByLogin(givenLogin)
                .orElseThrow();
        assertThat(account.isEnabled())
                .isFalse();
    }

    @Test
    @DisplayName("Should authenticate with correct credentials")
    void shouldAuthenticateWithCorrectCredentials() {
        var correctPassword = "adminadmin";
        var correctEncryptedCredentials = new Credentials("john123", passwordEncoder.encode(correctPassword));
        var correctCredentials = new Credentials("john123", correctPassword);
        var account = Account.withEmailAndCredentials("email", correctEncryptedCredentials);
        account.activate();

        txTemplate.execute(status ->
            accountRepository.saveAndFlush(account)
        );

        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(correctCredentials)
                .when()
                    .post("/accounts/auth")
                .then()
                    .statusCode(HttpStatus.OK.value());
    }
}