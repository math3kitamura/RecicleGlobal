package br.com.fiap.recicleglobal.bo;

import br.com.fiap.recicleglobal.dao.DoacaoDAO;
import br.com.fiap.recicleglobal.dao.SolicitacaoDAO;
import br.com.fiap.recicleglobal.dao.UsuarioDAO;
import br.com.fiap.recicleglobal.dto.SolicitacaoForm;
import br.com.fiap.recicleglobal.dto.SolicitacaoView;
import br.com.fiap.recicleglobal.exception.RecursoNaoEncontradoException;
import br.com.fiap.recicleglobal.exception.RegraNegocioException;
import br.com.fiap.recicleglobal.model.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class SolicitacaoBO {
    private final SolicitacaoDAO dao;
    private final DoacaoDAO doacaoDAO;
    private final UsuarioDAO usuarioDAO;

    public SolicitacaoBO(SolicitacaoDAO dao, DoacaoDAO doacaoDAO, UsuarioDAO usuarioDAO) {
        this.dao = dao;
        this.doacaoDAO = doacaoDAO;
        this.usuarioDAO = usuarioDAO;
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoView> listar(Long doacaoId) {
        List<Solicitacao> solicitacoes = doacaoId == null
                ? dao.findAll(Sort.by(Sort.Direction.DESC, "id")) : dao.findByDoacaoId(doacaoId);
        return solicitacoes.stream().map(SolicitacaoView::from).toList();
    }

    public SolicitacaoView criar(SolicitacaoForm form) {
        Doacao doacao = doacaoDAO.buscarParaAtualizar(form.doacaoId()).orElseThrow(() ->
                new RecursoNaoEncontradoException("Doação " + form.doacaoId() + " não encontrada."));
        Usuario interessado = usuarioDAO.findById(form.interessadoId()).orElseThrow(() ->
                new RecursoNaoEncontradoException("Usuário " + form.interessadoId() + " não encontrado."));
        if (doacao.getStatus() != StatusDoacao.DISPONIVEL) {
            throw new RegraNegocioException("Esta doação não está disponível.");
        }
        if (doacao.getUsuario().getId().equals(interessado.getId())) {
            throw new RegraNegocioException("O doador não pode solicitar a própria doação.");
        }
        if (doacao.getCategoria() == CategoriaDoacao.ALIMENTO
                && doacao.getDataValidade().isBefore(LocalDate.now())) {
            throw new RegraNegocioException("Este alimento já venceu.");
        }
        if (dao.existsByDoacaoIdAndInteressadoIdAndStatus(doacao.getId(), interessado.getId(),
                StatusSolicitacao.PENDENTE)) {
            throw new RegraNegocioException("Este usuário já possui uma solicitação pendente.");
        }
        return SolicitacaoView.from(dao.save(new Solicitacao(doacao, interessado)));
    }

    public SolicitacaoView aceitar(Long id) {
        Solicitacao solicitacao = encontrar(id);
        Doacao doacao = doacaoDAO.buscarParaAtualizar(solicitacao.getDoacao().getId()).orElseThrow();
        if (solicitacao.getStatus() != StatusSolicitacao.PENDENTE
                || doacao.getStatus() != StatusDoacao.DISPONIVEL) {
            throw new RegraNegocioException("A solicitação não pode ser aceita neste estado.");
        }
        if (doacao.getCategoria() == CategoriaDoacao.ALIMENTO
                && doacao.getDataValidade().isBefore(LocalDate.now())) {
            throw new RegraNegocioException("Não é possível reservar um alimento vencido.");
        }
        doacao.setStatus(StatusDoacao.RESERVADA);
        solicitacao.setStatus(StatusSolicitacao.ACEITA);
        for (Solicitacao outra : dao.findByDoacaoIdAndStatus(doacao.getId(), StatusSolicitacao.PENDENTE)) {
            if (!outra.getId().equals(id)) {
                outra.setStatus(StatusSolicitacao.RECUSADA);
            }
        }
        return SolicitacaoView.from(solicitacao);
    }

    public SolicitacaoView concluir(Long id) {
        Solicitacao solicitacao = encontrar(id);
        if (solicitacao.getStatus() != StatusSolicitacao.ACEITA) {
            throw new RegraNegocioException("Somente uma solicitação aceita pode ser concluída.");
        }
        Doacao doacao = solicitacao.getDoacao();
        if (doacao.getCategoria() == CategoriaDoacao.ALIMENTO
                && doacao.getDataValidade().isBefore(LocalDate.now())) {
            throw new RegraNegocioException("Não é possível concluir a entrega de alimento vencido.");
        }
        solicitacao.setStatus(StatusSolicitacao.CONCLUIDA);
        doacao.setStatus(StatusDoacao.CONCLUIDA);
        return SolicitacaoView.from(solicitacao);
    }

    public SolicitacaoView cancelar(Long id) {
        Solicitacao solicitacao = encontrar(id);
        if (solicitacao.getStatus() != StatusSolicitacao.PENDENTE
                && solicitacao.getStatus() != StatusSolicitacao.ACEITA) {
            throw new RegraNegocioException("Somente solicitações pendentes ou aceitas podem ser canceladas.");
        }
        if (solicitacao.getStatus() == StatusSolicitacao.ACEITA) {
            solicitacao.getDoacao().setStatus(StatusDoacao.DISPONIVEL);
        }
        solicitacao.setStatus(StatusSolicitacao.CANCELADA);
        return SolicitacaoView.from(solicitacao);
    }

    private Solicitacao encontrar(Long id) {
        return dao.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Solicitação " + id + " não encontrada."));
    }
}
