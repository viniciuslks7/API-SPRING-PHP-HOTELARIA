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
public class ImagemQuartoRequestDTO {

    @NotBlank(message = "A URL da imagem é obrigatória")
    @Size(max = 500, message = "A URL deve ter no máximo 500 caracteres")
    private String url;

    @NotNull(message = "O código do quarto é obrigatório")
    private Long codQuartoFk;
}
