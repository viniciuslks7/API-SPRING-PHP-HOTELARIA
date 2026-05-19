package br.com.fatec.hotel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hotelOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Hotelaria — FATEC Jales")
                        .description(
                                "API REST para gestão hoteleira. Cobre cadastros base, operações (quartos, reservas, check-ins) e relações N:N (hóspedes-reservas e reservas-serviços).")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Vinicius & Yuri")
                                .email("vinicius.oliveiratwt@gmail.com"))
                        .license(new License().name("Acadêmico — FATEC Jales 2026")));
    }
}
