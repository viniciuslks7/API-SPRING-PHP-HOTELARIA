package br.com.fatec.hotel.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponseDTO {
    private Long id;
    private String nome;
    private String cnpj;
    private Integer classificacaoEstrelas;
    private String endereco;
    private String telefone;
}
