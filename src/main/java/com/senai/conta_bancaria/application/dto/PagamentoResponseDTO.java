package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.enums.StatusPagamento;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record PagamentoResponseDTO(
        @Schema(description = "ID único do Pagamento")
        String id,

        @Schema(description = "Número da conta de origem", example = "1234-56")
        String contaOrigem,

        @Schema(description = "Identificador do boleto/serviço", example = "34191.00008...")
        String boleto,

        @Schema(description = "Valor principal do pagamento", example = "450.75")
        BigDecimal valorPago,

        @Schema(description = "Valor total debitado da conta (incluindo taxas)", example = "455.25")
        BigDecimal valorTotalDebitado,

        @Schema(description = "Data e hora da operação")
        LocalDateTime dataPagamento,

        @Schema(description = "Status do pagamento", example = "SUCESSO")
        StatusPagamento status,

        @Schema(description = "Lista de taxas aplicadas")
        List<TaxaResponseDTO> taxas
) {
    public static PagamentoResponseDTO fromEntity(Pagamento pagamento) {
        List<TaxaResponseDTO> taxasDTO = pagamento.getTaxas().stream()
                .map(TaxaResponseDTO::fromEntity).toList();

        return new PagamentoResponseDTO(
                pagamento.getId(),
                pagamento.getConta().getNumero(),
                pagamento.getBoleto(),
                pagamento.getValorPago(),
                pagamento.getValorTotalDebitado(),
                pagamento.getDataPagamento(),
                pagamento.getStatus(),
                taxasDTO
        );
    }
}
