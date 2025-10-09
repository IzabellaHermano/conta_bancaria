package com.senai.conta_bancaria.ui_interface.exception;

import com.senai.conta_bancaria.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //permite não expor a stack trace (descrição do erro)
    @ExceptionHandler(ContaMesmoTipoException.class)
    public ProblemDetail handleContaMesmoTipoException(ContaMesmoTipoException exception,
                                                       HttpServletRequest request){
        return ProblemDetailUtils.buildProblem(
                HttpStatus.CONFLICT,
                "O cliente ja possui uma conta deste tipo",
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ProblemDetail handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException exception,
                                                              HttpServletRequest request,
                                                              String entidade){
        return ProblemDetailUtils.buildProblem(
                HttpStatus.NOT_FOUND,
                entidade+" não existe ou inativo(a)",
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(RendimentoInvalidoException.class)
    public ProblemDetail handleRendimentoInvalidoException(RendimentoInvalidoException exception,
                                                           HttpServletRequest request){
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Rendimento deve ser aplicado somente em conta poupança!",
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ProblemDetail handleSaldoInsuficienteException(SaldoInsuficienteException exception,
                                                          HttpServletRequest request,
                                                          String operacao){
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Saldo insuficiente para realizar a operação de"+operacao,
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(TipoDeContaInvalidaException.class)
    public ProblemDetail handleTipoDeContaInvalidaException(TipoDeContaInvalidaException exception,
                                                            HttpServletRequest request,
                                                            String tipo){
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Tipo de conta "+tipo+" inválida. Os tipos válidos são: 'CORRENTE', 'POUPANCA'.",
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(TransferirParaMesmaContaException.class)
    public ProblemDetail handleTransferirParaMesmaContaException(TransferirParaMesmaContaException exception,
                                                                 HttpServletRequest request){

        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Não é possível transferir para a mesma conta",
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ValoresNegativosException.class)
    public ProblemDetail handleValoresNegativosException(ValoresNegativosException exception,
                                                         HttpServletRequest request,
                                                         String operacao){
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Não é possivel realizar "+operacao+" com valores negativos",
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)//qualquer outro erro sem ser os descritos acima
    public ResponseEntity <String> handleException(Exception exception){
        return new ResponseEntity <>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail badRequest(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                "Um ou mais campos são inválidos",
                request.getRequestURI()
        );

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        problem.setProperty("errors", errors);
        return problem;
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Tipo de parâmetro inválido");
        problem.setDetail(String.format(
                "O parâmetro '%s' deve ser do tipo '%s'. Valor recebido: '%s'",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido",
                ex.getValue()
        ));
        problem.setInstance(URI.create(request.getRequestURI()));
        return problem;
    }
    @ExceptionHandler(ConversionFailedException.class)
    public ProblemDetail handleConversionFailed(ConversionFailedException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Falha de conversão de parâmetro");
        problem.setDetail("Um parâmetro não pôde ser convertido para o tipo esperado.");
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("error", ex.getMessage());
        return problem;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Erro de validação nos parâmetros");
        problem.setDetail("Um ou mais parâmetros são inválidos");
        problem.setInstance(URI.create(request.getRequestURI()));

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String campo = violation.getPropertyPath().toString();
            String mensagem = violation.getMessage();
            errors.put(campo, mensagem);
        });
        problem.setProperty("errors", errors);
        return problem;
    }
}