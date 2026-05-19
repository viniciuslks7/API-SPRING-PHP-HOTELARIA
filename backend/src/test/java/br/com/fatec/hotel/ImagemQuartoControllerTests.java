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
class ImagemQuartoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private TestDataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TestDataFactory(mockMvc);
    }

    @Test
    void shouldCreateImagem() throws Exception {
        Long hotelId = factory.createHotel("ImgQuarto");
        Long tipoId = factory.createTipoQuarto("Standard");
        Long quartoId = factory.createQuarto(hotelId, tipoId, 301);

        String body = """
                { "url": "https://exemplo.com/foto.jpg", "codQuartoFk": %d }
                """.formatted(quartoId);

        mockMvc.perform(post("/imagens-quarto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codImagem").exists())
                .andExpect(jsonPath("$.codQuartoFk").value(quartoId));
    }

    @Test
    void shouldReturn404WhenQuartoFkDoesNotExist() throws Exception {
        String body = """
                { "url": "https://exemplo.com/foto.jpg", "codQuartoFk": 99999 }
                """;

        mockMvc.perform(post("/imagens-quarto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListAll() throws Exception {
        mockMvc.perform(get("/imagens-quarto"))
                .andExpect(status().isOk());
    }
}
