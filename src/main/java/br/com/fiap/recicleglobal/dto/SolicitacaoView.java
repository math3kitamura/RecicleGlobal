package br.com.fiap.recicleglobal.dto;

import br.com.fiap.recicleglobal.model.Solicitacao;
import br.com.fiap.recicleglobal.model.StatusSolicitacao;
import java.time.LocalDateTime;

public record SolicitacaoView(Long id, Long doacaoId, String tituloDoacao, Long interessadoId,
                             String nomeInteressado, StatusSolicitacao status, LocalDateTime criadaEm) {
    public static SolicitacaoView from(Solicitacao solicitacao) {
        return new SolicitacaoView(solicitacao.getId(), solicitacao.getDoacao().getId(),
                solicitacao.getDoacao().getTitulo(), solicitacao.getInteressado().getId(),
                solicitacao.getInteressado().getNome(), solicitacao.getStatus(), solicitacao.getCriadaEm());
    }
}
