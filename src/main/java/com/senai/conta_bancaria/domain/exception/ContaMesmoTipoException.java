package com.senai.conta_bancaria.domain.exception;
//tratamento do erro
public class ContaMesmoTipoException extends RuntimeException {
    public ContaMesmoTipoException() {
        super("O cliente ja possui uma conta deste tipo");
    }
}
