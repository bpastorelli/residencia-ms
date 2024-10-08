package br.com.residencia.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "residencia")
public class Residencia implements Serializable {

	private static final long serialVersionUID = 3960436649365666213L;
	
	@Column(name = "guide", nullable = false)
	private String guide;
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long   id;
	
	@Column(name = "endereco", nullable = false)
	private String endereco;
	
	@Column(name = "numero", nullable = false)
	private Long   numero;
	
	@Column(name = "complemento", nullable = true)
	private String complemento;
	
	@Column(name = "bairro", nullable = false)
	private String bairro;
	
	@Column(name = "cep", nullable = false)
	private String cep;
	
	@Column(name = "cidade", nullable = false)
	private String cidade;
	
	@Column(name = "uf", nullable = false)
	private String uf;
	
	@Column(name = "data_criacao", nullable = false)
	private Date   dataCriacao;
	
	@Column(name = "data_atualizacao", nullable = false)
	private Date   dataAtualizacao;
	
	@OneToMany(mappedBy = "residencia", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Lancamento> lancamentos;
	
	@PreUpdate
    public void preUpdate() {
        dataAtualizacao = new Date();
    }
     
    @PrePersist
    public void prePersist() {
        final Date atual = new Date();
        dataCriacao = atual;
        dataAtualizacao = atual;
    }

}
