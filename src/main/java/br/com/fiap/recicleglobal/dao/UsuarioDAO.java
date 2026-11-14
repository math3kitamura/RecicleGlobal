package br.com.fiap.recicleglobal.dao;

import br.com.fiap.recicleglobal.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioDAO extends JpaRepository<Usuario, Long> {}
