package br.com.fatec.hotel.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckinRequestDTO {

    @NotNull(message = "A data/hora real do check-in é obrigatória")
    private LocalDateTime dataHoraReal;

    @NotNull(message = "O código da reserva é obrigatório")
    private Long codReservaFk;

    @NotNull(message = "O código do funcionário é obrigatório")
    private Long codFuncionarioFk;
}
