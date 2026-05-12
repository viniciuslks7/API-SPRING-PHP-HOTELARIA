package br.com.fatec.hotel.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckinResponseDTO {
    private Long codCheckin;
    private LocalDateTime dataHoraReal;
    private Long codReservaFk;
    private Long codFuncionarioFk;
}
