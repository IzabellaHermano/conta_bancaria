package com.senai.conta_bancaria.domain.exception;
//tratamento do erro
public class EntidadeNaoEncontradaException extends RuntimeException {
    public EntidadeNaoEncontradaException(String entidade) {
        super(entidade+" não existe ou inativo(a)");
    }
}
