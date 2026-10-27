package com.palacios_chacana.tcgstore.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

/**
 * Verifica que el token fue emitido para ESTA aplicación (el App client de Cognito).
 * - El id_token trae el client id en el claim "aud".
 * - El access_token lo trae en el claim "client_id".
 * Se acepta cualquiera de los dos.
 */
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final String expectedClientId;

    public AudienceValidator(String expectedClientId) {
        this.expectedClientId = expectedClientId;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        List<String> audience = jwt.getAudience();
        boolean audOk = audience != null && audience.contains(expectedClientId);
        boolean clientIdOk = expectedClientId.equals(jwt.getClaimAsString("client_id"));

        if (audOk || clientIdOk) {
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(
                new OAuth2Error("invalid_token", "El token no fue emitido para esta aplicación (audience)", null));
    }
}
