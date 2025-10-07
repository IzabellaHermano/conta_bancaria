package com.senai.conta_bancaria.domain.exception;

public class ContaMesmoTipoException extends RuntimeException {
    public ContaMesmoTipoException() {
        super("O cliente ja possui uma conta deste tipo");
    }
}
