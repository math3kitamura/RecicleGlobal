package br.com.fiap.recicleglobal.dao;

import br.com.fiap.recicleglobal.model.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstituicaoDAO extends JpaRepository<Instituicao, Long> {}
