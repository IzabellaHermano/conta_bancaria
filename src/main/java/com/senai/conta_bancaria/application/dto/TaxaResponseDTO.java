package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Taxa;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record TaxaResponseDTO(
        @Schema(description = "ID único da Taxa")
        String id,

        @Schema(description = "Nome da taxa", example = "Tarifa de Processamento")
        String descricao,

        @Schema(description = "Taxa percentual sobre o valor da operação", example = "0.0050")
        BigDecimal percentual,

        @Schema(description = "Valor fixo adicional", example = "2.50")
        BigDecimal valorFixo
) {
    public static TaxaResponseDTO fromEntity(Taxa taxa) {
        return new TaxaResponseDTO(
                taxa.getId(),
                taxa.getDescricao(),
                taxa.getPercentual(),
                taxa.getValorFixo()
        );
    }
}
