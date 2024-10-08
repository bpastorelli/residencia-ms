package br.com.residencia.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name = "lancamento")
public class Lancamento implements Serializable {
	
	private static final long serialVersionUID = 6524560251526772839L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Morador morador;
	
	@Column(name = "data_pagamento", nullable = false)
	private LocalDateTime dataPagamento;
	
	@Column(name = "mes_referencia", nullable = false)
	private String periodo;
	
	@Column(name = "documento", nullable = false)
	private String documento;
	
	@Column(name = "valor", nullable = false)
	private BigDecimal valor;
	
	@Column(name = "data_criacao", nullable = false)
	private LocalDate dataCriacao;
	
	@Column(name = "data_atualizacao", nullable = false)
	private LocalDate dataAtualizacao;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Residencia residencia;
	
	@Transient
	private String requisicaoId;
	
	@PreUpdate
    public void preUpdate() {
        dataAtualizacao = LocalDate.now();
    }
	
    @PrePersist
    public void prePersist() {
        final LocalDate atual = LocalDate.now();
        dataCriacao = atual;
        dataAtualizacao = atual;
    }

}