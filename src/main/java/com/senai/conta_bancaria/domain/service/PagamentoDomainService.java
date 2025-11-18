package com.senai.conta_bancaria.domain.service;

import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.entity.Taxa;
import com.senai.conta_bancaria.domain.enums.StatusPagamento;
import com.senai.conta_bancaria.domain.exception.PagamentoInvalidoException;
import com.senai.conta_bancaria.domain.exception.SaldoInsuficienteException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagamentoDomainService {

    public void validarBoleto(String boleto) {

        if (boleto.startsWith("999")) {
            throw new PagamentoInvalidoException("Boleto vencido. A data de vencimento foi ultrapassada.");
        }

    }

    public BigDecimal calcularValorTotalDebitado(BigDecimal valorPrincipal, List<Taxa> taxas) {
        BigDecimal valorTotal = valorPrincipal;

        for (Taxa taxa : taxas) {

            BigDecimal valorTaxaPercentual = valorPrincipal.multiply(taxa.getPercentual())
                    .setScale(2, RoundingMode.HALF_UP);

            valorTotal = valorTotal.add(valorTaxaPercentual);


            if (taxa.getValorFixo() != null) {
                valorTotal = valorTotal.add(taxa.getValorFixo());
            }
        }

        return valorTotal.setScale(2, RoundingMode.HALF_UP);
    }
    @Transactional // A transação deve ser propagada do AppService
    public Pagamento processarPagamento(Conta conta, String boleto, BigDecimal valorPrincipal, List<Taxa> taxas) {
        Pagamento pagamento = Pagamento.builder()
                .conta(conta)
                .boleto(boleto)
                .valorPago(valorPrincipal)
                .dataPagamento(LocalDateTime.now())
                .status(StatusPagamento.PENDENTE)
                .taxas(taxas)
                .build();

        try {

            validarBoleto(boleto);


            BigDecimal valorTotalDebitado = calcularValorTotalDebitado(valorPrincipal, taxas);
            pagamento.setValorTotalDebitado(valorTotalDebitado);

            conta.sacar(valorTotalDebitado);


            pagamento.setStatus(StatusPagamento.SUCESSO);

        } catch (SaldoInsuficienteException e) {
            pagamento.setStatus(StatusPagamento.SALDO_INSUFICIENTE);
            throw e;

        } catch (PagamentoInvalidoException e) {

            pagamento.setStatus(StatusPagamento.BOLETO_VENCIDO);
            throw e;

        } catch (Exception e) {

            pagamento.setStatus(StatusPagamento.FALHA);
            throw new PagamentoInvalidoException("Erro inesperado no processamento do pagamento.");
        }

        return pagamento;
    }
}
