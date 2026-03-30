package br.com.fatec.hotel.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    private String nome;

    @NotBlank(message = "O CNPJ é obrigatório")
    @Size(max = 18, message = "O CNPJ deve ter no máximo 18 caracteres")
    private String cnpj;

    @Min(value = 1, message = "A classificação deve ser no mínimo 1 estrela")
    @Max(value = 5, message = "A classificação deve ser no máximo 5 estrelas")
    private Integer classificacaoEstrelas;

    @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres")
    private String endereco;

    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
    private String telefone;
}
