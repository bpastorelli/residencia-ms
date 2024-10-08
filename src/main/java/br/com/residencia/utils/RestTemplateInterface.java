package br.com.residencia.utils;

public interface RestTemplateInterface<T, O> {
	
	public T restTemplate(O clazz);

}
