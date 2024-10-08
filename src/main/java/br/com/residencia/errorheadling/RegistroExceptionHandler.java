package br.com.residencia.errorheadling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class RegistroExceptionHandler {

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public ResponseEntity<ErroApiRegistro> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

		ex.printStackTrace();

		final List<ErroRegistro> errors = new ArrayList<ErroRegistro>();

		Throwable cause = ex.getCause();
		if (cause instanceof InvalidFormatException) {
			InvalidFormatException formatException = (InvalidFormatException) cause;
			List<Reference> path = formatException.getPath();
			for (Reference reference : path) {
				errors.add(new ErroRegistro("", "Campo invalido.", reference.getFieldName() + " invalido."));
			}

			final ErroApiRegistro erroApiRegistro = new ErroApiRegistro(errors);
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erroApiRegistro);

		} else if (cause instanceof JsonParseException) {

			JsonParseException parseException = (JsonParseException) cause;

			try {
				errors.add(new ErroRegistro("", "Campo invalido.", parseException.getProcessor().getCurrentName() + " invalido."));
			} catch (IOException e) {
				errors.add(new ErroRegistro("", "Erro no body.", "Erro no formato do body ou dos campos."));
			}

			final ErroApiRegistro erroApiRegistro = new ErroApiRegistro(errors);
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erroApiRegistro);

		} else if (cause instanceof JsonMappingException) {

			JsonMappingException mappingException = (JsonMappingException) cause;
			List<Reference> path = mappingException.getPath();
			for (Reference reference : path) {
				errors.add(new ErroRegistro("", "Campo invalido.", reference.getFieldName() + " invalido."));
			}
			if (errors.isEmpty()) {
				errors.add(new ErroRegistro("", "Erro no body.", "Erro no formato do body ou dos campos."));
			}

			final ErroApiRegistro erroApiRegistro = new ErroApiRegistro(errors);
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erroApiRegistro);

		} else {
			errors.add(new ErroRegistro("", "Erro no body.", "Erro no formato do body ou dos campos."));

			final ErroApiRegistro erroApiRegistro = new ErroApiRegistro(errors);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroApiRegistro);
		}
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public ResponseEntity<ErroApiRegistro> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

		ex.printStackTrace();

		final List<ErroRegistro> errors = new ArrayList<ErroRegistro>();

		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		for (FieldError fieldError : fieldErrors) {
			String fieldName = extrairFieldName(fieldError);
			errors.add(new ErroRegistro("", "Campo invalido.", fieldName + " - " + fieldError.getDefaultMessage()));
		}

		final ErroApiRegistro erroApiRegistro = new ErroApiRegistro(errors);
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erroApiRegistro);
	}

	private String extrairFieldName(FieldError fieldError) {
		String fieldName = fieldError.getField();
		if (fieldName != null && fieldName.contains(".")) {
			fieldName = fieldName.substring(fieldName.lastIndexOf(".") + 1, fieldName.length());
		}
		return fieldName;
	}

	@ExceptionHandler(RegistroException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public ResponseEntity<ErroApiRegistro> handleRegistroException(RegistroException exception) {

		final ErroApiRegistro erroApiRegistro = new ErroApiRegistro(exception.getErros());

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erroApiRegistro);
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public ResponseEntity<ErroApiRegistro> handleBindException(BindException ex) {

		ex.printStackTrace();

		final List<ErroRegistro> errors = new ArrayList<ErroRegistro>();

		List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
		for (ObjectError error : allErrors) {
			errors.add(new ErroRegistro("", "Erro de validaÃ§Ã£o.", error.getDefaultMessage()));
		}

		final ErroApiRegistro erroApiRegistro = new ErroApiRegistro(errors);
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erroApiRegistro);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErroApiRegistro> handleAllOtherExceptions(Exception exception) {

		exception.printStackTrace();

		final List<ErroRegistro> errors = new ArrayList<ErroRegistro>();
		final ErroApiRegistro erroApiRegistro = new ErroApiRegistro(errors);
		errors.add(new ErroRegistro("", "Erro interno.", "Erro nÃ£o mapeado."));

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erroApiRegistro);
	}

}
