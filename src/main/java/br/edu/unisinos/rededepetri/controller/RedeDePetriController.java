package br.edu.unisinos.rededepetri.controller;

import br.edu.unisinos.rededepetri.controller.request.CriaConexaoRequest;
import br.edu.unisinos.rededepetri.controller.request.RedeDePetriRequest;
import br.edu.unisinos.rededepetri.controller.request.TransicaoRequest;
import br.edu.unisinos.rededepetri.domain.Lugar;
import br.edu.unisinos.rededepetri.domain.RedeDePetri;
import br.edu.unisinos.rededepetri.service.RedeDePetriService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
        redeDePetriService.criaConexao(conexaoRequest);
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

    @DeleteMapping("/deletar/conexao/{nomeConexao}")
    @ResponseStatus(HttpStatus.OK)
    public void deletarConexao(@PathVariable("nomeConexao") String nomeConexao) {
        redeDePetriService.deletarConexao(nomeConexao);
    }

    @GetMapping("/consultar")
    @ResponseStatus(HttpStatus.OK)
    public RedeDePetri getRedeDePetri() {
        return redeDePetriService.getRedeDePetri();
    }

    @PatchMapping("/adiciona/token/lugar/{nomeLugar}")
    @ResponseStatus(HttpStatus.OK)
    public void adicionaToken(@PathVariable("nomeLugar") String nomeLugar) {
        redeDePetriService.adicionaToken(nomeLugar);
    }

    @PatchMapping("/remove/token/lugar/{nomeLugar}")
    @ResponseStatus(HttpStatus.OK)
    public void removeToken(@PathVariable("nomeLugar") String nomeLugar) {
        redeDePetriService.removeToken(nomeLugar);
    }

    @GetMapping("/consultar/token/lugar/{nomeLugar}")
    @ResponseStatus(HttpStatus.OK)
    public Integer getToken(@PathVariable("nomeLugar") String nomeLugar) {
        return redeDePetriService.getToken(nomeLugar);
    }
}
