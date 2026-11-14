package br.com.fiap.recicleglobal.bo;

import br.com.fiap.recicleglobal.dao.DoacaoDAO;
import br.com.fiap.recicleglobal.dao.SolicitacaoDAO;
import br.com.fiap.recicleglobal.dao.UsuarioDAO;
import br.com.fiap.recicleglobal.dto.DoacaoForm;
import br.com.fiap.recicleglobal.exception.RegraNegocioException;
import br.com.fiap.recicleglobal.model.CategoriaDoacao;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class DoacaoBOTest {
    private final DoacaoBO bo = new DoacaoBO(
            mock(DoacaoDAO.class), mock(UsuarioDAO.class), mock(SolicitacaoDAO.class));

    @Test
    void alimentoComValidadeVencidaEhRejeitado() {
        DoacaoForm form = form(CategoriaDoacao.ALIMENTO, LocalDate.now().minusDays(1));

        RegraNegocioException erro = assertThrows(RegraNegocioException.class, () -> bo.cadastrar(form));

        assertTrue(erro.getMessage().contains("vencidos"));
    }

    @Test
    void alimentoSemValidadeEhRejeitado() {
        assertThrows(RegraNegocioException.class,
                () -> bo.cadastrar(form(CategoriaDoacao.ALIMENTO, null)));
    }

    @Test
    void roupaComDataDeValidadeEhRejeitada() {
        assertThrows(RegraNegocioException.class,
                () -> bo.cadastrar(form(CategoriaDoacao.ROUPA, LocalDate.now().plusDays(10))));
    }

    private DoacaoForm form(CategoriaDoacao categoria, LocalDate validade) {
        return new DoacaoForm(1L, categoria, "Teste", "Descricao", 2, validade,
                "Centro", new BigDecimal("-23.5509000"), new BigDecimal("-46.6333000"));
    }
}
