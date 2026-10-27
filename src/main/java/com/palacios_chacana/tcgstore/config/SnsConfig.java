package com.palacios_chacana.tcgstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class SnsConfig {

    // Sin región, claves ni endpoint en el código: todo sale del entorno (AWS_REGION, rol de la
    // instancia EC2 y, en local, AWS_ENDPOINT_URL + claves de prueba). Así el mismo código
    // sirve para LocalStack y para AWS real.
    @Bean
    public SnsClient snsClient() {
        return SnsClient.create();
    }
}
