package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.ClienteResponseDTO;
import com.senai.conta_bancaria.application.dto.GerenteAtualizadoDTO;
import com.senai.conta_bancaria.application.dto.GerenteRegistroDTO;
import com.senai.conta_bancaria.application.dto.GerenteResponseDTO;
import com.senai.conta_bancaria.domain.entity.Gerente;
import com.senai.conta_bancaria.domain.enums.Role;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.GerenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GerenteService {

    private  final GerenteRepository repository;

    private final PasswordEncoder encoder;

    @PreAuthorize( "hasRole('ADMIN')")
    public GerenteResponseDTO registrarGerente(GerenteRegistroDTO dto){

        var gerente = repository.findByCpfAndAtivoTrue(dto.cpf()).orElseGet(
                () -> repository.save(dto.toEntity())
        );

        gerente.setSenha(encoder.encode(dto.senha()));
        gerente.setRole(Role.GERENTE);

        return GerenteResponseDTO.fromEntity(repository.save(gerente));
    }

    @PreAuthorize( "hasRole('ADMIN')")
    public List <GerenteResponseDTO> listarGerentesAtivos(){
        return repository.findAllByAtivoTrue().stream()
                .map(GerenteResponseDTO::fromEntity)
                .toList();
    }

    @PreAuthorize( "hasRole('ADMIN')")
    public GerenteResponseDTO buscarGerenteAtivoPorCpfTrue(String cpf){
        var gerente = buscarGerentePorCpfTrue(cpf);
        return GerenteResponseDTO.fromEntity(gerente);
    }

    @PreAuthorize( "hasRole('ADMIN')")
    public GerenteResponseDTO atualizarGerente(String cpf, GerenteAtualizadoDTO dto){
        var gerente = buscarGerentePorCpfTrue(cpf);
        gerente.setNome(dto.nome());
        gerente.setCpf(dto.cpf());
        gerente.setEmail(dto.email());
        gerente.setSenha(dto.senha());
        return GerenteResponseDTO.fromEntity(repository.save(gerente));
    }

    @PreAuthorize( "hasRole('ADMIN')")
    public void deletarGerente(String cpf){
        var gerente = buscarGerentePorCpfTrue(cpf);
        gerente.setAtivo(false);
        repository.save(gerente);
    }

    private Gerente buscarGerentePorCpfTrue(String cpf) {
        var gerente = repository.findByCpfAndAtivoTrue(cpf).orElseThrow(
                () -> new EntidadeNaoEncontradaException("gerente")
        );
        return gerente;
    }
}
