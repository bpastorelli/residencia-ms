package br.com.residencia.security.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlterarSenhaDto {

	@NotNull(message = "O campo senha é obrigatório!")
	private String senha;
	
	@NotNull(message = "O campo nova senha é obrigatório!")
	private String novaSenha;
	
	@NotNull(message = "O campo confirmação de senha é obrigatório!")
	private String confirmarSenha;

}
