package com.senai.conta_bancaria.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ValorSaqueDespositoDTO(

        @NotNull
        @Positive(message = "O valor deve ser maior que ZERO")
        BigDecimal valor
){
}
