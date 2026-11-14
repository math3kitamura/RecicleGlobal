package br.com.fiap.recicleglobal.dto;

import jakarta.validation.constraints.NotNull;

public record SolicitacaoForm(@NotNull Long doacaoId, @NotNull Long interessadoId) {}
