package com.palacios_chacana.tcgstore.module.informes.dto;

import jakarta.validation.constraints.Size;

import java.util.Map;

/**
 * Cuerpo opcional de POST /api/informes/generar. El solicitante (requester) NO viene aquí:
 * lo decide el servidor a partir del JWT de Cognito.
 */
public record GenerarInformeRequestDto(
        @Size(max = 50) String reportType,
        Map<String, Object> params
) {
}
