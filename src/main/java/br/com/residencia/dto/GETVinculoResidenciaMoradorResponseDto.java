package br.com.residencia.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GETVinculoResidenciaMoradorResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public GETResidenciaResponseDto residencia;
	
}
