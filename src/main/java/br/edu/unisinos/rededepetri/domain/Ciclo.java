package br.edu.unisinos.rededepetri.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ciclo {
    private List<TransicaoCiclo> transicoes;
    private List<LugarCiclo> lugares;
}
