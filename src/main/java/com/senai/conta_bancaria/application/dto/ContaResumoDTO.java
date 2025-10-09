package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.ContaCorrente;
import com.senai.conta_bancaria.domain.entity.ContaPoupanca;
import com.senai.conta_bancaria.domain.exception.TipoDeContaInvalidaException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ContaResumoDTO(

        @NotBlank(message = "O número da conta é obrigatório.")
        @Pattern(regexp = "^\\\\d{4}-\\\\d{2}$", message = "O número da conta deve estar no formato 0000-00.")
        @Size(max = 7, message = "O número da conta deve ter 7 digítos, formato: 0000-00")
        String numero,

        @NotBlank(message = "O tipo da conta é obrigatório.")
        @Pattern(regexp = "CORRENTE|POUPANCA", message = "Tipo inválido. Valores válidos: CORRENTE, POUPANCA")
        String tipo,

        @NotNull(message = "O saldo é obrigatório.")
        BigDecimal saldo
) {

    public Conta toEntity(Cliente cliente) {
        if("CORRENTE".equalsIgnoreCase(tipo)) {
            return ContaCorrente.builder()
                    .cliente(cliente)
                    .numero(this.numero)
                    .saldo(this.saldo)
                    .taxa(new BigDecimal("0.05"))
                    .limite(new BigDecimal("500.00"))
                    .ativa(true)
                    .build();
        } else if ("POUPANCA".equalsIgnoreCase(tipo)) {
            return ContaPoupanca.builder()
                    .cliente(cliente)
                    .numero(this.numero)
                    .saldo(this.saldo)
                    .rendimento(new BigDecimal("0.01"))
                    .ativa(true)
                    .build();
        }
        throw new TipoDeContaInvalidaException(tipo);
    }
    public static ContaResumoDTO fromEntity(Conta conta) {
        return new ContaResumoDTO(
                conta.getNumero(),
                conta.getTipo(),
                conta.getSaldo()
        );
    }
}
