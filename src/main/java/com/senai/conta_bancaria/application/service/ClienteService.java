package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.ClienteAtualizadoDTO;
import com.senai.conta_bancaria.application.dto.ClienteRegistroDTO;
import com.senai.conta_bancaria.application.dto.ClienteResponseDTO;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {


    private final ClienteRepository repository;

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
            throw new RuntimeException("Cliente já possui uma conta desse tipo.");

        cliente.getContas().add(novaConta);

        return ClienteResponseDTO.fromEntity(repository.save(cliente));
    }

    public List<ClienteResponseDTO> listarClientesAtivos() {
        return repository.findAllByAtivoTrue().stream()
                .map(ClienteResponseDTO::fromEntity)
                .toList();
    }
    public ClienteResponseDTO buscarClienteAtivoPorCpf(String cpf){
        var cliente = repository.findByCpfAndAtivoTrue(cpf).orElseThrow(
                ()-> new RuntimeException("Cliente não encontrado")
        );
        return ClienteResponseDTO.fromEntity(cliente);
    }

    public ClienteResponseDTO atualizarCliente(String cpf, ClienteAtualizadoDTO dto) {
        var cliente = repository.findByCpfAndAtivoTrue(cpf).orElseThrow(
                () ->  new RuntimeException("Cliente não encontrado")
        );
        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        return ClienteResponseDTO.fromEntity(repository.save(cliente));
        // o metodo save observa o atributo id, e verifica se ja exite, se existe atualiza,se não salva
    }

    public void deletarCliente(String cpf) {
        var cliente = repository.findByCpfAndAtivoTrue(cpf).orElseThrow(
                () ->  new RuntimeException("Cliente não encontrado")

        );
        cliente.setAtivo(false);
        cliente.getContas().forEach(
                c -> c.setAtiva(false)
        );
        repository.save(cliente);
    }
}
