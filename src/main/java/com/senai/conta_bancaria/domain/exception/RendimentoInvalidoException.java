package com.senai.conta_bancaria.domain.exception;
//tratamento do erro
public class RendimentoInvalidoException extends RuntimeException {
    public RendimentoInvalidoException() {
        super("Rendimento deve ser aplicado somente em conta poupança!");
    }
}
