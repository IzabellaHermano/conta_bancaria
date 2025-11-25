package com.senai.conta_bancaria.application.dto;

import jakarta.validation.constraints.*;

public record GerenteAtualizadoDTO (
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
        @NotBlank(message = "Por favor, digite um email para o cliente")
        String email,

        @NotBlank
        @Size(min = 4, max = 8, message = "A sua senha deve ter no mínimo 4 digitos.")
        String senha
){
}
