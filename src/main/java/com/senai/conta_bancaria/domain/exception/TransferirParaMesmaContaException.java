package com.senai.conta_bancaria.domain.exception;
//tratamento do erro
public class TransferirParaMesmaContaException extends RuntimeException {
    public TransferirParaMesmaContaException() {
        super("Não é possível transferir para a mesma conta");
    }
}
