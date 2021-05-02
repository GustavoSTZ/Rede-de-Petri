package br.edu.unisinos.rededepetri.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conexao {

    @NotNull(message = "Conexão deve conter lugar para apontar ou ser apontado para algum lugar/transicao")
    private Lugar lugar;

    @Min(value = 1, message = "O mínimo que uma Conexão pode ter de peso é 1")
    @Value(value = "1") // Para caso não seja informado
    private Integer peso;
}
