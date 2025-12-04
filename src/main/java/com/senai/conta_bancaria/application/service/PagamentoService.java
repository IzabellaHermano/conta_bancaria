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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository repository;
    private final ContaRepository contaRepository;
    private final CodigoAutenticacaoService codigoAutenticacaoService;
    private final IoTService iotService;
    private final TaxaRepository taxasRepository;

    private Pagamento buscarPagamentoPorBoleto(String boleto) {
        var pagamento = repository.findByBoleto(boleto).orElseThrow(
                () -> new EntidadeNaoEncontradaException("pagamento")
        );
        return pagamento;
    }

    @PreAuthorize("hasAnyRole('GERENTE')")
    public PagamentoResponseDTO verPagamento(String boleto){
        var pagamento = buscarPagamentoPorBoleto(boleto);
        return PagamentoResponseDTO.fromEntity(pagamento);
    }

    @PreAuthorize("hasRole('GERENTE')")
    public List<PagamentoResponseDTO> listarPagamentos(){
        return repository.findAll().stream()
                .map(PagamentoResponseDTO::fromEntity)
                .toList();
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public PagamentoResponseDTO realizarPagamento(PagamentoRequestDTO dto) {
        Conta conta = contaRepository.findByNumeroAndAtivaTrue(dto.contaNumero())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta não encontrada"));

        iotService.solicitarAutenticacao(conta.getCliente().getCpf());

        codigoAutenticacaoService.validarCodigo(dto.codigoAutenticacao(), conta.getCliente().getCpf());

        List<Taxa> taxasDoBanco = taxasRepository.findByTipoPagamento(dto.tipoPagamento());

        Pagamento pagamento = dto.toEntity(conta);
        PagamentoDomainService.validarPagamento(pagamento);
        PagamentoDomainService.calcularTaxa(pagamento, taxasDoBanco);
        PagamentoDomainService.definirStatus(pagamento, true); // se chegou até aqui, IoT validado

        Pagamento pagamentoSalvo = repository.save(pagamento);
        return PagamentoResponseDTO.fromEntity(pagamentoSalvo);
    }
}
