package br.com.residencia.senders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.residencia.dto.MoradorRequestDto;
import br.com.residencia.dto.QueryResidenciaResponseDto;
import br.com.residencia.utils.RestTemplateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MoradorSender {
	
	@Value("${sgc-backend.morador.url}")
	public String URL;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public QueryResidenciaResponseDto buscarMoradores(MoradorRequestDto request) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException{
		
		log.info("Consultando moradores no endpoint: {}", URL);
		
		RestTemplateUtil rest = RestTemplateUtil.builder()
				.URL(URL)
				.mediaType(MediaType.APPLICATION_JSON)
				.method(HttpMethod.GET)
				.restTemplate(restTemplate)
				.params(request)
				.build();
		
		return (QueryResidenciaResponseDto) rest.execute(QueryResidenciaResponseDto.class);
		
	}

}
