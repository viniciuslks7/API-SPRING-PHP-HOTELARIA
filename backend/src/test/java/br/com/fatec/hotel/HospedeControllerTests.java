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
class HospedeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private TestDataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TestDataFactory(mockMvc);
    }

    @Test
    void shouldCreateHospede() throws Exception {
        Long nacId = factory.createNacionalidade("Brasil");

        String body = """
                { "nome": "Vinicius", "documento": "12345678900", "codNacionalidadeFk": %d }
                """.formatted(nacId);

        mockMvc.perform(post("/hospedes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codHospede").exists())
                .andExpect(jsonPath("$.nome").value("Vinicius"));
    }

    @Test
    void shouldReturn404WhenNacionalidadeDoesNotExist() throws Exception {
        String body = """
                { "nome": "Teste", "documento": "00000000000", "codNacionalidadeFk": 99999 }
                """;

        mockMvc.perform(post("/hospedes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn422WhenDocumentoIsBlank() throws Exception {
        Long nacId = factory.createNacionalidade("Brasil");

        String body = """
                { "nome": "Sem Documento", "documento": "", "codNacionalidadeFk": %d }
                """.formatted(nacId);

        mockMvc.perform(post("/hospedes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldListAll() throws Exception {
        mockMvc.perform(get("/hospedes"))
                .andExpect(status().isOk());
    }
}
