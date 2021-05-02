package br.edu.unisinos.rededepetri.controller;

import br.edu.unisinos.rededepetri.controller.request.RedeDePetriRequest;
import br.edu.unisinos.rededepetri.service.RedeDePetriService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/rede-de-petri")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedeDePetriController {

    private final RedeDePetriService redeDePetriService;

    @PostMapping("/criar")
    @ResponseStatus(HttpStatus.CREATED)
    public void criaRedeDePetri(@RequestBody @Valid RedeDePetriRequest redeDePetri) {
        redeDePetriService.criaRedeDePetri(redeDePetri);
    }

    @DeleteMapping("/deletar-lugar/{nomeLugar}")
    @ResponseStatus(HttpStatus.OK)
    public void deletarLugar(@PathVariable("nomeLugar") String nomeLugar) {
        redeDePetriService.deletarLugar(nomeLugar);
    }
}
