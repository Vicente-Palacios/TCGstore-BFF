package com.palacios_chacana.tcgstore.informes;

import com.palacios_chacana.tcgstore.informes.dto.GenerarInformeRequestDto;
import com.palacios_chacana.tcgstore.informes.dto.InformeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import tools.jackson.databind.ObjectMapper;

@Service
public class InformeService {

    private static final Logger log = LoggerFactory.getLogger(InformeService.class);
    private static final String DEFAULT_REPORT_TYPE = "top-products";

    private final SnsClient sns;
    private final ObjectMapper objectMapper;
    private final String topicArn;

    public InformeService(SnsClient sns, ObjectMapper objectMapper,
                          @Value("${app.sns.topic-arn:}") String topicArn) {
        this.sns = sns;
        this.objectMapper = objectMapper;
        this.topicArn = topicArn;
    }

    /** Publica "report.requested" en SNS y devuelve el requestId generado. */
    public String solicitarInforme(GenerarInformeRequestDto request, Jwt jwt) {
        if (topicArn.isBlank()) {
            throw new IllegalStateException("SNS_TOPIC_ARN no está configurado");
        }
        // El solicitante sale del token, no del cuerpo: nadie puede pedir un informe "a nombre de" otro.
        String email = jwt.getClaimAsString("email");
        String requester = email != null ? email : jwt.getSubject();
        String reportType = request != null && request.reportType() != null && !request.reportType().isBlank()
                ? request.reportType() : DEFAULT_REPORT_TYPE;

        InformeMessage message = InformeMessage.solicitado(
                requester, jwt.getSubject(), reportType, request != null ? request.params() : null);

        String messageId = sns.publish(PublishRequest.builder()
                .topicArn(topicArn)
                .message(objectMapper.writeValueAsString(message))
                .build()).messageId();

        log.info("Publicado {} requestId={} snsMessageId={}",
                message.type(), message.body().requestId(), messageId);
        return message.body().requestId();
    }
}
