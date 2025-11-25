package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.CodigoRequestDTO;
import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.CodigoAutenticacao;
import com.senai.conta_bancaria.domain.exception.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import com.senai.conta_bancaria.domain.repository.CodigoAutenticacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class IoTSubscriber {

    private final CodigoAutenticacaoRepository codigoRepo;
    private final ClienteRepository clienteRepo;

    @MqttSubscriber("banco/validacao/{clienteCpf}")
    public void receberCodigo(  @MqttPayload CodigoRequestDTO dto) {
        Cliente cliente = clienteRepo.findByCpfAndAtivoTrue(dto.clienteCpf())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado"));

        CodigoAutenticacao codigo = CodigoAutenticacao.builder()
                .codigo(dto.codigo())
                .expiraEm(dto.expiraEm())
                .validado(false)
                .cliente(cliente)
                .build();

        codigoRepo.save(codigo);
    }
}
