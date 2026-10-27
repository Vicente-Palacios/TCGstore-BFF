package com.palacios_chacana.tcgstore.informes.messaging;

import com.palacios_chacana.tcgstore.informes.dto.InformeSolicitadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InformeClient {

    private static final Logger log = LoggerFactory.getLogger(InformeClient.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;

    public InformeClient(RabbitTemplate rabbitTemplate,
                          @Value("${app.informes.exchange}") String exchangeName) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
    }

    public void publicarSolicitud(InformeSolicitadoEvent evento) {
        log.info("Publicando InformeSolicitadoEvent id={} email={} en exchange={}",
                evento.informeId(), evento.email(), exchangeName);
        // Exchange fanout: la routing key se ignora, llega a ambas colas igual.
        rabbitTemplate.convertAndSend(exchangeName, "", evento);
    }
}
