package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.PagamentoRequestDTO;
import com.senai.conta_bancaria.application.dto.PagamentoResponseDTO;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.entity.Taxa;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.ContaRepository;
import com.senai.conta_bancaria.domain.repository.PagamentoRepository;
import com.senai.conta_bancaria.domain.repository.TaxaRepository;
import com.senai.conta_bancaria.domain.service.PagamentoDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Adiciona a anotação para que o Spring gerencie esta classe
@RequiredArgsConstructor // Adiciona construtor para injetar as dependências 'final'
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final ContaRepository contaRepository;
    private final TaxaRepository taxaRepository;
    private final PagamentoDomainService domainService;

    // A autorização verifica se a conta pertence ao cliente autenticado (lógica de segurança mais robusta),
    // mas o requisito básico é permitir CLIENTE e GERENTE.
    @PreAuthorize("hasAnyRole('CLIENTE', 'GERENTE')")
    public PagamentoResponseDTO realizarPagamento(String numeroConta, PagamentoRequestDTO dto) {
        // 1. Buscar Conta de Origem
        Conta conta = contaRepository.findByNumeroAndAtivaTrue(numeroConta)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta ativa com número " + numeroConta));

        // 2. Buscar Taxas (simulação de taxas padrão aplicáveis a todo pagamento)
        List<Taxa> taxasAplicaveis = taxaRepository.findAll();

        // 3. Processar Pagamento (executa a lógica de negócio do Domínio)
        // Lança exceções como SaldoInsuficienteException e PagamentoInvalidoException
        Pagamento pagamento = domainService.processarPagamento(
                conta,
                dto.boleto(),
                dto.valor(),
                taxasAplicaveis
        );

        // 4. Persistir a Conta (com o saldo atualizado) e o Pagamento (com o status e valor total)
        contaRepository.save(conta);
        Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);

        return PagamentoResponseDTO.fromEntity(pagamentoSalvo);
    }
}