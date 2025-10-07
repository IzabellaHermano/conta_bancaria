package com.senai.conta_bancaria.ui_interface.exception;

import com.senai.conta_bancaria.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ContaMesmoTipoException.class)
    public ResponseEntity <String> handleContaMesmoTipoException(ContaMesmoTipoException exception){
        return new ResponseEntity <>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity <String> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException exception){
        return new ResponseEntity <>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RendimentoInvalidoException.class)
    public ResponseEntity <String> handleRendimentoInvalidoException(RendimentoInvalidoException exception){
        return new ResponseEntity <>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity <String> handleSaldoInsuficienteException(SaldoInsuficienteException exception){
        return new ResponseEntity <>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TipoDeContaInvalidaException.class)
    public ResponseEntity <String> handleTipoDeContaInvalidaException(TipoDeContaInvalidaException exception){
        return new ResponseEntity <>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransferirParaMesmaContaException.class)
    public ResponseEntity <String> handleTransferirParaMesmaContaException(TransferirParaMesmaContaException exception){
        return new ResponseEntity <>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValoresNegativosException.class)
    public ResponseEntity <String> handleValoresNegativosException(ValoresNegativosException exception){
        return new ResponseEntity <>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
