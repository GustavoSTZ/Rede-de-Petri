package br.edu.unisinos.rededepetri.controller;

import br.edu.unisinos.rededepetri.domain.RedeDePetri;
import br.edu.unisinos.rededepetri.service.EngineRedeDePetriService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.edu.unisinos.rededepetri.repository.RedeDePetriRepository.redeDePetri;

@RestController
@RequestMapping("/engine")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EngineRedeDePetriController {

    private final EngineRedeDePetriService engineRedeDePetriService;

    @PostMapping("/executar/tudo")
    @ResponseStatus(HttpStatus.OK)
    public void executarEngine() {
        engineRedeDePetriService.executarEngine();
    }

    @GetMapping("/executar/passo")
    public ResponseEntity<RedeDePetri> executarEnginePassoAPasso() {
        if (engineRedeDePetriService.executarEnginePassoAPasso()) {
            return new ResponseEntity<>(redeDePetri, HttpStatus.OK);
        }
        return new ResponseEntity<>(redeDePetri, HttpStatus.ALREADY_REPORTED);
    }
}
