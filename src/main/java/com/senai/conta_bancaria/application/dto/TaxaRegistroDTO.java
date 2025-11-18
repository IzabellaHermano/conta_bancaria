package com.senai.conta_bancaria.application.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TaxaRegistroDTO(
        @NotBlank(message = "A descrição da taxa é obrigatória")
        @Schema(description = "Nome da taxa (ex: IOF, Tarifa Bancária)", example = "Tarifa de Processamento")
        String descricao,

        @NotNull(message = "O percentual é obrigatório")
        @DecimalMin(value = "0.0000", inclusive = true, message = "O percentual deve ser positivo ou zero")
        @DecimalMax(value = "1.0000", inclusive = true, message = "O percentual deve ser no máximo 1.0000 (100%)")
        @Schema(description = "Taxa percentual sobre o valor da operação (ex: 0.01 para 1%)", example = "0.0050")
        BigDecimal percentual,

        @Schema(description = "Valor fixo adicional opcional", example = "2.50")
        BigDecimal valorFixo
) {
}
