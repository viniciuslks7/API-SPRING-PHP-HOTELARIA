package br.com.fatec.hotel.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaServicoRequestDTO {

    @NotNull(message = "O código da reserva é obrigatório!")
    private Long codReservaFk;

    @NotNull(message = "O código do serviço é obrigatório!")
    private Long codServicoFk;

    @NotNull(message = "A quantidade é obrigatória!")
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1.")
    private Integer quantidade;

}
