package br.edu.unisinos.rededepetri.service;

import br.edu.unisinos.rededepetri.controller.request.RedeDePetriRequest;
import br.edu.unisinos.rededepetri.domain.Conexao;
import br.edu.unisinos.rededepetri.domain.Lugar;
import br.edu.unisinos.rededepetri.domain.RedeDePetri;
import br.edu.unisinos.rededepetri.exception.ResourceNotFoundException;
import br.edu.unisinos.rededepetri.repository.RedeDePetriRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedeDePetriService {

    private final ModelMapper modelMapper;

    public void criaRedeDePetri(RedeDePetriRequest redeDePetri) {
        RedeDePetriRepository.redeDePetri = modelMapper.map(redeDePetri, RedeDePetri.class);
        RedeDePetriRepository.mapeamentoLugares = redeDePetri.getLugarList().stream().collect(Collectors.toMap(Lugar::getNome, lugar -> lugar));

        RedeDePetriRepository.redeDePetri
                .getTransicaoList()
                .forEach(transicaoRequest -> {
                    populaToken(transicaoRequest.getConexaoDeEntradaList());
                    populaToken(transicaoRequest.getConexaoDeSaidaList());
                });
    }

    private void populaToken(List<Conexao> conexaoList) {
        conexaoList.forEach(conexaoRequest -> conexaoRequest
                .getLugar()
                .setQuantidadeDeToken(
                        RedeDePetriRepository.mapeamentoLugares
                                .get(conexaoRequest
                                        .getLugar()
                                        .getNome()
                                ).getQuantidadeDeToken()
                )
        );
    }

    public void deletarLugar(String nomeLugar) {
        Lugar lugarDeletado = RedeDePetriRepository.mapeamentoLugares.get(nomeLugar);
        if (isNull(lugarDeletado)) {
            throw new ResourceNotFoundException();
        }
        RedeDePetriRepository.redeDePetri.getLugarList().remove(lugarDeletado);
        RedeDePetriRepository.redeDePetri.getTransicaoList().forEach(
                transicao -> {
                    transicao.setConexaoDeEntradaList(removeConexoesQuePossuemLugarDeletado(transicao.getConexaoDeEntradaList(), lugarDeletado));
                    transicao.setConexaoDeSaidaList(removeConexoesQuePossuemLugarDeletado(transicao.getConexaoDeSaidaList(), lugarDeletado));
                }
        );
    }

    private List<Conexao> removeConexoesQuePossuemLugarDeletado(List<Conexao> conexoes, Lugar lugarDeletado) {
        return conexoes.stream()
                        .filter(conexao -> !conexao.getLugar().equals(lugarDeletado))
                .collect(Collectors.toList());
    }


}
