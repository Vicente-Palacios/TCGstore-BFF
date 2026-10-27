package com.palacios_chacana.tcgstore.informes.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * Evento que viaja por el exchange "informe.exchange".
 * Lo consumen tanto api-informe (genera/envía el informe)
 * como api-db-informe (persiste la solicitud en Supabase).
 */
public record InformeSolicitadoEvent(
        String informeId,
        String email,
        Instant fechaSolicitud
) implements Serializable {

    public static InformeSolicitadoEvent nuevo(String email) {
        return new InformeSolicitadoEvent(UUID.randomUUID().toString(), email, Instant.now());
    }
}
