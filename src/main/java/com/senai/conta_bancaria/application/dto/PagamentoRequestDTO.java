package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.enums.StatusPagamento;
import com.senai.conta_bancaria.domain.enums.TipoPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public record PagamentoRequestDTO(
        String contaNumero,
        String boleto,
        BigDecimal valorPago,
        StatusPagamento status,

        TipoPagamento tipoPagamento,
        String codigoAutenticacao
) {
        public Pagamento toEntity(Conta conta) {
                return Pagamento.builder()
                        .conta(conta)
                        .boleto(this.boleto)
                        .valorPago(this.valorPago)
                        .dataPagamento(LocalDateTime.now())
                        .status(StatusPagamento.PENDENTE)
                        .tipoPagamento(this.tipoPagamento)
                        .taxas(new ArrayList<>())
                        .build();
        }
}
