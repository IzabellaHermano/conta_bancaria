package com.senai.conta_bancaria.domain.exception;
//tratamento do erro
public class ValoresNegativosException extends RuntimeException {
    public ValoresNegativosException(String operacao) {
        super("Não é possivel realizar "+operacao+" com valores negativos");
    }
}
