package com.palacios_chacana.tcgstore.module.informes;

import com.palacios_chacana.tcgstore.module.informes.client.InformeClient;
import com.palacios_chacana.tcgstore.module.informes.dto.GenerarInformeRequestDto;
import com.palacios_chacana.tcgstore.module.informes.dto.InformeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class InformeService {

    private static final Logger log = LoggerFactory.getLogger(InformeService.class);
    private static final String DEFAULT_REPORT_TYPE = "top-products";

    private final InformeClient informeClient;
    private final ObjectMapper objectMapper;

    public InformeService(InformeClient informeClient, ObjectMapper objectMapper) {
        this.informeClient = informeClient;
        this.objectMapper = objectMapper;
    }

    /** Arma el mensaje "report.requested", lo publica vía InformeClient y devuelve el requestId. */
    public String solicitarInforme(GenerarInformeRequestDto request, Jwt jwt) {
        // El solicitante sale del token, no del cuerpo: nadie puede pedir un informe "a nombre de" otro.
        String email = jwt.getClaimAsString("email");
        String requester = email != null ? email : jwt.getSubject();
        String reportType = request != null && request.reportType() != null && !request.reportType().isBlank()
                ? request.reportType() : DEFAULT_REPORT_TYPE;

        InformeMessage message = InformeMessage.solicitado(
                requester, jwt.getSubject(), reportType, request != null ? request.params() : null);

        String messageId = informeClient.publicar(objectMapper.writeValueAsString(message));

        log.info("Publicado {} requestId={} snsMessageId={}",
                message.type(), message.body().requestId(), messageId);
        return message.body().requestId();
    }
}
