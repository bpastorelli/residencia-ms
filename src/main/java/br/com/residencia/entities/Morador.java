package br.com.residencia.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.residencia.enums.PerfilEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MORADOR")
public class Morador implements Serializable {

	private static final long serialVersionUID = -5754246207015712518L;
	
	@Column(name = "GUIDE", nullable = true)
	private String guide;
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "NOME", nullable = false)
	private String nome;
	
	@Column(name = "EMAIL", nullable = false)
	private String email;
	
	@Column(name = "CPF", nullable = true)
	private String cpf;
	
	@Column(name = "RG", nullable = true)
	private String rg;
	
	@Column(name = "SENHA", nullable = false)
	private String senha;
	
	@Column(name = "TELEFONE", nullable = true)
	private String telefone;
	
	@Column(name = "CELULAR", nullable = true)
	private String celular;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "PERFIL", nullable = false)
	private PerfilEnum perfil;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CRIACAO", nullable = false)
	private Date dataCriacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_ATUALIZACAO", nullable = false)
	private Date dataAtualizacao;
	
	@Column(name = "ASSOCIADO", nullable = false)
	private Long associado;
	
	@Column(name = "POSICAO", nullable = false)
	private Long posicao;
	
	@OneToMany(mappedBy = "morador", fetch = FetchType.LAZY, cascade = CascadeType.ALL)	
	private List<VinculoResidencia> residencias;
	
	@OneToMany(mappedBy = "morador", fetch = FetchType.LAZY, cascade = CascadeType.ALL)	
	private List<Lancamento> lancamentos;
	
	@PreUpdate
    public void preUpdate() {
        dataAtualizacao = new Date();
    }
     
    @PrePersist
    public void prePersist() {
        final Date atual = new Date();
        final long status = 1;
        dataCriacao = atual;
        dataAtualizacao = atual;
        posicao = status;
    }

}
