package br.edu.unisinos.rededepetri.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = "nome")
@AllArgsConstructor
@NoArgsConstructor
public class TransicaoCiclo {
    private String nome;
    private boolean habilitada;
}
