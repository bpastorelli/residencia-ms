package br.com.residencia.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryResidenciaResponseDto {

	private List<GETMoradorResponseDto> moradores;
	
}
