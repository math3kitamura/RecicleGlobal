package br.com.fiap.recicleglobal.bo;

import br.com.fiap.recicleglobal.dao.InstituicaoDAO;
import br.com.fiap.recicleglobal.dto.InstituicaoForm;
import br.com.fiap.recicleglobal.dto.InstituicaoView;
import br.com.fiap.recicleglobal.exception.RecursoNaoEncontradoException;
import br.com.fiap.recicleglobal.model.Instituicao;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class InstituicaoBO {
    private final InstituicaoDAO dao;

    public InstituicaoBO(InstituicaoDAO dao) { this.dao = dao; }

    @Transactional(readOnly = true)
    public List<InstituicaoView> listar() {
        return dao.findAll(Sort.by("nome")).stream().map(InstituicaoView::from).toList();
    }

    @Transactional(readOnly = true)
    public InstituicaoView buscar(Long id) { return InstituicaoView.from(encontrar(id)); }

    public InstituicaoView cadastrar(InstituicaoForm form) {
        Instituicao instituicao = new Instituicao(form.nome().trim(), form.descricao(),
                form.bairro().trim(), form.latitude(), form.longitude());
        return InstituicaoView.from(dao.save(instituicao));
    }

    public InstituicaoView atualizar(Long id, InstituicaoForm form) {
        Instituicao instituicao = encontrar(id);
        instituicao.atualizar(form.nome().trim(), form.descricao(), form.bairro().trim(),
                form.latitude(), form.longitude());
        return InstituicaoView.from(instituicao);
    }

    public void excluir(Long id) { dao.delete(encontrar(id)); }

    private Instituicao encontrar(Long id) {
        return dao.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Instituição " + id + " não encontrada."));
    }
}
