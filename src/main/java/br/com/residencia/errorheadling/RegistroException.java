package br.com.residencia.errorheadling;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroException extends Exception {

	private static final long serialVersionUID = 80329302388291L;
	
	private List<ErroRegistro> erros = new ArrayList<>();
	
	ResponseEntity<?> responseEntity;
	
	public RegistroException(Exception e) {
		super(e);
	}
	
	public RegistroException(ResponseEntity<?> responseEntity ) {
		
		super("Erro: " + responseEntity.getStatusCodeValue() + " (" + responseEntity.getStatusCode().getReasonPhrase() + ")");
		this.responseEntity = responseEntity;
	}

}
