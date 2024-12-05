package br.com.residencia.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import br.com.residencia.errorheadling.ErroRegistro;

public class Response<T> {

	private T data;
	
	@JsonUnwrapped
	private List<ErroRegistro> errors;

	public Response() {
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<ErroRegistro> getErrors() {
		if (this.errors == null) {
			this.errors = new ArrayList<ErroRegistro>();
		}
		return errors;
	}

	public void setErrors(List<ErroRegistro> errors) {
		this.errors = errors;
	}

}
