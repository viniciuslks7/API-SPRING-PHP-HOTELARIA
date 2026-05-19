package br.com.fatec.hotel;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ReservaValidationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn400WhenCheckoutBeforeCheckin() throws Exception {
        String canalJson = """
                { "nome": "Booking" }
                """;
        String canalResp = mockMvc.perform(post("/canais-reserva")
                .contentType(MediaType.APPLICATION_JSON)
                .content(canalJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long canalId = Long.valueOf(canalResp.replaceAll(".*\"codCanais\"\\s*:\\s*(\\d+).*", "$1"));

        String reservaInvalida = """
                {
                  "dataCheckin": "2026-06-10",
                  "dataCheckout": "2026-06-08",
                  "valorTotal": 500.00,
                  "codCanalFk": %d
                }
                """.formatted(canalId);

        mockMvc.perform(post("/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservaInvalida))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(
                        org.hamcrest.Matchers.containsString("posterior")));
    }

    @Test
    void shouldReturn404WhenCanalDoesNotExistOnReservaCreate() throws Exception {
        String reservaSemCanal = """
                {
                  "dataCheckin": "2026-06-10",
                  "dataCheckout": "2026-06-15",
                  "valorTotal": 1200.00,
                  "codCanalFk": 99999
                }
                """;

        mockMvc.perform(post("/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservaSemCanal))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
