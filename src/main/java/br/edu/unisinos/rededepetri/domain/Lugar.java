package br.edu.unisinos.rededepetri.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Min;

@Data
@EqualsAndHashCode(of = "nome")
@AllArgsConstructor
@NoArgsConstructor
public class Lugar {

    private String nome;

    @Value("0")
    @Min(value = 0, message = "Valor mínimo para tokens é 0")
    private Integer quantidadeDeToken;
}
