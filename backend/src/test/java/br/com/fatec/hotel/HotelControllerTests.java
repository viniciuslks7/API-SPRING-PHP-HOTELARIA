package br.com.fatec.hotel;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class HotelControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private static final String VALID_HOTEL = """
            {
              "nome": "Hotel Paraíso",
              "cnpj": "12.345.678/0001-90",
              "estrelas": 4
            }
            """;

    @Test
    void shouldCreateHotelAndReturn201() throws Exception {
        mockMvc.perform(post("/hoteis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_HOTEL))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codHotel").exists())
                .andExpect(jsonPath("$.nome").value("Hotel Paraíso"));
    }

    @Test
    void shouldReturn422WhenPayloadIsInvalid() throws Exception {
        String invalidHotel = """
                {
                  "nome": "",
                  "cnpj": "",
                  "estrelas": 10
                }
                """;

        mockMvc.perform(post("/hoteis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidHotel))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors.nome").exists())
                .andExpect(jsonPath("$.errors.estrelas").exists());
    }

    @Test
    void shouldReturn404WhenHotelDoesNotExist() throws Exception {
        mockMvc.perform(get("/hoteis/{id}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldDeleteHotelAndReturn204() throws Exception {
        String response = mockMvc.perform(post("/hoteis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_HOTEL))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = Long.valueOf(response.replaceAll(".*\"codHotel\"\\s*:\\s*(\\d+).*", "$1"));

        mockMvc.perform(delete("/hoteis/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/hoteis/{id}", id))
                .andExpect(status().isNotFound());
    }
}
