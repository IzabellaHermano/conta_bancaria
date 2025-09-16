package com.senai.conta_bancaria.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.util.List;
@Entity//CRIA TABELA NO BANCO DE DADOS
@Data//GETTERS E SETTERS
@AllArgsConstructor //CONSTRUTOR COM TODOS OS PARAMETROS
@NoArgsConstructor//CONSTRUTOR SEM PARAMETROS
@Builder//ACESSO AO BILDER E PERMITE CONTRUIR OBJETO CONFORME O NECESSIDADE (FACILITA A CRIAÇÃO DE UM OBJETO)
@Table(name = "cliente",//PERMITE MODELAR MELHOR A TABELA DO BANCO
        /*REGRAS NA HORA DE POPULAÇÃO DO BANCO (NÃO PERMITE TER DOIS VALORES IGUAIS DE CPF)*/
        uniqueConstraints ={
            @UniqueConstraint(columnNames = {"cpf"})
        }
)
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 11)
    private String cpf;

    //TIPO DE RELACIONAMENTO (1:N)
    @OneToMany(mappedBy = "cliente",cascade = CascadeType.ALL)//mappedBy - MAPEA O ATRIBUTO, cascade - PERMITE INTERLIGAR TOTALMENTE
    private List<Conta> contas;

    @Column(nullable = false)
    private Boolean ativo;

}

/*BUILDER
* O padrão Builder no Java é um padrão de projeto criacional que permite construir objetos complexos passo a passo,
* separando o processo de construção da representação do objeto.
* Ele é útil quando um objeto tem muitos parâmetros,
* para produzir objetos com diferentes configurações ou para criar objetos imutáveis.*/
