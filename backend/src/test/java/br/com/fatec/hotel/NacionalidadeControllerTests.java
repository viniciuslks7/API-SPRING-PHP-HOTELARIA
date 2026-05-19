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
class NacionalidadeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateAndReturn201() throws Exception {
        mockMvc.perform(post("/nacionalidades")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "nomePais": "Brasil" }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codNacionalidade").exists())
                .andExpect(jsonPath("$.nomePais").value("Brasil"));
    }

    @Test
    void shouldReturn422WhenBlank() throws Exception {
        mockMvc.perform(post("/nacionalidades")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "nomePais": "" }
                        """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldUpdateAndDelete() throws Exception {
        String resp = mockMvc.perform(post("/nacionalidades")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "nomePais": "Argentina" }
                        """))
                .andReturn().getResponse().getContentAsString();
        Long id = Long.valueOf(resp.replaceAll(".*\"codNacionalidade\"\\s*:\\s*(\\d+).*", "$1"));

        mockMvc.perform(put("/nacionalidades/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "nomePais": "Argentina (atualizado)" }
                        """))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/nacionalidades/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldListAll() throws Exception {
        mockMvc.perform(get("/nacionalidades"))
                .andExpect(status().isOk());
    }
}
