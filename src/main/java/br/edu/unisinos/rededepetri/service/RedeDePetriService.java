package br.edu.unisinos.rededepetri.service;

import br.edu.unisinos.rededepetri.controller.request.ConexaoRequest;
import br.edu.unisinos.rededepetri.controller.request.CriaConexaoRequest;
import br.edu.unisinos.rededepetri.controller.request.RedeDePetriRequest;
import br.edu.unisinos.rededepetri.controller.request.TransicaoRequest;
import br.edu.unisinos.rededepetri.domain.Conexao;
import br.edu.unisinos.rededepetri.domain.Lugar;
import br.edu.unisinos.rededepetri.domain.RedeDePetri;
import br.edu.unisinos.rededepetri.domain.Transicao;
import br.edu.unisinos.rededepetri.exception.ResourceNotFoundException;
import br.edu.unisinos.rededepetri.repository.RedeDePetriRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        System.out.println(RedeDePetriRepository.redeDePetri);
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
        Lugar lugarDeletado = RedeDePetriRepository.mapeamentoLugares.remove(nomeLugar);
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

    public void criaConexa(CriaConexaoRequest conexaoRequest) {
        Transicao transicao = RedeDePetriRepository.redeDePetri.getTransicaoList().stream().filter(t -> t.getNome().equals(conexaoRequest.getNomeTransicao())).findFirst().orElseThrow();
        Lugar lugar = RedeDePetriRepository.redeDePetri.getLugarList().stream().filter(l -> l.getNome().equals(conexaoRequest.getNomeLugar())).findFirst().orElseThrow();

        String nomeConexao;
        List<Conexao> conexaoList;

        if (conexaoRequest.isEntrada()) {
            conexaoList = transicao.getConexaoDeEntradaList();
            nomeConexao = conexaoRequest.getNomeLugar() + "->" + conexaoRequest.getNomeTransicao();
        } else {
            conexaoList = transicao.getConexaoDeSaidaList();
            nomeConexao = conexaoRequest.getNomeTransicao() + "->" + conexaoRequest.getNomeLugar();
        }

        conexaoList.add(new Conexao(nomeConexao, lugar, conexaoRequest.getPeso(), conexaoRequest.getTipoArco()));
    }

    public RedeDePetri getRedeDePetri() {
        return RedeDePetriRepository.redeDePetri;
    }

    public void criaTransicao(TransicaoRequest transicaoRequest) {
        Optional<Transicao> optionalTransicao = RedeDePetriRepository.redeDePetri.getTransicaoList().stream().filter(t -> t.getNome().equals(transicaoRequest.getNome())).findAny();
        if (optionalTransicao.isPresent()) {
            throw new RuntimeException("Transição: " + transicaoRequest.getNome() + " já existe");
        }

        List<ConexaoRequest> conexaoDeEntradaList = transicaoRequest.getConexaoDeEntradaList();
        List<ConexaoRequest> conexaoDeSaidaList = transicaoRequest.getConexaoDeSaidaList();

        for (ConexaoRequest conexaoDeEntrada : conexaoDeEntradaList) {
            Optional<Lugar> optionalLugar = RedeDePetriRepository.redeDePetri.getLugarList().stream().filter(l -> l.getNome().equals(conexaoDeEntrada.getNomeLugar())).findAny();
            if (optionalLugar.isEmpty()) {
                throw new RuntimeException("Lugar: " + conexaoDeEntrada.getNomeLugar() + " não existe");
            }
        }

        for (ConexaoRequest conexaoDeSaida : conexaoDeSaidaList) {
            Optional<Lugar> optionalLugar = RedeDePetriRepository.redeDePetri.getLugarList().stream().filter(l -> l.getNome().equals(conexaoDeSaida.getNomeLugar())).findAny();
            if (optionalLugar.isEmpty()) {
                throw new RuntimeException("Lugar: " + conexaoDeSaida.getNomeLugar() + " não existe");
            }
        }

        RedeDePetriRepository.redeDePetri.getTransicaoList().add(
                new Transicao(transicaoRequest.getNome(), transicaoRequest.getConexaoDeEntradaList().stream().map(conexaoRequest -> new Conexao(conexaoRequest.getNomeConexao(), RedeDePetriRepository.mapeamentoLugares.get(conexaoRequest.getNomeLugar()), conexaoRequest.getPeso(), conexaoRequest.getTipoArco())).collect(Collectors.toList()), transicaoRequest.getConexaoDeSaidaList().stream().map(conexaoRequest -> new Conexao(conexaoRequest.getNomeConexao(), RedeDePetriRepository.mapeamentoLugares.get(conexaoRequest.getNomeLugar()), conexaoRequest.getPeso(), conexaoRequest.getTipoArco())).collect(Collectors.toList())));
    }

    public void criaLugar(Lugar lugarRequest) {
        if (RedeDePetriRepository.mapeamentoLugares.containsKey(lugarRequest.getNome())) {
            throw new RuntimeException("Lugar: " + lugarRequest.getNome() + " já existe");
        }

        Lugar lugar = new Lugar(lugarRequest.getNome(), lugarRequest.getQuantidadeDeToken());
        RedeDePetriRepository.redeDePetri.getLugarList().add(lugar);
        RedeDePetriRepository.mapeamentoLugares.put(lugarRequest.getNome(), lugar);
    }

    public void deletarTransicao(String nomeTransicao) {
        Optional<Transicao> optionalTransicao = RedeDePetriRepository.redeDePetri.getTransicaoList().stream().filter(t -> t.getNome().equals(nomeTransicao)).findAny();

        if (optionalTransicao.isEmpty()) {
            throw new RuntimeException("Transição: " + nomeTransicao + " não existe");
        }

        RedeDePetriRepository.redeDePetri.getTransicaoList().remove(optionalTransicao.get());
    }
}
