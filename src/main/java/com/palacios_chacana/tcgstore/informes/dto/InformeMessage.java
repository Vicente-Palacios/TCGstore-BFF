package com.palacios_chacana.tcgstore.informes.dto;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Mensaje que se publica en el topic SNS "report-events". Forma que esperan las Lambdas:
 * {"type": "report.requested", "body": {requestId, requester, requesterId, reportType, params, requestedAt}}
 * Los filtros de las suscripciones SNS miran el campo "type".
 */
public record InformeMessage(String type, Body body) {

    public static final String REQUESTED = "report.requested";

    public record Body(
            String requestId,
            String requester,
            String requesterId,
            String reportType,
            Map<String, Object> params,
            Instant requestedAt
    ) {
    }

    public static InformeMessage solicitado(String requester, String requesterId,
                                            String reportType, Map<String, Object> params) {
        return new InformeMessage(REQUESTED, new Body(
                UUID.randomUUID().toString(),
                requester,
                requesterId,
                reportType,
                params == null ? Map.of() : params,
                Instant.now()));
    }
}
