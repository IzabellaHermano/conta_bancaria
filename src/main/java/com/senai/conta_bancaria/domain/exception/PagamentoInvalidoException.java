package com.senai.conta_bancaria.domain.exception;

public class PagamentoInvalidoException extends RuntimeException {
    public PagamentoInvalidoException(String motivo) {
        super("Pagamento inválido: " + motivo);
    }
}
