package com.senai.conta_bancaria.application.dto;


import com.senai.conta_bancaria.domain.entity.Cliente;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

public record ClienteResponseDTO(

        String id,

        @NotNull(message = "Nome não pode ser null")
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "O tamanho deve ser entre 2 e 100")
        //@Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$", message = "O nome deve conter apenas letras")
        String nome,

        @NotNull(message = "CPF não pode ser null")
        @NotBlank(message = "CPF é obrigatório")
        //@CPF(message = "CPF inválido.")
        //@Pattern(regexp = "^\\\\d{3}\\\\.\\\\d{3}\\\\.\\\\d{3}\\\\-\\\\d{2}$",message = "O CPF deve estar no formato 000.000.000-00.")
        String cpf,

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 4, max = 8, message = "A sua senha deve ter no mínimo 4 digitos.")
        String senha,

        @NotEmpty(message = "O cliente deve ter pelo menos uma conta cadastrada.")
        @Valid
        List<ContaResumoDTO> contas
) {
    public static ClienteResponseDTO fromEntity(Cliente cliente) {
        List<ContaResumoDTO> contas = cliente.getContas().stream().map(ContaResumoDTO::fromEntity).toList();
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getSenha(),
                contas
        );
    }
}