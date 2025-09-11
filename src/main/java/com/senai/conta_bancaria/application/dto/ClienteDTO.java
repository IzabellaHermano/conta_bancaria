package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.Conta;

import java.util.List;


public record ClienteDTO(
    String id,
    String nome,
    String cpf,
    List <String> idConta
) {
    public static ClienteDTO fromEntity(Cliente cliente) {
        if (cliente == null) return null;

        List <String> idConta = cliente.getConta()
                .stream()
                .map(Conta::getId)
                .toList();

        return new ClienteDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                idConta
        );
    }

    public Cliente toEntity (List<Conta> c){
        Cliente cliente = new Cliente();
        cliente.setNome(this.nome);
        cliente.setCpf(this.cpf);
        cliente.setConta(c);
        return cliente;
    }
}
