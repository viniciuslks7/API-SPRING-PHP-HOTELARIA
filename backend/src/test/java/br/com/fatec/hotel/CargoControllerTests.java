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
class CargoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateAndReturn201() throws Exception {
        mockMvc.perform(post("/cargos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "nomeCargo": "Recepcionista", "salarioBase": 2500.00 }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codCargo").exists());
    }

    @Test
    void shouldReturn422WhenSalarioInvalid() throws Exception {
        mockMvc.perform(post("/cargos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "nomeCargo": "Gerente", "salarioBase": -100 }
                        """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors.salarioBase").exists());
    }

    @Test
    void shouldUpdateAndDelete() throws Exception {
        String resp = mockMvc.perform(post("/cargos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "nomeCargo": "Camareira", "salarioBase": 1800.00 }
                        """))
                .andReturn().getResponse().getContentAsString();
        Long id = Long.valueOf(resp.replaceAll(".*\"codCargo\"\\s*:\\s*(\\d+).*", "$1"));

        mockMvc.perform(put("/cargos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "nomeCargo": "Camareira Sênior", "salarioBase": 2200.00 }
                        """))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/cargos/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldListAll() throws Exception {
        mockMvc.perform(get("/cargos"))
                .andExpect(status().isOk());
    }
}
