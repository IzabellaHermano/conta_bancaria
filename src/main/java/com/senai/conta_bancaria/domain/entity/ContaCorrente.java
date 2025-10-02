package com.senai.conta_bancaria.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.bind.annotation.PutMapping;

import java.math.BigDecimal;


@Entity
@DiscriminatorValue("CORRENTE")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class ContaCorrente extends Conta {

    @Column(precision = 4,scale = 2)
    private BigDecimal limite;

    @Column(precision = 5,scale = 2)
    private BigDecimal taxa;

    @Override
    public String getTipo() {
        return "CORRENTE";
    }

    //polimorfismo(subscreve o metodo da classe "pai")
    @Override
    public void sacar(BigDecimal valor) {
        validaValorMaiorQueZero(valor);

        BigDecimal custoSaque = valor.multiply(taxa);
        BigDecimal totalSaque = valor.add(custoSaque);

        if (this.getSaldo().add(this.limite).compareTo(totalSaque) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para o saque");
        }

        this.setSaldo(this.getSaldo().subtract(totalSaque));
    }
}
