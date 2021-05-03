package br.edu.unisinos.rededepetri.repository;

import br.edu.unisinos.rededepetri.domain.Ciclo;
import br.edu.unisinos.rededepetri.domain.Lugar;
import br.edu.unisinos.rededepetri.domain.RedeDePetri;

import java.util.List;
import java.util.Map;

public class RedeDePetriRepository {

    public static RedeDePetri redeDePetri;

    public static Map<String, Lugar> mapeamentoLugares;

    public static List<Ciclo> ciclos;
}
