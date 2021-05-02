package br.edu.unisinos.rededepetri.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransicaoRequest {

    @NotNull(message = "Nome não pode ser null para poder ser formada tabela futuramente")
    private String nome;

    @Valid
    private List<ConexaoRequest> conexaoDeEntradaList;

    @Valid
    private List<ConexaoRequest> conexaoDeSaidaList;
}
