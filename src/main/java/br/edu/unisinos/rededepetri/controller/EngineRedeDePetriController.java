package br.edu.unisinos.rededepetri.controller;

import br.edu.unisinos.rededepetri.controller.request.RedeDePetriRequest;
import br.edu.unisinos.rededepetri.service.EngineRedeDePetriService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/engine")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EngineRedeDePetriController {

    private final EngineRedeDePetriService engineRedeDePetriService;

    @PostMapping("/executar")
    @ResponseStatus(HttpStatus.OK)
    public void executarEngine() {
        engineRedeDePetriService.executarEngine();
    }
}
