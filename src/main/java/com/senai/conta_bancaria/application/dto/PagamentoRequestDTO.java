package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.enums.StatusPagamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public record PagamentoRequestDTO(
        String contaNumero,
        String boleto,
        BigDecimal valorPago,
        StatusPagamento status,
        List<TaxaRequestDTO> taxas,
        String codigoAutenticacao
) {
        public Pagamento toEntity(Conta conta) {
                return Pagamento.builder()
                        .conta(conta)
                        .boleto(this.boleto)
                        .valorPago(this.valorPago)
                        .status(StatusPagamento.PENDENTE)
                        .taxas(new ArrayList<>())
                        .build();
        }
}
