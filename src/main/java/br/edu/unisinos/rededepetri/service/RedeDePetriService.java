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

        Optional<Conexao> optionalConexao = conexaoList.stream().filter(c -> c.getNomeConexao().equals(nomeConexao)).findAny();

        if (optionalConexao.isPresent()) {
            throw new RuntimeException("Conexão: " + nomeConexao + " já existe");
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

        procuraLugar(conexaoDeEntradaList);
        procuraLugar(conexaoDeSaidaList);

        RedeDePetriRepository.redeDePetri.getTransicaoList().add(
                new Transicao(transicaoRequest.getNome(), transicaoRequest.getConexaoDeEntradaList().stream().map(conexaoRequest -> new Conexao(conexaoRequest.getNomeConexao(), RedeDePetriRepository.mapeamentoLugares.get(conexaoRequest.getNomeLugar()), conexaoRequest.getPeso(), conexaoRequest.getTipoArco())).collect(Collectors.toList()), transicaoRequest.getConexaoDeSaidaList().stream().map(conexaoRequest -> new Conexao(conexaoRequest.getNomeConexao(), RedeDePetriRepository.mapeamentoLugares.get(conexaoRequest.getNomeLugar()), conexaoRequest.getPeso(), conexaoRequest.getTipoArco())).collect(Collectors.toList())));
    }

    private void procuraLugar(List<ConexaoRequest> conexaoList) {
        for (ConexaoRequest conexaoDeEntrada : conexaoList) {
            Optional<Lugar> optionalLugar = RedeDePetriRepository.redeDePetri.getLugarList().stream().filter(l -> l.getNome().equals(conexaoDeEntrada.getNomeLugar())).findAny();
            if (optionalLugar.isEmpty()) {
                throw new RuntimeException("Lugar: " + conexaoDeEntrada.getNomeLugar() + " não existe");
            }
        }
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

    public void deletarConexao(String nomeConexao) {
        Optional<Transicao> optionalTransicao = RedeDePetriRepository.redeDePetri.getTransicaoList().stream().filter(t -> {
            Optional<Conexao> conexaoDeEntrada = t.getConexaoDeEntradaList().stream().filter(c -> c.getNomeConexao().equals(nomeConexao)).findAny();
            Optional<Conexao> conexaoDeSaida = t.getConexaoDeSaidaList().stream().filter(c -> c.getNomeConexao().equals(nomeConexao)).findAny();
            return conexaoDeEntrada.isPresent() || conexaoDeSaida.isPresent();
        }).findAny();

        if (optionalTransicao.isEmpty()) {
            throw new RuntimeException("");
        }

        Transicao transicao = optionalTransicao.get();

        transicao.getConexaoDeEntradaList().stream().filter(c -> c.getNomeConexao().equals(nomeConexao)).findAny().ifPresent(conexao -> transicao.getConexaoDeEntradaList().remove(conexao));
        transicao.getConexaoDeSaidaList().stream().filter(c -> c.getNomeConexao().equals(nomeConexao)).findAny().ifPresent(conexao -> transicao.getConexaoDeSaidaList().remove(conexao));
    }

    public String imprimeTabela() {
        if (RedeDePetriRepository.ciclos.isEmpty()) {
            throw new RuntimeException("Lista vazia");
        }

        StringBuilder tabela = new StringBuilder("| Nº do ciclo | ");
        RedeDePetriRepository.ciclos.get(0).getLugares().forEach(lugar -> tabela.append(lugar.getNome()).append(" | "));
        RedeDePetriRepository.ciclos.get(0).getTransicoes().forEach(transicao -> tabela.append(transicao.getNome()).append(" | "));
        tabela.append("\n");

        for (int i = 0; i < RedeDePetriRepository.ciclos.size(); i++) {
            tabela.append("|      ").append(i).append("      | ");
            RedeDePetriRepository.ciclos.get(i).getLugares().forEach(lugar -> tabela.append(lugar.getQuantidadeDeToken()).append(" | "));
            RedeDePetriRepository.ciclos.get(i).getTransicoes().forEach(transicao -> tabela.append(transicao.isHabilitada() ? "S " : "N ").append(" | "));
        }

        System.out.println(tabela);
        return tabela.toString();
    }

    public void adicionaToken(String nomeLugar) {
        if (RedeDePetriRepository.mapeamentoLugares.containsKey(nomeLugar)) {
            RedeDePetriRepository.mapeamentoLugares.get(nomeLugar).setQuantidadeDeToken(RedeDePetriRepository.mapeamentoLugares.get(nomeLugar).getQuantidadeDeToken() + 1);
        }
        throw new ResourceNotFoundException();
    }

    public void removeToken(String nomeLugar) {
        if (RedeDePetriRepository.mapeamentoLugares.containsKey(nomeLugar)) {
            RedeDePetriRepository.mapeamentoLugares.get(nomeLugar).setQuantidadeDeToken(RedeDePetriRepository.mapeamentoLugares.get(nomeLugar).getQuantidadeDeToken() - 1);
        }
        throw new ResourceNotFoundException();
    }
}
