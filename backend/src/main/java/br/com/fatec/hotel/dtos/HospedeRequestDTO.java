package br.com.fatec.hotel.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospedeRequestDTO {

    @NotBlank(message = "O nome do hóspede é obrigatório")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    private String nome;

    @NotBlank(message = "O documento é obrigatório")
    @Size(max = 30, message = "O documento deve ter no máximo 30 caracteres")
    private String documento;

    @NotNull(message = "O código da nacionalidade é obrigatório")
    private Long codNacionalidadeFk;
}
