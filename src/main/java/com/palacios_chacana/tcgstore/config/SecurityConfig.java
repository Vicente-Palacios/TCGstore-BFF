package com.palacios_chacana.tcgstore.config;
 
import com.palacios_chacana.tcgstore.security.CognitoJwtAuthenticationConverter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
 
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Habilita @PreAuthorize en los controladores
public class SecurityConfig {
 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecretGatewayFilter secretGatewayFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                // API sin estado: cada petición trae su JWT, no se usan sesiones.
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Filtro del secreto del API Gateway, antes del filtro de autenticación
                .addFilterBefore(secretGatewayFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // Preflight CORS: el navegador no envía el JWT en OPTIONS, así que se permite sin token.
                        // El SecretGatewayFilter sigue exigiendo X-Secret-Gateway también en estas peticiones.
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(new CognitoJwtAuthenticationConverter()))
                );
 
        return http.build();
    }
 
    // Por ser @Component, Spring Boot registraría el filtro también como filtro de servlet
    // y se ejecutaría dos veces. Se desactiva ese registro: solo corre dentro de la cadena de seguridad.
    @Bean
    public FilterRegistrationBean<SecretGatewayFilter> secretGatewayFilterRegistration(SecretGatewayFilter filter) {
        FilterRegistrationBean<SecretGatewayFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }
}