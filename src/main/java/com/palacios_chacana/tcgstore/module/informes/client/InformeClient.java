package com.palacios_chacana.tcgstore.module.informes.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

/**
 * Cliente de mensajería del módulo informes: es la única clase que habla con SNS.
 * InformeService le entrega el mensaje ya armado (JSON) y este lo publica en el topic.
 */
@Component
public class InformeClient {

    private static final Logger log = LoggerFactory.getLogger(InformeClient.class);

    private final SnsClient sns;
    private final String topicArn;

    public InformeClient(SnsClient sns, @Value("${app.sns.topic-arn:}") String topicArn) {
        this.sns = sns;
        this.topicArn = topicArn;
    }

    /** Publica el mensaje en el topic de SNS y devuelve el messageId que asigna SNS. */
    public String publicar(String mensajeJson) {
        if (topicArn.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "El servicio de informes no está configurado");
        }
        try {
            return sns.publish(PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(mensajeJson)
                    .build()).messageId();
        } catch (SdkException e) {
            // El detalle va al log; al cliente solo le llega un mensaje genérico.
            log.error("Error publicando en SNS", e);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "No se pudo encolar la solicitud de informe");
        }
    }
}
