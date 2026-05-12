package br.com.fatec.hotel.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicoExtraResponseDTO {
    
    private Long codServicos;
    private String nome;
    private Double preco;

}
