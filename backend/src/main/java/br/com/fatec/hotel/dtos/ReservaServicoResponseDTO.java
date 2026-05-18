package br.com.fatec.hotel.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaServicoResponseDTO {

    private Long codReservaServico;
    private Long codReservaFk;
    private Long codServicoFk;
    private Integer quantidade;
}
