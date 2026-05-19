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
class CheckinControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private TestDataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TestDataFactory(mockMvc);
    }

    @Test
    void shouldCreateCheckin() throws Exception {
        Long canalId = factory.createCanalReserva("Site");
        Long reservaId = factory.createReserva(canalId);
        Long cargoId = factory.createCargo("Recepcionista");
        Long funcId = factory.createFuncionario(cargoId, "Funcionario1");

        String body = """
                {
                  "dataHoraReal": "2026-07-01T14:30:00",
                  "codReservaFk": %d,
                  "codFuncionarioFk": %d
                }
                """.formatted(reservaId, funcId);

        mockMvc.perform(post("/checkins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codCheckin").exists())
                .andExpect(jsonPath("$.codReservaFk").value(reservaId));
    }

    @Test
    void shouldReturn404WhenReservaDoesNotExist() throws Exception {
        Long cargoId = factory.createCargo("Gerente");
        Long funcId = factory.createFuncionario(cargoId, "Func");

        String body = """
                {
                  "dataHoraReal": "2026-07-01T14:30:00",
                  "codReservaFk": 99999,
                  "codFuncionarioFk": %d
                }
                """.formatted(funcId);

        mockMvc.perform(post("/checkins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListAll() throws Exception {
        mockMvc.perform(get("/checkins"))
                .andExpect(status().isOk());
    }
}
