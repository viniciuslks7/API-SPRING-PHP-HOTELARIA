package br.com.fatec.hotel.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagemQuartoResponseDTO {
    private Long codImagem;
    private String url;
    private Long codQuartoFk;
}
