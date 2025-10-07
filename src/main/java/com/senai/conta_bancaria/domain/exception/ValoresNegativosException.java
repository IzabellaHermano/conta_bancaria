package com.senai.conta_bancaria.domain.exception;

public class ValoresNegativosException extends RuntimeException {
    public ValoresNegativosException(String operacao) {
        super("Não é possivel realizar "+operacao+" com valores negativos");
    }
}
