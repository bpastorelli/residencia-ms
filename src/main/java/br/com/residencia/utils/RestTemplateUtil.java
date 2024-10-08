package br.com.residencia.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestTemplateUtil {

	private String URL;
	
    private Object params;
    
    private MediaType mediaType;
	
	private HttpMethod method;
	
	private RestTemplate restTemplate;
	
	public <T> Object execute(Class<T> clazz) {
		
		HttpHeaders headers = new HttpHeaders();
      	headers.setAccept(Arrays.asList(mediaType));
      	HttpEntity<Object> entity = new HttpEntity<>(headers);
      	
      	Map<String, String> paramsMap = null;
		paramsMap = getParams(params);
      	
      	String parametrizedArgs = paramsMap.keySet().stream().map(k ->
      		String.format("%s={%s}", k, k)
      	).collect(Collectors.joining("&"));
      	
      	Object response = restTemplate.exchange(
      			String.format(URL, parametrizedArgs),
      			method, 
      			entity, 
      			clazz,
      			paramsMap
      		  ).getBody();
      	
      	return response;
		
	}
	
	private Map<String, String> getParams(Object clazz) {
		
		Class<?> thisClass = null;
		Map<String, String> params = new HashMap<>();
		
		try {
			thisClass = Class.forName(clazz.getClass().getName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		Field[] aClassFields = thisClass.getFields();
		
		for(Field f : aClassFields){
		    Object fValue;
			try {
				String fName = f.getName();
				fValue = f.get(clazz);
				params.put(fName, fValue.toString());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return params;
	}

}
