package br.com.fatec.hotel;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
class TipoQuartoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateAndReturn201() throws Exception {
        mockMvc.perform(post("/tipos-quarto")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "nome": "Luxo", "precoBase": 500.00 }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codTipo").exists())
                .andExpect(jsonPath("$.nome").value("Luxo"));
    }

    @Test
    void shouldReturn422WhenInvalid() throws Exception {
        mockMvc.perform(post("/tipos-quarto")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "nome": "", "precoBase": -10 }
                        """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors.nome").exists())
                .andExpect(jsonPath("$.errors.precoBase").exists());
    }

    @Test
    void shouldUpdateAndReturn200() throws Exception {
        String resp = mockMvc.perform(post("/tipos-quarto")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "nome": "Standard", "precoBase": 200.00 }
                        """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = Long.valueOf(resp.replaceAll(".*\"codTipo\"\\s*:\\s*(\\d+).*", "$1"));

        mockMvc.perform(put("/tipos-quarto/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "nome": "Standard Plus", "precoBase": 250.00 }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Standard Plus"));
    }

    @Test
    void shouldListAll() throws Exception {
        mockMvc.perform(get("/tipos-quarto"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteAndReturn204() throws Exception {
        String resp = mockMvc.perform(post("/tipos-quarto")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "nome": "Suite", "precoBase": 800.00 }
                        """))
                .andReturn().getResponse().getContentAsString();
        Long id = Long.valueOf(resp.replaceAll(".*\"codTipo\"\\s*:\\s*(\\d+).*", "$1"));

        mockMvc.perform(delete("/tipos-quarto/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/tipos-quarto/99999"))
                .andExpect(status().isNotFound());
    }
}
