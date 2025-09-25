package com.senai.conta_bancaria.ui_interface;

import com.senai.conta_bancaria.application.dto.ContaResumoDTO;
import com.senai.conta_bancaria.application.service.ContaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conta")//endpoint
@RequiredArgsConstructor//contrutor para final
public class ContaController {

    private final ContaService service;

    @GetMapping
    public ResponseEntity <List<ContaResumoDTO>> listarContasAtivas(){
        return ResponseEntity.ok(service.listarContasAtivas());
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity <ContaResumoDTO> buscarContaAtivaPorNumero(@PathVariable String numero){
        return ResponseEntity.ok(service.buscarContaAtivaPorNumero(numero));
    }
}
