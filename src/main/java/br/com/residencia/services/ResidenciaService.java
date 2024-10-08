package br.com.residencia.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.residencia.amqp.producer.impl.ResidenciaProducer;
import br.com.residencia.dto.AtualizaResidenciaDto;
import br.com.residencia.dto.CabecalhoResponsePublisherDto;
import br.com.residencia.dto.GETResidenciaResponseDto;
import br.com.residencia.dto.MoradorRequestDto;
import br.com.residencia.dto.QueryResidenciaResponseDto;
import br.com.residencia.dto.ResidenciaDto;
import br.com.residencia.dto.ResponsePublisherDto;
import br.com.residencia.entities.Residencia;
import br.com.residencia.errorheadling.RegistroException;
import br.com.residencia.filter.ResidenciaFiltro;
import br.com.residencia.mappers.ResidenciaMapper;
import br.com.residencia.repositories.ResidenciaRepository;
import br.com.residencia.repositories.query.QueryRepository;
import br.com.residencia.response.Response;
import br.com.residencia.senders.MoradorSender;
import br.com.residencia.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResidenciaService {

	@Value("${guide.limit}")
	private int guideLimit;
	
	@Autowired
	private QueryRepository<Residencia, ResidenciaFiltro> queryRepository;
	
	@Autowired
	private ResidenciaProducer producer;
	
	@Autowired
	private Validators<ResidenciaDto, AtualizaResidenciaDto> validator;
	
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	@Autowired
	private ResidenciaMapper residenciaMapper;
	
	@Autowired
	private MoradorSender moradorSender;
	
	public ResponsePublisherDto salvar(ResidenciaDto residenciaRequestBody) throws RegistroException {
		
		log.info("Cadastrando um morador: {}", residenciaRequestBody.toString());
		
		residenciaRequestBody.setGuide(this.gerarGuide());
		
		this.validator.validarPost(residenciaRequestBody);
		
		//Preenche o id da residencia se ela já existir, para fazer apenas vinculo do morador.
		residenciaRepository.findByCepAndNumeroAndComplemento(residenciaRequestBody.getCep(), residenciaRequestBody.getNumero(), residenciaRequestBody.getComplemento())
			.ifPresent(r -> {
				residenciaRequestBody.setId(r.getId());
				residenciaRequestBody.setGuide(r.getGuide());
			});
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  residenciaRequestBody.toString() + " para o consumer.");
		
		this.producer.producerAsync(residenciaRequestBody);
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(residenciaRequestBody.getGuide())
						.build())
				.build();
		
		return response;
		
	}
	
	public ResponsePublisherDto atualizar(AtualizaResidenciaDto residenciaRequestBody, Long id) throws RegistroException {

		log.info("Atualizando uma residencia: {}", residenciaRequestBody.toString());
		
		residenciaRequestBody.setGuide(this.gerarGuide()); 	
		
		this.validator.validarPut(residenciaRequestBody, id);
		
		//Prepara os dados para enviar para a fila.
		Residencia residencia = residenciaRepository.findById(residenciaRequestBody.getId()).get();
		ResidenciaDto residenciaDto = this.residenciaMapper.residenciaToResidenciaDto(residencia);
		residenciaDto = this.mergeObject(residenciaDto, residenciaRequestBody);
		
		//Envia para a fila de Morador
		log.info("Enviando mensagem " +  residenciaRequestBody.toString() + " para o consumer.");
		
		this.producer.producerAsync(residenciaDto);
		
		ResponsePublisherDto response = ResponsePublisherDto
				.builder()
				.ticket(CabecalhoResponsePublisherDto
						.builder()
						.ticket(residenciaRequestBody.getGuide())
						.build())
				.build();
		
		return response;
	}
	
	public Page<GETResidenciaResponseDto> buscar(ResidenciaFiltro filtros, Pageable pageable) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
		
		log.info("Buscando residencia(s)...");
		
		List<GETResidenciaResponseDto> listaResidencias = new ArrayList<>();
		
		Response<List<GETResidenciaResponseDto>> response = new Response<List<GETResidenciaResponseDto>>();
		
		List<Residencia> residencias = this.queryRepository.query(filtros, pageable);
		
		long total = this.queryRepository.totalRegistros(filtros);
		
		for(Residencia residencia : residencias) {
			
			GETResidenciaResponseDto residenciaResponse = residenciaMapper.residenciaToGETResidenciaResponseDto(residencia);
			
			MoradorRequestDto request = MoradorRequestDto.builder()
					.residenciaId(residencia.getId().toString())
					.build();
			QueryResidenciaResponseDto responseMoradores = moradorSender.buscarMoradores(request);
			
			residenciaResponse.setMoradores(responseMoradores);
			listaResidencias.add(residenciaResponse);
		}
		
		response.setData(listaResidencias);
		
		return new PageImpl<>(response.getData(), pageable, total);
	}
	
	private String gerarGuide() {

		String guide = null;
		int i = 0;
		boolean ticketValido = false;
		
		do {
			i++;
			if(this.residenciaRepository.findByGuide(guide).isPresent())
				guide = UUID.randomUUID().toString();
			else if(guide == null)
				guide = UUID.randomUUID().toString();
			else {
				ticketValido = true;
			}
			
		}while(!ticketValido && i < guideLimit);
		
		return guide;
	}
	
	public ResidenciaDto mergeObject(ResidenciaDto t, AtualizaResidenciaDto x) {
		
		t.setEndereco(x.getEndereco());
		t.setNumero(x.getNumero());
		t.setComplemento(x.getComplemento());
		t.setBairro(x.getBairro());
		t.setCep(x.getCep());
		t.setCidade(x.getCidade());
		t.setUf(x.getUf());
		
		return t;
	}
	
}
