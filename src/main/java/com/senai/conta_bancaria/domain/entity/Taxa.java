package com.senai.conta_bancaria.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "taxa",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"})
        }
)
public class Taxa {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    protected String descricao;

    @Column(precision = 19,scale = 2)
    private BigDecimal percentual;

    @Column(nullable = false, precision = 19,scale = 2)
    private BigDecimal valorFixo;
}
