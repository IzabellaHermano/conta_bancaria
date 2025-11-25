package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.enums.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.ArrayList;

public record ClienteRegistroDTO(

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

        @NotNull(message = "A conta é obrigatória.")
        @Valid
        ContaResumoDTO contaDTO
) {
    public Cliente toEntity() {
        return Cliente.builder()
                .ativo(true)
                .nome(this.nome)
                .cpf(this.cpf)
                .email(this.email)
                .senha(this.senha)
                .contas(new ArrayList<Conta>())
                .role(Role.CLIENTE)
                .build();
    }
}
