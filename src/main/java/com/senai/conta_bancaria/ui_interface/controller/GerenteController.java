package com.senai.conta_bancaria.ui_interface.controller;

import com.senai.conta_bancaria.application.dto.ClienteResponseDTO;
import com.senai.conta_bancaria.application.dto.GerenteAtualizadoDTO;
import com.senai.conta_bancaria.application.dto.GerenteRegistroDTO;
import com.senai.conta_bancaria.application.dto.GerenteResponseDTO;
import com.senai.conta_bancaria.application.service.GerenteService;
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

    @PostMapping
    public ResponseEntity <GerenteResponseDTO> registrarGerente(@Valid @RequestBody GerenteRegistroDTO dto){
        GerenteResponseDTO gerenteNovo = service.registrarGerente(dto);

        return ResponseEntity.created(URI.create("/api/gerente/cpf"+
                gerenteNovo.cpf())).body(gerenteNovo);
    }
    @PreAuthorize( "hasRole('ADMIN')")
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<GerenteResponseDTO> buscarGerenteAtivoPorCpfTrue(@PathVariable String cpf){
        return ResponseEntity.ok(service.buscarGerenteAtivoPorCpfTrue(cpf));
    }
    @PreAuthorize( "hasRole('ADMIN')")
    @PutMapping("/cpf/{cpf}")
    public ResponseEntity <GerenteResponseDTO> atualizarGerente(@PathVariable String cpf,
                                                                @Valid @RequestBody GerenteAtualizadoDTO dto){
        return ResponseEntity.ok(service.atualizarGerente(cpf,dto));
    }
    @PreAuthorize( "hasRole('ADMIN')")
    @DeleteMapping("/{cpf}")
    public ResponseEntity <Void> deletarGerente(@PathVariable String cpf){
        service.deletarGerente(cpf);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<GerenteResponseDTO>> listarClientesAtivos(){
        return ResponseEntity.ok(service.listarGerentesAtivos());
    }

}
