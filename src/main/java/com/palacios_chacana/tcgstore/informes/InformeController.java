package com.palacios_chacana.tcgstore.informes;

import com.palacios_chacana.tcgstore.informes.dto.GenerarInformeRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/informes")
public class InformeController {

    private final InformeService informeService;

    public InformeController(InformeService informeService) {
        this.informeService = informeService;
    }

    // Botón "Generar Informe" del front -> POST /api/informes/generar (cuerpo opcional)
    @PostMapping("/generar")
    public ResponseEntity<Map<String, String>> generar(
            @RequestBody(required = false) GenerarInformeRequestDto request,
            @AuthenticationPrincipal Jwt jwt) {
        String requestId = informeService.solicitarInforme(request, jwt);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of("informeId", requestId, "status", "SOLICITADO"));
    }
}
