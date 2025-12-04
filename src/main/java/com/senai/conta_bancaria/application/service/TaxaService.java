package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.TaxaRequestDTO;
import com.senai.conta_bancaria.application.dto.TaxaResponseDTO;
import com.senai.conta_bancaria.domain.entity.Taxa;
import com.senai.conta_bancaria.domain.repository.TaxaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class TaxaService {
    private final TaxaRepository repository;

    @PreAuthorize("hasRole('GERENTE')")
    @Transactional
    public TaxaResponseDTO criarTaxa(TaxaRequestDTO dto) {
        Taxa taxa = Taxa.builder()
                .descricao(dto.descricao())
                .percentual(dto.percentual())
                .valorFixo(dto.valorFixo())
                .tipoPagamento(dto.tipoPagamento())
                .build();

        return TaxaResponseDTO.fromEntity(repository.save(taxa));
    }

    @PreAuthorize("hasRole('GERENTE')")
    public List<TaxaResponseDTO> listarTaxas() {
        return repository.findAll().stream()
                .map(TaxaResponseDTO::fromEntity)
                .toList();
    }
}
