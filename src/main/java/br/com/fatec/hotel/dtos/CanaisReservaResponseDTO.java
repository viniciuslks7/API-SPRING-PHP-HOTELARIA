package br.com.fatec.hotel.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CanaisReservaResponseDTO {
    private Long id;
    private String nome;
    private Double taxaComissao; 
}
