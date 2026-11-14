package br.com.fiap.recicleglobal.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record InstituicaoForm(
        @NotBlank @Size(max = 120) String nome,
        @Size(max = 500) String descricao,
        @NotBlank @Size(max = 100) String bairro,
        @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal latitude,
        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal longitude
) {}
