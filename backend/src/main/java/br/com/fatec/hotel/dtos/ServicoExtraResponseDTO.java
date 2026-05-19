package br.com.fatec.hotel.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicoExtraResponseDTO {

    private Long codServicos;
    private String nome;
    private BigDecimal preco;

}
