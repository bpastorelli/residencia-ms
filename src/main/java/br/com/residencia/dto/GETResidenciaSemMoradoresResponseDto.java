package br.com.residencia.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GETResidenciaSemMoradoresResponseDto {
	
	private Long   id;
	
	private String endereco;
	
	private Long   numero;
	
	private String complemento;
	
	private String bairro;
	
	private String cep;
	
	private String cidade;
	
	private String uf;
	
	private String dataVinculo;

	private String guide;
}
