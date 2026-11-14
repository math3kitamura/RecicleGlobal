package br.com.fiap.recicleglobal.dto;

import br.com.fiap.recicleglobal.model.CategoriaDoacao;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DoacaoForm(
        @NotNull Long usuarioId,
        @NotNull CategoriaDoacao categoria,
        @NotBlank @Size(max = 120) String titulo,
        @Size(max = 500) String descricao,
        @NotNull @Positive Integer quantidade,
        LocalDate dataValidade,
        @NotBlank @Size(max = 100) String bairro,
        @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal latitude,
        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal longitude
) {}
