package br.com.residencia.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GETVinculoMoradorResidenciaResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public GETMoradorResponseDto morador;
	
}
