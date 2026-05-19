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
class ServicoExtraControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateAndReturn201() throws Exception {
        mockMvc.perform(post("/servicos-extras")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "nome": "Spa", "preco": 120.00 }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codServicos").exists())
                .andExpect(jsonPath("$.preco").value(120.00));
    }

    @Test
    void shouldReturn422WhenPrecoNotPositive() throws Exception {
        mockMvc.perform(post("/servicos-extras")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "nome": "Frigobar", "preco": -5 }
                        """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors.preco").exists());
    }

    @Test
    void shouldUpdateAndDelete() throws Exception {
        String resp = mockMvc.perform(post("/servicos-extras")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "nome": "Massagem", "preco": 200.00 }
                        """))
                .andReturn().getResponse().getContentAsString();
        Long id = Long.valueOf(resp.replaceAll(".*\"codServicos\"\\s*:\\s*(\\d+).*", "$1"));

        mockMvc.perform(put("/servicos-extras/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "nome": "Massagem Premium", "preco": 280.00 }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preco").value(280.00));

        mockMvc.perform(delete("/servicos-extras/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldListAll() throws Exception {
        mockMvc.perform(get("/servicos-extras"))
                .andExpect(status().isOk());
    }
}
