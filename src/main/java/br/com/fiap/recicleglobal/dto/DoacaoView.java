package br.com.fiap.recicleglobal.dto;

import br.com.fiap.recicleglobal.model.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DoacaoView(Long id, Long usuarioId, String nomeDoador, CategoriaDoacao categoria,
                         String titulo, String descricao, Integer quantidade, LocalDate dataValidade,
                         String bairro, BigDecimal latitude, BigDecimal longitude,
                         StatusDoacao status, LocalDateTime criadaEm) {
    public static DoacaoView from(Doacao doacao) {
        return new DoacaoView(doacao.getId(), doacao.getUsuario().getId(),
                doacao.getUsuario().getNome(), doacao.getCategoria(), doacao.getTitulo(),
                doacao.getDescricao(), doacao.getQuantidade(), doacao.getDataValidade(),
                doacao.getBairro(), doacao.getLatitude(), doacao.getLongitude(),
                doacao.getStatus(), doacao.getCriadaEm());
    }
}
