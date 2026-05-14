package br.com.fatec.hotel.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuartoResponseDTO {
    private Long codQuarto;
    private Integer numero;
    private Integer andar;
    private Long codHotelFk;
    private Long codTipoFk;
}
