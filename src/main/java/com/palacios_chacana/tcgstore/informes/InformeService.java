package com.palacios_chacana.tcgstore.informes;

import com.palacios_chacana.tcgstore.informes.dto.GenerarInformeRequestDto;
import com.palacios_chacana.tcgstore.informes.dto.InformeSolicitadoEvent;
import com.palacios_chacana.tcgstore.informes.messaging.InformeClient;
import org.springframework.stereotype.Service;

@Service
public class InformeService {

    private final InformeClient informeClient;

    public InformeService(InformeClient informeClient) {
        this.informeClient = informeClient;
    }

    public String solicitarInforme(GenerarInformeRequestDto request) {
        InformeSolicitadoEvent evento = InformeSolicitadoEvent.nuevo(request.email());
        informeClient.publicarSolicitud(evento);
        return evento.informeId();
    }
}
