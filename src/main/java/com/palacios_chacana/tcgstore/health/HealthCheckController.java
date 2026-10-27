package com.palacios_chacana.tcgstore.health;

import com.palacios_chacana.tcgstore.health.dto.HealthResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;

@RestController
@RequestMapping("/api")
public class HealthCheckController {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckController.class);

    private final DataSource dataSource;

    // En Spring Boot moderno, si tienes un solo constructor, el @Autowired es implícito
    public HealthCheckController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponseDto> getHealth() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                return ResponseEntity.ok(new HealthResponseDto("UP"));
            } else {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(new HealthResponseDto("DOWN", "Database connection is invalid"));
            }
        } catch (Exception e) {
            // El detalle del error va al log; al cliente no se le exponen datos internos de la base.
            log.error("Health check: falló la conexión a la base de datos", e);
            HealthResponseDto errorResponse = new HealthResponseDto("DOWN", "Database unavailable");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        }
    }
}
