package br.com.fiap.recicleglobal.bo;

import br.com.fiap.recicleglobal.dao.UsuarioDAO;
import br.com.fiap.recicleglobal.dto.UsuarioForm;
import br.com.fiap.recicleglobal.dto.UsuarioView;
import br.com.fiap.recicleglobal.exception.RecursoNaoEncontradoException;
import br.com.fiap.recicleglobal.model.Usuario;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class UsuarioBO {
    private final UsuarioDAO dao;

    public UsuarioBO(UsuarioDAO dao) { this.dao = dao; }

    @Transactional(readOnly = true)
    public List<UsuarioView> listar() {
        return dao.findAll(Sort.by("id")).stream().map(UsuarioView::from).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioView buscar(Long id) { return UsuarioView.from(encontrar(id)); }

    public UsuarioView cadastrar(UsuarioForm form) {
        Usuario usuario = new Usuario(form.nome().trim(), emailNormalizado(form.email()));
        return UsuarioView.from(dao.save(usuario));
    }

    public UsuarioView atualizar(Long id, UsuarioForm form) {
        Usuario usuario = encontrar(id);
        usuario.setNome(form.nome().trim());
        usuario.setEmail(emailNormalizado(form.email()));
        return UsuarioView.from(usuario);
    }

    public void excluir(Long id) { dao.delete(encontrar(id)); }

    private Usuario encontrar(Long id) {
        return dao.findById(id).orElseThrow(() ->
                new RecursoNaoEncontradoException("Usuário " + id + " não encontrado."));
    }

    private String emailNormalizado(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
