package br.com.fatec.hotel.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospedeReservaRequestDTO {

    @NotNull(message = "O código do hóspede é obrigatório")
    private Long codHospedeFk;

    @NotNull(message = "O código da reserva é obrigatório")
    private Long codReservaFk;
}
