package com.senai.conta_bancaria.application.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ContaAtualizacaoDTO(

        @NotNull(message = "O saldo é obrigatório.")
        BigDecimal saldo,

        @NotNull(message = "O limite é obrigatório.")
        BigDecimal limite,

        @NotNull(message = "O rendimento é obrigatório.")
        BigDecimal rendimento,

        @NotNull(message = "O taxa é obrigatório.")
        BigDecimal taxa
) {
}
