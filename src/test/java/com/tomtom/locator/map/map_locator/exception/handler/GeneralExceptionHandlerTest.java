package com.tomtom.locator.map.map_locator.exception.handler;

import com.tomtom.locator.map.map_locator.exception.AccountNotActiveException;
import com.tomtom.locator.map.map_locator.exception.AppBaseException;
import com.tomtom.locator.map.map_locator.exception.ExceptionMessage;
import com.tomtom.locator.map.map_locator.exception.ExpiredTokenException;
import com.tomtom.locator.map.map_locator.exception.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GeneralExceptionHandlerTest {

    private final GeneralExceptionHandler underTest = new GeneralExceptionHandler();

    @DisplayName("Should handle unexpected errors and return a 500 response")
    @Test
    void handleUnexpectedError() {
        // Given
        var exception = new Exception("Unexpected error");
        var request = mock(HttpServletRequest.class);
        given(request.getServletPath()).willReturn("/api/test");

        // When
        var response = underTest.handleUnexpectedError(exception, request);

        // Then
        var body = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(body).isNotNull();
        assertThat(body.message()).isEqualTo(ExceptionMessage.UNKNOWN);
        assertThat(body.path()).isEqualTo("/api/test");
        assertThat(body.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(body.timestamp()).isBeforeOrEqualTo(ZonedDateTime.now());
    }

    @Test
    void handleValidationException() {
        // Given
        var request = mock(HttpServletRequest.class);
        given(request.getServletPath()).willReturn("/api/test");

        var bindingResult = mock(org.springframework.validation.BindingResult.class);
        var fieldError = new FieldError("objectName", "fieldName", "Field must not be empty");
        given(bindingResult.getFieldErrors()).willReturn(List.of(fieldError));

        var exception = new MethodArgumentNotValidException(null, bindingResult);

        // When
        var response = underTest.handleValidationException(exception, request);

        // Then
        FieldValidationError[] expectedErrors = Stream.of(fieldError)
                .map(error -> new FieldValidationError(error.getField(), error.getDefaultMessage()))
                .toArray(FieldValidationError[]::new);
        var body = response.getBody();
        var basicException = body.exception();

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(body)
                .isNotNull();
        assertThat(basicException.message())
                .isEqualTo(ExceptionMessage.VALIDATION_FAILED);
        assertThat(basicException.path())
                .isEqualTo("/api/test");
        assertThat(basicException.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(basicException.timestamp())
                .isBeforeOrEqualTo(ZonedDateTime.now());
        assertThat(body.fieldErrors())
                .contains(expectedErrors);
    }

    @DisplayName("Should handle malformed JSON exceptions")
    @Test
    void handleMalformedJson() {
        // Given
        var exception = new HttpMessageNotReadableException("Unexpected error");
        var request = mock(HttpServletRequest.class);
        given(request.getServletPath()).willReturn("/api/test");

        // When
        var response = underTest.handleMalformedJson(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        var body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.message()).isEqualTo(ExceptionMessage.MALFORMED_JSON);
        assertThat(body.path()).isEqualTo("/api/test");
        assertThat(body.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(body.timestamp()).isBeforeOrEqualTo(ZonedDateTime.now());
    }

    @DisplayName("Should handle application-specific exceptions")
    @ParameterizedTest
    @MethodSource("exceptions")
    void handleAppExceptions(AppBaseException exception) {
        // Given
        var request = mock(HttpServletRequest.class);
        given(request.getServletPath()).willReturn("/api/test");

        // When
        var response = underTest.handleAppExceptions(exception, request);

        // Then
        var body = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(exception.getStatusCode());
        assertThat(body).isNotNull();
        assertThat(body.message()).isEqualTo(exception.getMessage());
        assertThat(body.path()).isEqualTo("/api/test");
        assertThat(body.statusCode()).isEqualTo(exception.getStatusCode());
        assertThat(body.timestamp()).isBeforeOrEqualTo(ZonedDateTime.now());
    }

    @DisplayName("Should handle AuthorizationDeniedException errors and return a 403 response")
    @Test
    void handleAuthorizationDeniedError() {
        // Given
        var exception = new AuthorizationDeniedException("Unexpected error");
        var request = mock(HttpServletRequest.class);
        given(request.getServletPath()).willReturn("/api/test");

        // When
        var response = underTest.handleAuthorizationDeniedException(exception, request);

        // Then
        var body = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(body).isNotNull();
        assertThat(body.message()).isEqualTo(ExceptionMessage.ACCESS_DENIED);
        assertThat(body.path()).isEqualTo("/api/test");
        assertThat(body.statusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(body.timestamp()).isBeforeOrEqualTo(ZonedDateTime.now());
    }

    private static Stream<AppBaseException> exceptions() {
        return Stream.of(
                InvalidTokenException.withDefaultMsgAndCause(null),
                ExpiredTokenException.withDefaultMsgAndCause(null),
                AccountNotActiveException.withDefaultMsg()
        );
    }
}