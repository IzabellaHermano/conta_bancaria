package com.senai.conta_bancaria.application.service;


import com.senai.conta_bancaria.application.dto.ContaAtualizacaoDTO;
import com.senai.conta_bancaria.application.dto.ContaResumoDTO;
import com.senai.conta_bancaria.application.dto.TransferenciaDTO;
import com.senai.conta_bancaria.application.dto.ValorSaqueDespositoDTO;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.ContaCorrente;
import com.senai.conta_bancaria.domain.entity.ContaPoupanca;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.exception.RendimentoInvalidoException;
import com.senai.conta_bancaria.domain.exception.TipoDeContaInvalidaException;
import com.senai.conta_bancaria.domain.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
//service chama o conjunto de acoes a serem realizadas
@Service
@RequiredArgsConstructor
@Transactional
public class ContaService {

    private final ContaRepository repository;

    @PreAuthorize( "hasRole('GERENTE')")
    @Transactional(readOnly = true)
    public List<ContaResumoDTO> listarContasAtivas(){
     return repository.findAllByAtivaTrue().stream()
             .map(ContaResumoDTO::fromEntity)
             .toList();
    }

    @PreAuthorize( "hasAnyRole('GERENTE','CLIENTE')")
    @Transactional(readOnly = true)
    public ContaResumoDTO buscarContaPorNumero(String numero) {
        return ContaResumoDTO.fromEntity(
                buscarContaAtivaPorNumero(numero)
        );
    }

    @PreAuthorize( "hasRole('GERENTE')")
    public ContaResumoDTO atualizarConta(String numero, ContaAtualizacaoDTO dto) {
        Conta conta = buscarContaAtivaPorNumero(numero);

        if (conta instanceof ContaPoupanca poupanca){
            poupanca.setRendimento(dto.rendimento());

        }else if (conta instanceof  ContaCorrente corrente) {
            corrente.setLimite(dto.limite());
            corrente.setTaxa(dto.taxa());
        }else{
            throw new TipoDeContaInvalidaException("");
        }

        conta.setSaldo(dto.saldo());
        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    @PreAuthorize( "hasRole('GERENTE')")
    public void deletarConta(String numero) {
        Conta conta = buscarContaAtivaPorNumero(numero);
        conta.setAtiva(false);
        repository.save(conta);
    }

    @PreAuthorize( "hasRole('CLIENTE')")
    public ContaResumoDTO sacar(String numero, ValorSaqueDespositoDTO dto) {
        Conta conta = buscarContaAtivaPorNumero(numero);
        conta.sacar(dto.valor());
        return ContaResumoDTO.fromEntity(repository.save(conta));
    }
    public ContaResumoDTO depositar(String numero, ValorSaqueDespositoDTO dto) {
        Conta conta = buscarContaAtivaPorNumero(numero);
        conta.depositar(dto.valor());
        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    @PreAuthorize( "hasRole('CLIENTE')")
    public ContaResumoDTO tranferir(String numero, TransferenciaDTO dto) {
        Conta contaOrigem = buscarContaAtivaPorNumero(numero);
        Conta contaDestino = buscarContaAtivaPorNumero(dto.contaDestino());

        contaOrigem.transferir(dto.valor(), contaDestino);

        repository.save(contaDestino);
        return ContaResumoDTO.fromEntity(repository.save(contaOrigem));

    }

    private Conta buscarContaAtivaPorNumero(String numero){
        return repository.findByNumeroAndAtivaTrue(numero)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("conta")
                );
    }

    @PreAuthorize( "hasRole('GERENTE')")
    public ContaResumoDTO aplicarRendimento(String numero) {
        Conta conta = buscarContaAtivaPorNumero(numero);
        if (conta instanceof ContaPoupanca poupanca){
            poupanca.aplicarRendimento();
            return ContaResumoDTO.fromEntity(repository.save(poupanca));
        }
        throw new RendimentoInvalidoException();
    }
}
