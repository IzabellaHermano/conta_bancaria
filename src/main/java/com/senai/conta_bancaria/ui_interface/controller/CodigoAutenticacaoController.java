package com.senai.conta_bancaria.ui_interface.controller;

import com.senai.conta_bancaria.application.dto.CodigoRequestDTO;
import com.senai.conta_bancaria.application.dto.CodigoResponseDTO;
import com.senai.conta_bancaria.application.service.CodigoAutenticacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autenticacao")
@RequiredArgsConstructor
public class CodigoAutenticacaoController {
    private final CodigoAutenticacaoService service;

    @Operation(
            summary = "Registrar Código (Simulação do Dispositivo IoT)",
            description = "Simula a resposta do dispositivo IoT, salva um código para o cliente usar para realizar uma transação.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CodigoRequestDTO.class),
                            examples = @ExampleObject(name = "Exemplo de Código IoT", value = """
                                    {
                                      "codigo": "789012",
                                      "expiraEm": "2030-12-31T23:59:59",
                                      "clienteCpf": "12345678901"
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Código registrado com sucesso (Status 'validado': false)."),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
            }
    )
    @PostMapping("/registrar")
    public ResponseEntity<CodigoResponseDTO> registrarCodigo(
            @Valid @RequestBody CodigoRequestDTO dto) {
        return ResponseEntity.ok(service.registrarCodigo(dto));
    }

    @Operation(
            summary = "Validar Código (Ação do Cliente)",
            description = "Valida o código para o usuário poder realizar determinada transação",
            parameters = {
                    @Parameter(name = "codigo", description = "Código de autenticação gerado pelo dispositivo", example = "789012", required = true),
                    @Parameter(name = "clienteCpf", description = "CPF do cliente", example = "12345678901", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Validação bem-sucedida (Status 'validado': true)."),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Autenticação Falhou",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Código Inválido", value = "Erro ao autenticar: Código não encontrado ou inválido."),
                                            @ExampleObject(name = "Código Expirado", value = "Erro ao autenticar: Autenticação falhou ou o código expirou.")
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
            }
    )
    @PostMapping("/validar")
    public ResponseEntity<CodigoResponseDTO> validarCodigo(
            @RequestParam String codigo,
            @RequestParam String clienteCpf) {
        return ResponseEntity.ok(service.validarCodigo(codigo, clienteCpf));
    }
}