package br.com.fiap.recicleglobal.dao;

import br.com.fiap.recicleglobal.model.Doacao;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface DoacaoDAO extends JpaRepository<Doacao, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select d from Doacao d where d.id = :id")
    Optional<Doacao> buscarParaAtualizar(@Param("id") Long id);
}
