package com.senai.conta_bancaria.ui_interface.controller;

import com.senai.conta_bancaria.application.dto.PagamentoRequestDTO;
import com.senai.conta_bancaria.application.dto.PagamentoResponseDTO;
import com.senai.conta_bancaria.application.service.PagamentoAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/pagamento")
@RequiredArgsConstructor
public class PagamentoController {
    private final PagamentoAppService service;

    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> processarPagamento(
            @Valid @RequestBody PagamentoRequestDTO dto,
            @RequestParam boolean respostaValidaMQTT) {
        PagamentoResponseDTO createdPagamento = service.processarPagamento(dto, respostaValidaMQTT);

        return ResponseEntity.created(URI.create("/api/pagamento/" +
                createdPagamento.boleto())).body(createdPagamento);
    }

    @PostMapping("/cliente")
    public ResponseEntity<PagamentoResponseDTO> realizarPagamento(
            @Valid @RequestBody PagamentoRequestDTO dto) {

        PagamentoResponseDTO pagamento = service.realizarPagamento(dto);

        return ResponseEntity.created(
                URI.create("/api/pagamento/" + pagamento.boleto())
        ).body(pagamento);
    }
}
