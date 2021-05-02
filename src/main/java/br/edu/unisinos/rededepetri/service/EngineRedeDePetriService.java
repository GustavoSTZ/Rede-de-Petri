package br.edu.unisinos.rededepetri.service;

import br.edu.unisinos.rededepetri.domain.Conexao;
import br.edu.unisinos.rededepetri.domain.TipoArco;
import br.edu.unisinos.rededepetri.domain.Transicao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static br.edu.unisinos.rededepetri.repository.RedeDePetriRepository.mapeamentoLugares;
import static br.edu.unisinos.rededepetri.repository.RedeDePetriRepository.redeDePetri;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EngineRedeDePetriService {

    public void executarEngine() {
        AtomicBoolean executouTransicao = new AtomicBoolean(false);
        redeDePetri.getTransicaoList()
                .forEach(transicao -> {
                    if (transicao.getConexaoDeEntradaList()
                            .stream()
                            .allMatch(conexao -> conexao.getPeso() <= conexao.getLugar().getQuantidadeDeToken())
                            &&
                            transicao.getConexaoDeEntradaList()
                            .stream()
                            .noneMatch(conexao -> conexao.getTipoArco().equals(TipoArco.INIBIDOR)
                                    && conexao.getPeso() <= conexao.getLugar().getQuantidadeDeToken())
                    ) {
                        executarTransicaoEntrada(transicao);
                        executouTransicao.set(true);
                    }
                    atualizaLugares(transicao.getConexaoDeEntradaList());
                    atualizaLugares(transicao.getConexaoDeSaidaList());
                });
        if (executouTransicao.get()) {
            executarEngine();
        }
    }

    private void atualizaLugares(List<Conexao> conexaoList) {
        conexaoList.forEach(conexao ->
                conexao.getLugar()
                        .setQuantidadeDeToken(
                                mapeamentoLugares.get(
                                        conexao.getLugar()
                                                .getNome()
                                ).getQuantidadeDeToken()
                        )
        );
    }

    public void executarTransicaoEntrada(Transicao transicaoEntrada) {
        transicaoEntrada.getConexaoDeEntradaList().forEach(this::consomeToken);

        redeDePetri.getTransicaoList()
                .stream()
                .filter(transicao -> transicao.getNome().equals(transicaoEntrada.getNome()))
                .findFirst()
                .ifPresent(this::executarTransicaoSaida);
    }

    private void executarTransicaoSaida(Transicao transicaoSaida) {
        transicaoSaida.getConexaoDeSaidaList().forEach(this::distribuiToken);
    }

    private void consomeToken(Conexao conexao) {
        if(!conexao.getTipoArco().equals(TipoArco.INIBIDOR)){
            mapeamentoLugares.get(conexao.getLugar().getNome()).setQuantidadeDeToken(mapeamentoLugares.get(conexao.getLugar().getNome()).getQuantidadeDeToken() - (conexao.getTipoArco().equals(TipoArco.NORMAL) ? conexao.getPeso() : mapeamentoLugares.get(conexao.getLugar().getNome()).getQuantidadeDeToken()));
            conexao.getLugar().setQuantidadeDeToken(mapeamentoLugares.get(conexao.getLugar().getNome()).getQuantidadeDeToken());
        }
    }

    private void distribuiToken(Conexao conexao) {
        mapeamentoLugares.get(conexao.getLugar().getNome()).setQuantidadeDeToken(mapeamentoLugares.get(conexao.getLugar().getNome()).getQuantidadeDeToken() + conexao.getPeso());
        conexao.getLugar().setQuantidadeDeToken(mapeamentoLugares.get(conexao.getLugar().getNome()).getQuantidadeDeToken());
    }
}
