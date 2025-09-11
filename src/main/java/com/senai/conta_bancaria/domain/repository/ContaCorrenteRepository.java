package com.senai.conta_bancaria.domain.repository;

import com.senai.conta_bancaria.domain.entity.ContaCorrente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaCorrenteRepository extends JpaRepository<ContaCorrente, String> {
}
