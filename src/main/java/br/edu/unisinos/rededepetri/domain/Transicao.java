package br.edu.unisinos.rededepetri.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transicao {

    private String nome;

    private List<Conexao> conexaoDeEntradaList;

    private List<Conexao> conexaoDeSaidaList;
}
