package br.edu.unisinos.rededepetri.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class Conexao {

    @NotNull(message = "Para identificarmos a conexao caso queiramos deleta-la")
    private String nomeConexao;

    @NotNull(message = "Conexão deve conter lugar para apontar ou ser apontado para algum lugar/transicao")
    private Lugar lugar;

    @NotNull
    @Min(value = 1, message = "O mínimo que uma Conexão pode ter de peso é 1")
    private Integer peso;

    private TipoArco tipoArco = TipoArco.NORMAL;
}
