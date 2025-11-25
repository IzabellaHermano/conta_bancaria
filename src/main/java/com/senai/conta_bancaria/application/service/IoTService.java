package com.senai.conta_bancaria.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class IoTService {

    @MqttPublisher("banco/autenticacao/{clienteCpf}")
    public void solicitarAutenticacao(String clienteCpf) {
        System.out.print("Solicitação de autenticação para cliente: " + clienteCpf);
    }
}
