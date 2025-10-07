package com.senai.conta_bancaria.domain.entity;

import com.senai.conta_bancaria.domain.exception.SaldoInsuficienteException;
import com.senai.conta_bancaria.domain.exception.TransferirParaMesmaContaException;
import com.senai.conta_bancaria.domain.exception.ValoresNegativosException;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


import java.math.BigDecimal;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)//ESTRATEGIA DE COMO SERA FEITA A TABELA NO BANCO
@DiscriminatorColumn(name = "tipo_conta", discriminatorType = DiscriminatorType.STRING, length = 20)//DETERMINA O NOME DA COLUNA ADICIONAL, O TIPO DE ATRIBUTO QUE VAI RECEBER E O TAMANHO
@Table(name = "contaDTO",
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_conta_numero", columnNames = {"numero"}),
            @UniqueConstraint(name = "uk_cliente_tipo", columnNames = {"cliente_id","tipo_conta"})
        })
@Data
@SuperBuilder//POIS TEM CLASSES QUE HERDAM DELA
@NoArgsConstructor
public abstract class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 20)//NÃO PERMITE QUE SEJA NULO, LIMITA O TAMANHO
    private String numero;

    @Column(nullable = false, precision = 19,scale = 2)//precision - QUANTAS CASAS DECIMAIS PODEM TER
    private BigDecimal saldo; //substitui o tipo primitivo

    @Column(nullable = false)
    private boolean ativa;

    @ManyToOne(fetch = FetchType.LAZY)//fetch = FetchType.LAZY- BUSCA UMA UNICA VEZ A INFORMÇÃO E PARA(LIMITA)
    @JoinColumn(name = "cliente_id",foreignKey = @ForeignKey(name = "fk_conta_cliente"))
    private Cliente cliente;

    public abstract String getTipo();

    public void sacar(BigDecimal valor) {
        validaValorMaiorQueZero(valor,"saque");
        if(valor.compareTo(saldo) > 0){
            throw new SaldoInsuficienteException("saque");
        }
        saldo = saldo.subtract(valor);
    }

    public void depositar(BigDecimal valor) {
        validaValorMaiorQueZero(valor,"deposito");
        saldo = saldo.add(valor);
    }

    public void transferir(BigDecimal valor, Conta contaDestino) {
        if(this.id.equals(contaDestino.getId())) {
            throw new TransferirParaMesmaContaException();
        }
        this.sacar(valor);
        contaDestino.depositar(valor);
    }

    protected static void validaValorMaiorQueZero(BigDecimal valor,String operacao) {
        if(valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValoresNegativosException(operacao);
        }
    }


}
