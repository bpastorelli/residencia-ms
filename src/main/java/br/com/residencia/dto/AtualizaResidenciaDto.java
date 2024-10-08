package br.com.residencia.dto;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AtualizaResidenciaDto {
	
	@Transient
	private Long   id;
	
	@NotNull(message = "Endereço não pode ser nulo.")
	@Length(min = 10, max = 200, message = "Endereço deve conter entre 10 e 200 caracteres.")
	private String endereco;
	
	@NotNull(message = "Número não pode ser nulo.")
	private Long   numero;
	
	@Length(min = 0, max = 50, message = "Campo Complemento deve conter no máximo 50 caracteres.")
	private String complemento;
	
	@NotNull(message = "Bairro não pode ser nulo.")
	private String bairro;
	
	@NotNull(message = "CEP não pode ser nulo.")
	@Length(min = 8, max = 8, message = "Campo CEP deve conter 8 caracteres sem traço.")
	private String cep;
	
	@NotNull(message = "Cidade não pode ser nulo.")
	private String cidade;
	
	@NotNull(message = "UF não pode ser nulo.")
	@Length(min = 2, max = 2, message = "Campo UF deve conter 2 caracteres.")
	private String uf;

	@Transient
	private String guide;
}
