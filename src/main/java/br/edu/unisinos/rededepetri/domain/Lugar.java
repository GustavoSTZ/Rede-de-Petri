package br.edu.unisinos.rededepetri.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(of = "nome")
@AllArgsConstructor
@NoArgsConstructor
public class Lugar {

    @NotNull(message = "Id e identificação do Lugar, não pode ser nullo")
    private String nome;

    @Value("0")
    @Min(value = 0, message = "Valor mínimo para tokens é 0")
    private Integer quantidadeDeToken;
}
