package com.palacios_chacana.tcgstore.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AudienceValidatorTest {

    private static final String CLIENT_ID = "mi-client-id";
    private final AudienceValidator validator = new AudienceValidator(CLIENT_ID);

    private Jwt.Builder base() {
        return Jwt.withTokenValue("token").header("alg", "none").subject("user-1");
    }

    @Test
    void aceptaIdTokenConAudCorrecto() {
        Jwt jwt = base().audience(List.of(CLIENT_ID)).build();
        assertFalse(validator.validate(jwt).hasErrors());
    }

    @Test
    void aceptaAccessTokenConClientIdCorrecto() {
        Jwt jwt = base().claim("client_id", CLIENT_ID).build();
        assertFalse(validator.validate(jwt).hasErrors());
    }

    @Test
    void rechazaTokenDeOtraAplicacion() {
        Jwt jwt = base().audience(List.of("otro-client")).claim("client_id", "otro-client").build();
        assertTrue(validator.validate(jwt).hasErrors());
    }

    @Test
    void rechazaTokenSinAudience() {
        Jwt jwt = base().claim("email", "a@b.cl").build();
        assertTrue(validator.validate(jwt).hasErrors());
    }
}
