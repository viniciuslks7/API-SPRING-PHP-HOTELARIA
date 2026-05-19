package br.com.fatec.hotel.support;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

public class TestDataFactory {

    private final MockMvc mockMvc;

    public TestDataFactory(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private Long extractId(String body, String idField) {
        return Long.valueOf(body.replaceAll(".*\"" + idField + "\"\\s*:\\s*(\\d+).*", "$1"));
    }

    public Long createHotel(String suffix) throws Exception {
        String body = """
                { "nome": "Hotel %s", "cnpj": "%s", "estrelas": 4 }
                """.formatted(suffix, randomCnpj());
        String resp = mockMvc.perform(post("/hoteis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn().getResponse().getContentAsString();
        return extractId(resp, "codHotel");
    }

    public Long createTipoQuarto(String nome) throws Exception {
        String body = """
                { "nome": "%s", "precoBase": 250.00 }
                """.formatted(nome);
        String resp = mockMvc.perform(post("/tipos-quarto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn().getResponse().getContentAsString();
        return extractId(resp, "codTipo");
    }

    public Long createCanalReserva(String nome) throws Exception {
        String body = """
                { "nome": "%s" }
                """.formatted(nome);
        String resp = mockMvc.perform(post("/canais-reserva")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn().getResponse().getContentAsString();
        return extractId(resp, "codCanais");
    }

    public Long createServicoExtra(String nome) throws Exception {
        String body = """
                { "nome": "%s", "preco": 50.00 }
                """.formatted(nome);
        String resp = mockMvc.perform(post("/servicos-extras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn().getResponse().getContentAsString();
        return extractId(resp, "codServicos");
    }

    public Long createNacionalidade(String nome) throws Exception {
        String body = """
                { "nomePais": "%s" }
                """.formatted(nome);
        String resp = mockMvc.perform(post("/nacionalidades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn().getResponse().getContentAsString();
        return extractId(resp, "codNacionalidade");
    }

    public Long createCargo(String nome) throws Exception {
        String body = """
                { "nomeCargo": "%s", "salarioBase": 3500.00 }
                """.formatted(nome);
        String resp = mockMvc.perform(post("/cargos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn().getResponse().getContentAsString();
        return extractId(resp, "codCargo");
    }

    public Long createQuarto(Long hotelId, Long tipoId, int numero) throws Exception {
        String body = """
                { "numero": %d, "andar": 1, "codHotelFk": %d, "codTipoFk": %d }
                """.formatted(numero, hotelId, tipoId);
        String resp = mockMvc.perform(post("/quartos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn().getResponse().getContentAsString();
        return extractId(resp, "codQuarto");
    }

    public Long createHospede(Long nacionalidadeId, String documento) throws Exception {
        String body = """
                { "nome": "Hospede %s", "documento": "%s", "codNacionalidadeFk": %d }
                """.formatted(documento, documento, nacionalidadeId);
        String resp = mockMvc.perform(post("/hospedes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn().getResponse().getContentAsString();
        return extractId(resp, "codHospede");
    }

    public Long createFuncionario(Long cargoId, String nome) throws Exception {
        String body = """
                { "nome": "%s", "codCargoFk": %d }
                """.formatted(nome, cargoId);
        String resp = mockMvc.perform(post("/funcionarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn().getResponse().getContentAsString();
        return extractId(resp, "codFuncionario");
    }

    public Long createReserva(Long canalId) throws Exception {
        String body = """
                {
                  "dataCheckin": "2026-07-01",
                  "dataCheckout": "2026-07-05",
                  "valorTotal": 1200.00,
                  "codCanalFk": %d
                }
                """.formatted(canalId);
        String resp = mockMvc.perform(post("/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andReturn().getResponse().getContentAsString();
        return extractId(resp, "codReserva");
    }

    private String randomCnpj() {
        long n = System.nanoTime() % 100000000L;
        return String.format("%02d.%03d.%03d/0001-%02d",
                (n / 1000000) % 100,
                (n / 1000) % 1000,
                n % 1000,
                (int) (Math.random() * 100));
    }
}
