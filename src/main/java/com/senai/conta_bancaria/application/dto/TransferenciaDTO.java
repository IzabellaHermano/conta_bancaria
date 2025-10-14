package com.senai.conta_bancaria.application.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TransferenciaDTO(

        @NotNull(message = "Conta Destino não pode ser null")
        @NotBlank(message = "O número da conta é obrigatório.")
        @Pattern(regexp = "^\\\\d{4}-\\\\d{2}$", message = "O número da conta deve estar no formato 0000-00.")
        @Size(max = 7, message = "O número da conta deve ter 7 digítos, formato: 0000-00")
        String contaDestino,

        @NotNull
        @Positive(message = "O valor deve ser maior que ZERO")
        BigDecimal valor
) {
}
