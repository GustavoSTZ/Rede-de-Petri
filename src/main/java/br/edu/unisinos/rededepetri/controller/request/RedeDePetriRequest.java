package br.edu.unisinos.rededepetri.controller.request;

import br.edu.unisinos.rededepetri.domain.Lugar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedeDePetriRequest {

    @Valid
    private List<Lugar> lugarList;

    @Valid
    private List<TransicaoRequest> transicaoList;
}
