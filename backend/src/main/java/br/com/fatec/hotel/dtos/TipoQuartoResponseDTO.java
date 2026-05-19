package br.com.fatec.hotel.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoQuartoResponseDTO {
    private Long codTipo;
    private String nome;
    private BigDecimal precoBase;
}
