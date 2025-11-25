package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.CodigoAutenticacao;

import java.time.LocalDateTime;

public record CodigoResponseDTO(
        String id,
        String codigo,
        LocalDateTime expiraEm,
        boolean validado,
        String clienteCpf
){
    public static CodigoResponseDTO fromEntity(CodigoAutenticacao entity){
        return new CodigoResponseDTO(
                entity.getId(),
                entity.getCodigo(),
                entity.getExpiraEm(),
                entity.isValidado(),
                entity.getCliente().getCpf()
        );
    }
}
