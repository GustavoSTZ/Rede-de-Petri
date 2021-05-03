package br.edu.unisinos.rededepetri.controller;

import br.edu.unisinos.rededepetri.controller.request.CriaConexaoRequest;
import br.edu.unisinos.rededepetri.controller.request.RedeDePetriRequest;
import br.edu.unisinos.rededepetri.controller.request.TransicaoRequest;
import br.edu.unisinos.rededepetri.domain.*;
import br.edu.unisinos.rededepetri.repository.RedeDePetriRepository;
import br.edu.unisinos.rededepetri.service.RedeDePetriService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public void criaConexao(@RequestBody @Valid CriaConexaoRequest conexaoRequest) {
        redeDePetriService.criaConexa(conexaoRequest);
    }

    @PostMapping("/criar/transicao")
    @ResponseStatus(HttpStatus.CREATED)
    public void criaTransicao(@RequestBody @Valid TransicaoRequest transicaoRequest) {
        redeDePetriService.criaTransicao(transicaoRequest);
    }

    @PostMapping("/criar/lugar")
    @ResponseStatus(HttpStatus.CREATED)
    public void criaLugar(@RequestBody @Valid Lugar lugarRequest) {
        redeDePetriService.criaLugar(lugarRequest);
    }

    @DeleteMapping("/deletar/lugar/{nomeLugar}")
    @ResponseStatus(HttpStatus.OK)
    public void deletarLugar(@PathVariable("nomeLugar") String nomeLugar) {
        redeDePetriService.deletarLugar(nomeLugar);
    }

    @DeleteMapping("/deletar/transicao/{nomeTransicao}")
    @ResponseStatus(HttpStatus.OK)
    public void deletarTransicao(@PathVariable("nomeTransicao") String nomeTransicao) {
        redeDePetriService.deletarTransicao(nomeTransicao);
    }

    @DeleteMapping("/deletar/transicao/{nomeConexao}")
    @ResponseStatus(HttpStatus.OK)
    public void deletarConexao(@PathVariable("nomeConexao") String nomeConexao) {
        redeDePetriService.deletarConexao(nomeConexao);
    }

    @GetMapping("/consultar")
    @ResponseStatus(HttpStatus.OK)
    public RedeDePetri getRedeDePetri() {
        return redeDePetriService.getRedeDePetri();
    }

    @GetMapping("/tabelaTest")
    @ResponseStatus(HttpStatus.OK)
    public String tabelaTest() {
        RedeDePetriRepository.ciclos = List.of(new Ciclo(List.of(new TransicaoCiclo("Ta", true)), List.of(new LugarCiclo("L1", 12))));
        return redeDePetriService.imprimeTabela();
    }

    @PostMapping("/adiciona/token/lugar/{nomeLugar}")
    @ResponseStatus(HttpStatus.OK)
    public void adicionaToken(@PathVariable("nomeLugar") String nomeLugar) {
        redeDePetriService.adicionaToken(nomeLugar);
    }

    @PostMapping("/remove/token/lugar/{nomeLugar}")
    @ResponseStatus(HttpStatus.OK)
    public void removeToken(@PathVariable("nomeLugar") String nomeLugar) {
        redeDePetriService.removeToken(nomeLugar);
    }
}
