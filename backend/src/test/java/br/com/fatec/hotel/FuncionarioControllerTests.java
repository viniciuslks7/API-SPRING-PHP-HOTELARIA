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
class FuncionarioControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private TestDataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TestDataFactory(mockMvc);
    }

    @Test
    void shouldCreateFuncionario() throws Exception {
        Long cargoId = factory.createCargo("Recepcionista");

        String body = """
                { "nome": "Yuri", "codCargoFk": %d }
                """.formatted(cargoId);

        mockMvc.perform(post("/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codFuncionario").exists())
                .andExpect(jsonPath("$.nome").value("Yuri"));
    }

    @Test
    void shouldReturn404WhenCargoFkDoesNotExist() throws Exception {
        String body = """
                { "nome": "Sem Cargo", "codCargoFk": 99999 }
                """;

        mockMvc.perform(post("/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListAll() throws Exception {
        mockMvc.perform(get("/funcionarios"))
                .andExpect(status().isOk());
    }
}
