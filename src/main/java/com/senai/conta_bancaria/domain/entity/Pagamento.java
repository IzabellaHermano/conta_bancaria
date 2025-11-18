package com.senai.conta_bancaria.domain.entity;

import com.senai.conta_bancaria.domain.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pagamento")
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id", nullable = false, foreignKey = @ForeignKey(name = "fk_pagamento_conta"))
    private Conta conta;

    @Column(nullable = false, length = 50)
    private String boleto;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorPago;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTotalDebitado;

    @Column(nullable = false)
    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPagamento status;

    @ManyToMany
    @JoinTable(
            name = "pagamento_taxa",
            joinColumns = @JoinColumn(name = "pagamento_id"),
            inverseJoinColumns = @JoinColumn(name = "taxa_id")
    )
    private Set<Taxa> taxas = new HashSet<>();
}
