package br.com.residencia.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GETMoradoresResponseDto {

	private List<GETMoradorSemResidenciasResponseDto> moradores;
	
}
