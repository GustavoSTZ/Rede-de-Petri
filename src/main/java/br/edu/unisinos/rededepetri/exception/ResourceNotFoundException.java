package br.edu.unisinos.rededepetri.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Não encontrado lugar/transição na rede de petri.")
public class ResourceNotFoundException extends RuntimeException {
}