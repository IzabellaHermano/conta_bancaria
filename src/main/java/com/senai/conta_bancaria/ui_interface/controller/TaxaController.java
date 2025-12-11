package com.senai.conta_bancaria.ui_interface.controller;

import com.senai.conta_bancaria.application.dto.TaxaRequestDTO;
import com.senai.conta_bancaria.application.dto.TaxaResponseDTO;
import com.senai.conta_bancaria.application.service.TaxaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taxas")
@RequiredArgsConstructor
public class TaxaController {

    private final TaxaService service;

    @Operation(
            summary = "Cadastrar uma nova taxa",
            description = "Cadastra uma nova taxa no sistema vinculada a um tipo de pagamento específico. Requer permissão de GERENTE.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TaxaRequestDTO.class),
                            examples = @ExampleObject(name = "Exemplo de criação de taxa", value = """
                                        {
                                          "descricao": "Taxa Administrativa Boleto",
                                          "percentual": 0.00,
                                          "valorFixo": 2.50,
                                          "tipoPagamento": "BOLETO"
                                        }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Taxa cadastrada com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Tipo de Pagamento Nulo", value = "\"O tipo de pagamento é obrigatório\""),
                                            @ExampleObject(name = "Valor Negativo", value = "\"Taxa Inválida: Percentual ou Valor Fixo não podem ser negativos.\"")
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (Apenas Gerentes podem criar taxas).")
            }
    )
    @PostMapping
    public ResponseEntity<TaxaResponseDTO> criarTaxa(@RequestBody TaxaRequestDTO dto) {
        return ResponseEntity.ok(service.criarTaxa(dto));
    }

    @Operation(
            summary = "Listar todas as taxas",
            description = "Retorna todas as taxas cadastradas no banco de dados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de taxas retornada com sucesso.")
            }
    )
    @GetMapping
    public ResponseEntity<List<TaxaResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTaxas());
    }
}
