package br.com.fatec.hotel.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospedeReservaResponseDTO {
    private Long codHospedeReserva;
    private Long codHospedeFk;
    private Long codReservaFk;
}
