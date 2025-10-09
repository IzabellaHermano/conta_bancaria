package com.senai.conta_bancaria.domain.exception;
//tratamento do erro
public class TipoDeContaInvalidaException extends RuntimeException {
    public TipoDeContaInvalidaException(String tipo) {
        super("Tipo de conta "+tipo+" inválida. Os tipos válidos são: 'CORRENTE', 'POUPANCA'.");
    }
}
