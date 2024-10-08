package br.com.residencia.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "VINCULO_RESIDENCIA")
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VinculoResidencia implements Serializable {
	
	private static final long serialVersionUID = 3960436649365666214L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "data_vinculo", nullable = false)
	private Date dataVinculo;
	
    @ManyToOne
    @JoinColumn(name = "morador_id")
	private Morador morador;
    
	@ManyToOne
	@JoinColumn(name="residencia_id")
	private Residencia residencia;
	
	@Column(name = "guide", nullable = true)
	private String guide;
	
	@PreUpdate
    public void preUpdate() {
		dataVinculo = new Date();
    }
     
    @PrePersist
    public void prePersist() {
        final Date atual = new Date();
        dataVinculo = atual;
    }

}
