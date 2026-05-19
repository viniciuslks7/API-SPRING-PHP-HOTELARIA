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
class HospedeReservaControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private TestDataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TestDataFactory(mockMvc);
    }

    @Test
    void shouldCreateAssociation() throws Exception {
        Long nacId = factory.createNacionalidade("Brasil");
        Long hospedeId = factory.createHospede(nacId, "DOC001");
        Long canalId = factory.createCanalReserva("Booking");
        Long reservaId = factory.createReserva(canalId);

        String body = """
                { "codHospedeFk": %d, "codReservaFk": %d }
                """.formatted(hospedeId, reservaId);

        mockMvc.perform(post("/hospedes-reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codHospedeReserva").exists());
    }

    @Test
    void shouldReturn404WhenHospedeDoesNotExist() throws Exception {
        Long canalId = factory.createCanalReserva("Site");
        Long reservaId = factory.createReserva(canalId);

        String body = """
                { "codHospedeFk": 99999, "codReservaFk": %d }
                """.formatted(reservaId);

        mockMvc.perform(post("/hospedes-reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListAll() throws Exception {
        mockMvc.perform(get("/hospedes-reservas"))
                .andExpect(status().isOk());
    }
}
