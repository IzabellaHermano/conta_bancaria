package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.enums.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PagamentoResponseDTO(
        String contaNumero,
        String boleto,
        BigDecimal valorPago,
        LocalDateTime dataPagamento,
        StatusPagamento status,
        List<TaxaResponseDTO> taxas
) {
    public static PagamentoResponseDTO fromEntity(Pagamento pagamento) {
        List<TaxaResponseDTO> taxas = pagamento.getTaxas().stream().map(TaxaResponseDTO::fromEntity).toList();

        return new PagamentoResponseDTO(
                pagamento.getConta().getNumero(),
                pagamento.getBoleto(),
                pagamento.getValorPago(),
                pagamento.getDataPagamento(),
                pagamento.getStatus(),
                taxas
        );
    }
}
