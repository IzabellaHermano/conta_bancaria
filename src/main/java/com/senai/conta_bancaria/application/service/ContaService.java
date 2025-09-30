package com.senai.conta_bancaria.application.service;


import com.senai.conta_bancaria.application.dto.ContaAtualizacaoDTO;
import com.senai.conta_bancaria.application.dto.ContaResumoDTO;
import com.senai.conta_bancaria.application.dto.ValorSaqueDespositoDTO;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.ContaCorrente;
import com.senai.conta_bancaria.domain.entity.ContaPoupanca;
import com.senai.conta_bancaria.domain.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
//service chama o conjunto de acoes a serem realizadas
@Service
@RequiredArgsConstructor
@Transactional
public class ContaService {

    private final ContaRepository repository;

    @Transactional(readOnly = true)
    public List<ContaResumoDTO> listarContasAtivas(){
     return repository.findAllByAtivaTrue().stream()
             .map(ContaResumoDTO::fromEntity)
             .toList();
    }

    @Transactional(readOnly = true)
    public ContaResumoDTO buscarContaAtivaPorNumero(String numero) {
        return ContaResumoDTO.fromEntity(
                repository.findByNumeroAndAtivaTrue(numero)
                        .orElseThrow(() -> new RuntimeException("Conta não encontrada")
        ));
    }

    public ContaResumoDTO atualizarConta(String numero, ContaAtualizacaoDTO dto) {
        Conta conta = repository.findByNumeroAndAtivaTrue(numero)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if (conta instanceof ContaPoupanca poupanca){
            poupanca.setRendimento(dto.rendimento());

        }else if (conta instanceof  ContaCorrente corrente) {
            corrente.setLimite(dto.limite());
            corrente.setTaxa(dto.taxa());
        }else{
            throw new RuntimeException("Tipo de Conta Inválido");
        }

        conta.setSaldo(dto.saldo());
        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    public void deletarConta(String numero) {
        Conta conta = repository.findByNumeroAndAtivaTrue(numero)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        conta.setAtiva(false);
        repository.save(conta);
    }

    public ContaResumoDTO sacar(String numero, ValorSaqueDespositoDTO dto) {
        Conta conta = repository.findByNumeroAndAtivaTrue(numero)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        conta.sacar(dto.valor());
        return ContaResumoDTO.fromEntity(repository.save(conta));

    }
}
