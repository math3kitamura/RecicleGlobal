package br.com.fiap.recicleglobal.dao;

import br.com.fiap.recicleglobal.model.Solicitacao;
import br.com.fiap.recicleglobal.model.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolicitacaoDAO extends JpaRepository<Solicitacao, Long> {
    List<Solicitacao> findByDoacaoId(Long doacaoId);
    List<Solicitacao> findByDoacaoIdAndStatus(Long doacaoId, StatusSolicitacao status);
    boolean existsByDoacaoIdAndInteressadoIdAndStatus(Long doacaoId, Long interessadoId,
                                                       StatusSolicitacao status);
    boolean existsByDoacaoId(Long doacaoId);
}
