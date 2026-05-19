package br.com.fatec.hotel;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
class ReservaServicoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private TestDataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TestDataFactory(mockMvc);
    }

    @Test
    void shouldCreateAssociationWithQuantidade() throws Exception {
        Long canalId = factory.createCanalReserva("Site");
        Long reservaId = factory.createReserva(canalId);
        Long servicoId = factory.createServicoExtra("Frigobar");

        String body = """
                { "codReservaFk": %d, "codServicoFk": %d, "quantidade": 3 }
                """.formatted(reservaId, servicoId);

        mockMvc.perform(post("/reservas-servicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codReservaServico").exists())
                .andExpect(jsonPath("$.quantidade").value(3));
    }

    @Test
    void shouldReturn422WhenQuantidadeIsZero() throws Exception {
        Long canalId = factory.createCanalReserva("Balcao");
        Long reservaId = factory.createReserva(canalId);
        Long servicoId = factory.createServicoExtra("Spa");

        String body = """
                { "codReservaFk": %d, "codServicoFk": %d, "quantidade": 0 }
                """.formatted(reservaId, servicoId);

        mockMvc.perform(post("/reservas-servicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors.quantidade").exists());
    }

    @Test
    void shouldReturn404WhenServicoDoesNotExist() throws Exception {
        Long canalId = factory.createCanalReserva("Site");
        Long reservaId = factory.createReserva(canalId);

        String body = """
                { "codReservaFk": %d, "codServicoFk": 99999, "quantidade": 1 }
                """.formatted(reservaId);

        mockMvc.perform(post("/reservas-servicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListAll() throws Exception {
        mockMvc.perform(get("/reservas-servicos"))
                .andExpect(status().isOk());
    }
}
