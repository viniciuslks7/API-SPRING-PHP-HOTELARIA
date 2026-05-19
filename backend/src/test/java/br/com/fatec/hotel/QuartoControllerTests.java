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
class QuartoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private TestDataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TestDataFactory(mockMvc);
    }

    @Test
    void shouldCreateQuartoWithFkResolved() throws Exception {
        Long hotelId = factory.createHotel("Quarto");
        Long tipoId = factory.createTipoQuarto("Standard");

        String body = """
                { "numero": 101, "andar": 1, "codHotelFk": %d, "codTipoFk": %d }
                """.formatted(hotelId, tipoId);

        mockMvc.perform(post("/quartos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codQuarto").exists())
                .andExpect(jsonPath("$.codHotelFk").value(hotelId))
                .andExpect(jsonPath("$.codTipoFk").value(tipoId));
    }

    @Test
    void shouldReturn404WhenHotelFkDoesNotExist() throws Exception {
        Long tipoId = factory.createTipoQuarto("Luxo");

        String body = """
                { "numero": 200, "andar": 2, "codHotelFk": 99999, "codTipoFk": %d }
                """.formatted(tipoId);

        mockMvc.perform(post("/quartos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn422WhenFieldsMissing() throws Exception {
        mockMvc.perform(post("/quartos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldListAll() throws Exception {
        mockMvc.perform(get("/quartos"))
                .andExpect(status().isOk());
    }
}
