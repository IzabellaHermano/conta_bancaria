package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.ClienteAtualizadoDTO;
import com.senai.conta_bancaria.application.dto.ClienteRegistroDTO;
import com.senai.conta_bancaria.application.dto.ClienteResponseDTO;
import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.enums.Role;
import com.senai.conta_bancaria.domain.exception.ContaMesmoTipoException;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {


    private final ClienteRepository repository;
    private final PasswordEncoder encoder;

    @PreAuthorize( "hasRole('GERENTE')")
    public ClienteResponseDTO registrarCliente(ClienteRegistroDTO dto) {

        var cliente = repository.findByCpfAndAtivoTrue(dto.cpf()).orElseGet(
                () -> repository.save(dto.toEntity())
        );
        var contas = cliente.getContas();
        var novaConta = dto.contaDTO().toEntity(cliente);

        boolean jaTemTipo = contas.stream().anyMatch(
                c -> c.getClass().equals(novaConta.getClass()) && c.isAtiva()
        );
        if (jaTemTipo)
            throw new ContaMesmoTipoException();

        cliente.getContas().add(novaConta);

        cliente.setSenha(encoder.encode(dto.senha()));
        cliente.setRole(Role.CLIENTE);

        return ClienteResponseDTO.fromEntity(repository.save(cliente));
    }

    @PreAuthorize( "hasRole('GERENTE')")
    public List<ClienteResponseDTO> listarClientesAtivos() {
        return repository.findAllByAtivoTrue().stream()
                .map(ClienteResponseDTO::fromEntity)
                .toList();
    }

    @PreAuthorize( "hasRole('GERENTE')")
    public ClienteResponseDTO buscarClienteAtivoPorCpfTrue(String cpf){
        var cliente = buscarClientePorCpfAtivo(cpf);
        return ClienteResponseDTO.fromEntity(cliente);
    }

    @PreAuthorize( "hasRole('GERENTE')")
    public ClienteResponseDTO atualizarCliente(String cpf, ClienteAtualizadoDTO dto) {
        var cliente = buscarClientePorCpfAtivo(cpf);
        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setEmail(dto.email());
        cliente.setSenha(dto.senha());
        return ClienteResponseDTO.fromEntity(repository.save(cliente));
        // o metodo save observa o atributo id, e verifica se ja exite, se existe atualiza,se não salva
    }

    @PreAuthorize( "hasRole('GERENTE')")
    public void deletarCliente(String cpf) {
        var cliente = buscarClientePorCpfAtivo(cpf);
        cliente.setAtivo(false);
        cliente.getContas().forEach(
                c -> c.setAtiva(false)
        );
        repository.save(cliente);
    }
    private Cliente buscarClientePorCpfAtivo(String cpf) {
        var cliente = repository.findByCpfAndAtivoTrue(cpf).orElseThrow(
            ()-> new EntidadeNaoEncontradaException("cliente")
        );
        return cliente;
    }


}
