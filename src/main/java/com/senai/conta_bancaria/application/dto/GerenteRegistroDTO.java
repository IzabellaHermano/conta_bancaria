package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Gerente;
import com.senai.conta_bancaria.domain.enums.Role;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

public record GerenteRegistroDTO(

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
        String senha
) {
    public Gerente toEntity() {
        return Gerente.builder()
                .ativo(true)
                .nome(this.nome)
                .cpf(this.cpf)
                .email(this.email)
                .senha(this.senha)
                .role(Role.GERENTE)
                .build();
    }
}
