package com.senai.conta_bancaria.domain.entity;

import lombok.Data;

@Data
public class ContaCorrente extends Conta {
    private int limite;
    private int taxa;
}
