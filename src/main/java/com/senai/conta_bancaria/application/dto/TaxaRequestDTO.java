package com.senai.conta_bancaria.application.dto;
import com.senai.conta_bancaria.domain.entity.Taxa;

import java.math.BigDecimal;

public record TaxaRequestDTO(
        String descricao,
        BigDecimal percentual
){
        public static TaxaRequestDTO fromEntity(Taxa taxa){
                return new TaxaRequestDTO(
                        taxa.getDescricao(),
                        taxa.getPercentual()
                );
        }
}
