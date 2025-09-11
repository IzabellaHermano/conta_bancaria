package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.ClienteDTO;
import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import com.senai.conta_bancaria.domain.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContaRepository contaRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List <ClienteDTO> listarClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteDTO buscarClientePorId(String id) {
        return clienteRepository.findById(id)
                .map(ClienteDTO::fromEntity)
                .orElse(null);
    }
    public ClienteDTO salvarCliente (ClienteDTO dto){
        Conta conta = contaRepository.findById(dto.id()).orElse(null);
        assert conta != null;
        Cliente entidade = dto.toEntity(List.of(conta));
        Cliente salvo = clienteRepository.save(entidade);
        return ClienteDTO.fromEntity(salvo);
    }

    public void deletarCliente(String id){
        clienteRepository.deleteById(id);
    }
}
