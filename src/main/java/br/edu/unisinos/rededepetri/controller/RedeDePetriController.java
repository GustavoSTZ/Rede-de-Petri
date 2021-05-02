package br.edu.unisinos.rededepetri.controller;

import br.edu.unisinos.rededepetri.controller.request.ConexaoRequest;
import br.edu.unisinos.rededepetri.controller.request.RedeDePetriRequest;
import br.edu.unisinos.rededepetri.controller.request.TransicaoRequest;
import br.edu.unisinos.rededepetri.domain.Lugar;
import br.edu.unisinos.rededepetri.service.RedeDePetriService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping()
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedeDePetriController {

    private final RedeDePetriService redeDePetriService;

    @PostMapping("/criar/rede")
    @ResponseStatus(HttpStatus.CREATED)
    public void criaRedeDePetri(@RequestBody @Valid RedeDePetriRequest redeDePetri) {
        redeDePetriService.criaRedeDePetri(redeDePetri);
    }

    @PostMapping("/criar/conexao")
    @ResponseStatus(HttpStatus.CREATED)
    public void criaConexao(@RequestBody @Valid ConexaoRequest conexaoRequest) {
        //TODO redeDePetriService.criaConexa(conexaoRequest);
    }

    @PostMapping("/criar/transicao")
    @ResponseStatus(HttpStatus.CREATED)
    public void criaTransicao(@RequestBody @Valid TransicaoRequest transicaoRequest) {
        //TODO redeDePetriService.criaTransicao(transicaoRequest);
    }

    @PostMapping("/criar/lugar")
    @ResponseStatus(HttpStatus.CREATED)
    public void criaLugar(@RequestBody @Valid Lugar lugarRequest) {
        //TODO redeDePetriService.criaLugar(lugarRequest);
    }

    @DeleteMapping("/deletar/lugar/{nomeLugar}")
    @ResponseStatus(HttpStatus.OK)
    public void deletarLugar(@PathVariable("nomeLugar") String nomeLugar) {
        redeDePetriService.deletarLugar(nomeLugar);
    }

    @DeleteMapping("/deletar/transicao/{nomeTransicao}")
    @ResponseStatus(HttpStatus.OK)
    public void deletarTransicao(@PathVariable("nomeTransicao") String nomeTransicao) {
        //TODO redeDePetriService.deletarTransicao(nomeTransicao);
    }

    @DeleteMapping("/deletar/transicao/{nomeConexao}")
    @ResponseStatus(HttpStatus.OK)
    public void deletarConexao(@PathVariable("nomeConexao") String nomeConexao) {
        //TODO redeDePetriService.deletarConexao(nomeConexao);
    }

}
