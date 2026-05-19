package br.com.fatec.hotel;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.com.fatec.hotel.support.TestDataFactory;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ReservaControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private TestDataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TestDataFactory(mockMvc);
    }

    @Test
    void shouldCreateReserva() throws Exception {
        Long canalId = factory.createCanalReserva("Site");

        String body = """
                {
                  "dataCheckin": "2026-08-01",
                  "dataCheckout": "2026-08-05",
                  "valorTotal": 1500.00,
                  "codCanalFk": %d
                }
                """.formatted(canalId);

        mockMvc.perform(post("/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codReserva").exists())
                .andExpect(jsonPath("$.valorTotal").value(1500.00));
    }

    @Test
    void shouldUpdateReserva() throws Exception {
        Long canalId = factory.createCanalReserva("Balcao");
        Long id = factory.createReserva(canalId);

        String body = """
                {
                  "dataCheckin": "2026-09-10",
                  "dataCheckout": "2026-09-15",
                  "valorTotal": 2000.00,
                  "codCanalFk": %d
                }
                """.formatted(canalId);

        mockMvc.perform(put("/reservas/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorTotal").value(2000.00));
    }

    @Test
    void shouldDeleteReserva() throws Exception {
        Long canalId = factory.createCanalReserva("Booking");
        Long id = factory.createReserva(canalId);

        mockMvc.perform(delete("/reservas/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldListAll() throws Exception {
        mockMvc.perform(get("/reservas"))
                .andExpect(status().isOk());
    }
}
