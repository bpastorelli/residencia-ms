package br.com.residencia.errorheadling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErroRegistro {
	
	private String codigo;
	
	private String titulo;
	
	private String detalhe;

}
