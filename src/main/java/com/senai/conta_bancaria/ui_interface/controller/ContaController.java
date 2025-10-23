package com.senai.conta_bancaria.ui_interface.controller;


import com.senai.conta_bancaria.application.dto.ContaAtualizacaoDTO;
import com.senai.conta_bancaria.application.dto.ContaResumoDTO;
import com.senai.conta_bancaria.application.dto.TransferenciaDTO;
import com.senai.conta_bancaria.application.dto.ValorSaqueDespositoDTO;
import com.senai.conta_bancaria.application.service.ContaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conta")//endpoint
@RequiredArgsConstructor//contrutor para final
public class ContaController {

    private final ContaService service;

    @PreAuthorize( "hasRole('GERENTE')")
    @GetMapping
    public ResponseEntity <List<ContaResumoDTO>> listarContasAtivas(){
        return ResponseEntity.ok(service.listarContasAtivas());
    }

    @PreAuthorize( "hasAnyRole('GERENTE','CLIENTE')")
    @GetMapping("/numero/{numero}")
    public ResponseEntity <ContaResumoDTO> buscarContaAtivaPorNumero(@PathVariable String numero){
        return ResponseEntity.ok(service.buscarContaPorNumero(numero));
    }

    @PreAuthorize( "hasRole('GERENTE')")
    @PutMapping("/{numero}")
    public ResponseEntity <ContaResumoDTO> atualizarConta(@PathVariable String numero,
                                                          @Valid @RequestBody ContaAtualizacaoDTO dto){
        return ResponseEntity.ok(service.atualizarConta(numero,dto));

    }

    @PreAuthorize( "hasRole('GERENTE')")
    @DeleteMapping("/{numero}")
    public ResponseEntity<Void> deletarConta(@PathVariable String numero){
        service.deletarConta(numero);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize( "hasRole('CLIENTE')")
    @PostMapping("/{numero}/sacar")
    public  ResponseEntity<ContaResumoDTO> sacar(@PathVariable String numero,
                                                 @Valid @RequestBody ValorSaqueDespositoDTO dto){
        return ResponseEntity.ok(service.sacar(numero,dto));
    }

    @PreAuthorize( "hasRole('CLIENTE')")
    @PostMapping("/{numero}/depositar")
    public ResponseEntity<ContaResumoDTO> depositar (@PathVariable String numero,
                                                     @Valid @RequestBody ValorSaqueDespositoDTO dto){
        return ResponseEntity.ok(service.depositar(numero,dto));
    }

    @PreAuthorize( "hasRole('CLIENTE')")
    @PutMapping("/{numero}/transferir")
    public ResponseEntity<ContaResumoDTO> transferir(@PathVariable String numero,
                                                     @Valid @RequestBody TransferenciaDTO dto){
        return ResponseEntity.ok(service.tranferir(numero,dto));
    }

    @PreAuthorize( "hasRole('GERENTE')")
    @PostMapping("/{numero}/rendimento")
    public ResponseEntity<ContaResumoDTO> aplicarRendimento(@PathVariable String numero){
        return ResponseEntity.ok(service.aplicarRendimento(numero));
    }
}
