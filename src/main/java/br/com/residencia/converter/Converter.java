package br.com.residencia.converter;

public interface Converter<T, Z> {
	
	T convert(Z object);

}
