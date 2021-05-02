package br.edu.unisinos.rededepetri.controller.request;

import br.edu.unisinos.rededepetri.domain.TipoArco;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConexaoRequest {

    @NotNull(message = "Para identificarmos a conexao caso queiramos deleta-la")
    private String nomeConexao;

    @NotNull(message = "Conexão deve conter lugar para apontar ou ser apontado para algum lugar/transicao")
    private String nomeLugar;

    @NotNull
    @Min(value = 1, message = "O mínimo que uma Conexão pode ter de peso é 1")
    private Integer peso;

    private TipoArco tipoArco = TipoArco.NORMAL;
}
