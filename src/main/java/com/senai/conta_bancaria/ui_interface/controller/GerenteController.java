package com.senai.conta_bancaria.ui_interface.controller;

import com.senai.conta_bancaria.application.dto.*;
import com.senai.conta_bancaria.application.service.GerenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/gerente")
@RequiredArgsConstructor
public class GerenteController {

    private final GerenteService service;

    @Operation(
            summary = "Registrar Gerente",
            description = "Cria e registra um gerente",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = GerenteRegistroDTO.class),
                            examples = @ExampleObject(name = "Exemplo válido", value = """
                                            {
                                              "nome": "Jonas",
                                              "cpf": "12345678910",
                                              "email":"jonas@email.com",
                                              "senha": "1234",
                                            }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Gerente cadastrado com sucesso!"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Senha curta demais", value = "A sua senha deve ter no mínimo 4 digitos."),
                                            @ExampleObject(name = "CPF inválido", value = "O CPF deve contar exatamente 11 dígitos numéricos"),
                                    }
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity <GerenteResponseDTO> registrarGerente(@Valid @RequestBody GerenteRegistroDTO dto){
        GerenteResponseDTO gerenteNovo = service.registrarGerente(dto);

        return ResponseEntity.created(URI.create("/api/gerente/cpf"+
                gerenteNovo.cpf())).body(gerenteNovo);
    }

    @Operation(
            summary = "Listar gerentes por CPF",
            description = "Retorna gerentes ativos pelo CPF requesitado.",
            parameters = {
                    @Parameter(name = "CPF", description = "CPF do Gerente que deseja buscar: 123.456.789-10")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Gerente encontrado com sucesso"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Gerente não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Gerente com CPF: 123456789   10 não encontrado.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('ADMIN')")
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<GerenteResponseDTO> buscarGerenteAtivoPorCpfTrue(@PathVariable String cpf){
        return ResponseEntity.ok(service.buscarGerenteAtivoPorCpfTrue(cpf));
    }

    @Operation(
            summary = "Atualizar Gerente",
            description = "Atualiza e salva as informações de um gerente",
            parameters = {
                    @Parameter(name = "CPF", description = "CPF do Gerente que deseja buscar: 123.456.789-10")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = GerenteRegistroDTO.class),
                            examples = @ExampleObject(name = "Exemplo válido", value = """
                                        {
                                          "nome": "Henrique",
                                          "cpf": "12378945611",
                                          "email":"henrique@email.com",
                                          "senha": "1234"
                                         }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Gerente atualizado com sucesso!"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Senha curta demais", value = "A sua senha deve ter no mínimo 4 digitos."),
                                            @ExampleObject(name = "CPF inválido", value = "O CPF deve contar exatamente 11 dígitos numéricos")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Gerente não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Gerente com CPF: 12345678910 não encontrado.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('ADMIN')")
    @PutMapping("/cpf/{cpf}")
    public ResponseEntity <GerenteResponseDTO> atualizarGerente(@PathVariable String cpf,
                                                                @Valid @RequestBody GerenteAtualizadoDTO dto){
        return ResponseEntity.ok(service.atualizarGerente(cpf,dto));
    }

    @Operation(
            summary = "Deletar um Gerente",
            description = "Desativa o registro de um Gerente do sistema",
            parameters = {
                    @Parameter(name = "CPF", description = "CPF do Gerente que deseja buscar: 12345678910")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Gerente deletado com sucesso."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Gerente não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "Gerente com CPF: 12345678910 não encontrado.")
                            )
                    )
            }
    )
    @PreAuthorize( "hasRole('ADMIN')")
    @DeleteMapping("/{cpf}")
    public ResponseEntity <Void> deletarGerente(@PathVariable String cpf){
        service.deletarGerente(cpf);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar Gerentes",
            description = "Lista todos os gerentes no banco de dados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista com gerentes retornada com sucesso.")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<GerenteResponseDTO>> listarGerentesAtivos(){
        return ResponseEntity.ok(service.listarGerentesAtivos());
    }

}
