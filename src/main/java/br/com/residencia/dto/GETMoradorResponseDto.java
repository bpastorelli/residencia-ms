package br.com.residencia.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import br.com.residencia.enums.PerfilEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GETMoradorResponseDto implements Comparable<GETMoradorResponseDto> {
	
	private Long id;
	
	private String nome;
	
	private String email;
	
	private String cpf;
	
	private String rg;
	
	private String telefone;
	
	private String celular;
	
	private PerfilEnum perfil;
	
	@JsonUnwrapped
	private Long residenciaId;
	
	private Long associado;
	
	private Long posicao;
	
	private String guide;
	
	@Override
	public int compareTo(GETMoradorResponseDto o) {
		return this.nome.compareTo(o.nome);
	}

}
