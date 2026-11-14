package br.com.fiap.recicleglobal.dto;

import br.com.fiap.recicleglobal.model.Instituicao;
import java.math.BigDecimal;

public record InstituicaoView(Long id, String nome, String descricao, String bairro,
                             BigDecimal latitude, BigDecimal longitude) {
    public static InstituicaoView from(Instituicao instituicao) {
        return new InstituicaoView(instituicao.getId(), instituicao.getNome(), instituicao.getDescricao(),
                instituicao.getBairro(), instituicao.getLatitude(), instituicao.getLongitude());
    }
}
