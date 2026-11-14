package br.com.fiap.recicleglobal.bo;

import br.com.fiap.recicleglobal.dao.DoacaoDAO;
import br.com.fiap.recicleglobal.dao.SolicitacaoDAO;
import br.com.fiap.recicleglobal.dao.UsuarioDAO;
import br.com.fiap.recicleglobal.dto.DoacaoForm;
import br.com.fiap.recicleglobal.dto.DoacaoView;
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
public class DoacaoBO {
    private final DoacaoDAO dao;
    private final UsuarioDAO usuarioDAO;
    private final SolicitacaoDAO solicitacaoDAO;

    public DoacaoBO(DoacaoDAO dao, UsuarioDAO usuarioDAO, SolicitacaoDAO solicitacaoDAO) {
        this.dao = dao;
        this.usuarioDAO = usuarioDAO;
        this.solicitacaoDAO = solicitacaoDAO;
    }

    @Transactional(readOnly = true)
    public List<DoacaoView> listar(CategoriaDoacao categoria, StatusDoacao status) {
        return dao.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .filter(d -> categoria == null || d.getCategoria() == categoria)
                .filter(d -> status == null || d.getStatus() == status)
                .filter(d -> d.getCategoria() != CategoriaDoacao.ALIMENTO
                        || d.getStatus() != StatusDoacao.DISPONIVEL
                        || !d.getDataValidade().isBefore(LocalDate.now()))
                .map(DoacaoView::from).toList();
    }

    @Transactional(readOnly = true)
    public DoacaoView buscar(Long id) { return DoacaoView.from(encontrar(id)); }

    public DoacaoView cadastrar(DoacaoForm form) {
        validarForm(form);
        Usuario usuario = usuarioDAO.findById(form.usuarioId()).orElseThrow(() ->
                new RecursoNaoEncontradoException("Usuário " + form.usuarioId() + " não encontrado."));
        Doacao doacao = new Doacao(usuario);
        aplicar(form, doacao);
        return DoacaoView.from(dao.save(doacao));
    }

    public DoacaoView atualizar(Long id, DoacaoForm form) {
        Doacao doacao = encontrar(id);
        if (doacao.getStatus() != StatusDoacao.DISPONIVEL) {
            throw new RegraNegocioException("Apenas doações disponíveis podem ser editadas.");
        }
        if (!doacao.getUsuario().getId().equals(form.usuarioId())) {
            throw new RegraNegocioException("Não é permitido trocar o doador de um anúncio.");
        }
        validarForm(form);
        aplicar(form, doacao);
        return DoacaoView.from(doacao);
    }

    public void excluir(Long id) {
        Doacao doacao = encontrar(id);
        if (solicitacaoDAO.existsByDoacaoId(id)) {
            throw new RegraNegocioException("Essa doação possui solicitações e não pode ser excluída.");
        }
        dao.delete(doacao);
    }

    private void aplicar(DoacaoForm form, Doacao doacao) {
        doacao.atualizar(form.categoria(), form.titulo().trim(), form.descricao(),
                form.quantidade(), form.dataValidade(), form.bairro().trim(),
                form.latitude(), form.longitude());
    }

    private void validarForm(DoacaoForm form) {
        if (form.categoria() == CategoriaDoacao.ALIMENTO) {
            if (form.dataValidade() == null) {
                throw new RegraNegocioException("Alimentos precisam de uma data de validade.");
            }
            if (form.dataValidade().isBefore(LocalDate.now())) {
                throw new RegraNegocioException("Não é permitida a doação de alimentos vencidos.");
            }
        } else if (form.dataValidade() != null) {
            throw new RegraNegocioException("Roupas não devem ter data de validade.");
        }
    }

    private Doacao encontrar(Long id) {
        return dao.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Doação " + id + " não encontrada."));
    }
}
