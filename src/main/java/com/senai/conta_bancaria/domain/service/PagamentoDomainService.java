package com.senai.conta_bancaria.domain.service;


import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.entity.Taxa;
import com.senai.conta_bancaria.domain.enums.StatusPagamento;
import com.senai.conta_bancaria.domain.exception.PagamentoInvalidoException;
import com.senai.conta_bancaria.domain.exception.SaldoInsuficienteException;
import com.senai.conta_bancaria.domain.exception.TaxaInvalidaException;
import com.senai.conta_bancaria.domain.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class PagamentoDomainService {

    private static PagamentoRepository repository;


    public static void validarPagamento(Pagamento pagamento) {
        if (pagamento.getValorPago() == null || pagamento.getValorPago().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PagamentoInvalidoException("Valor do pagamento deve ser positivo.");
        }

        if (pagamento.getConta() == null || !pagamento.getConta().isAtiva()) {
            throw new PagamentoInvalidoException("Conta inválida ou inativa.");
        }

        if (pagamento.getConta().getSaldo().compareTo(pagamento.getValorPago()) < 0) {
            throw new SaldoInsuficienteException("realizar pagamento");
        }

    }

    public static void calcularTaxa(Pagamento pagamento) {
        BigDecimal valorBase = pagamento.getValorPago();
        BigDecimal totalTaxas = BigDecimal.ZERO;

        if (pagamento.getTaxas() != null) {
            for (Taxa taxa : pagamento.getTaxas()) {

                if (taxa.getPercentual() != null && taxa.getPercentual().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal valorPercentual = valorBase.multiply(taxa.getPercentual());
                    totalTaxas = totalTaxas.add(valorPercentual);
                } else throw new TaxaInvalidaException("Percentual da taxa nulo, ou negativo.");


                if (taxa.getValorFixo() != null && taxa.getValorFixo().compareTo(BigDecimal.ZERO) > 0) {
                    totalTaxas = totalTaxas.add(taxa.getValorFixo());
                } else throw new TaxaInvalidaException("Valor fixo da taxa nulo, ou negativo.");
            }
        }

        pagamento.setValorPago(valorBase.add(totalTaxas));
    }

    public static void definirStatus(Pagamento pagamento, boolean respostaValidaMQTT) {
        if (!respostaValidaMQTT) {
            pagamento.setStatus(StatusPagamento.FALHA);
            return;
        }

        if (pagamento.getConta().getSaldo().compareTo(pagamento.getValorPago()) >= 0) {
            pagamento.setStatus(StatusPagamento.PAGO);
            pagamento.getConta().sacar(pagamento.getValorPago());
        } else {
            pagamento.setStatus(StatusPagamento.FALHA);
        }
    }

}