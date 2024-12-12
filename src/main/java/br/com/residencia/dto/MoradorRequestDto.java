package br.com.residencia.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MoradorRequestDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public Long id;
	
	public List<String> ids;
	
	public String nome;
	
	public String cpf;
	
	public String rg;
	
	public String email;
	
	public Long posicao;
	
	public String guide;
	
	public Long residenciaId;
	
	public boolean content;

}
