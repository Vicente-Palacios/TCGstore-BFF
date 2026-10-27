package com.palacios_chacana.tcgstore.informes.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Exchange tipo fanout: el BFF publica UN evento "generar informe" y
 * ambas colas (queue.informe y queue.db-informe) reciben una copia,
 * cada una consumida por un microservicio distinto (api-informe / api-db-informe).
 */
@Configuration
public class RabbitMQConfig {

    @Value("${app.informes.exchange}")
    private String exchangeName;

    @Value("${app.informes.queue-informe}")
    private String queueInforme;

    @Value("${app.informes.queue-db-informe}")
    private String queueDbInforme;

    @Bean
    public FanoutExchange informeExchange() {
        return new FanoutExchange(exchangeName, true, false);
    }

    @Bean
    public Queue queueInforme() {
        return new Queue(queueInforme, true);
    }

    @Bean
    public Queue queueDbInforme() {
        return new Queue(queueDbInforme, true);
    }

    @Bean
    public Binding bindingInforme(Queue queueInforme, FanoutExchange informeExchange) {
        return BindingBuilder.bind(queueInforme).to(informeExchange);
    }

    @Bean
    public Binding bindingDbInforme(Queue queueDbInforme, FanoutExchange informeExchange) {
        return BindingBuilder.bind(queueDbInforme).to(informeExchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
