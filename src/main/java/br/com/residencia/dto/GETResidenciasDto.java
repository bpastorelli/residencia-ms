package br.com.residencia.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GETResidenciasDto {

	public List<GETResidenciaResponseDto> residencias;
	
	private PaginacaoDto paginacao;
	
}
