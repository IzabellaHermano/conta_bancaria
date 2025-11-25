package com.senai.conta_bancaria.application.dto;

import java.time.LocalDateTime;

public record CodigoRequestDTO (
        String codigo,
        LocalDateTime expiraEm,
        String clienteCpf
){}
