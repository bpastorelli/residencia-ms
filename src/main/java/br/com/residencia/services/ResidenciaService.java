package br.com.residencia.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.residencia.amqp.producer.impl.ResidenciaProducer;
import br.com.residencia.amqp.producer.impl.VinculosProducer;
import br.com.residencia.dto.AtualizaResidenciaDto;
import br.com.residencia.dto.CabecalhoResponsePublisherDto;
import br.com.residencia.dto.GETMoradoresSemResidenciaResponseDto;
import br.com.residencia.dto.GETResidenciaResponseDto;
import br.com.residencia.dto.GETResidenciasDto;
import br.com.residencia.dto.MoradorRequestDto;
import br.com.residencia.dto.PaginacaoDto;
import br.com.residencia.dto.QueryResidenciaResponseDto;
import br.com.residencia.dto.ResidenciaDto;
import br.com.residencia.dto.ResponsePublisherDto;
import br.com.residencia.dto.VinculoRequestDto;
import br.com.residencia.entities.Residencia;
import br.com.residencia.errorheadling.RegistroException;
import br.com.residencia.filter.ResidenciaFiltro;
import br.com.residencia.mappers.ResidenciaMapper;
import br.com.residencia.repositories.ResidenciaRepository;
import br.com.residencia.response.Response;
import br.com.residencia.senders.MoradorSender;
import br.com.residencia.validators.Validators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResidenciaService {
	 
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	@Autowired
	private ResidenciaProducer producer;
	
	@Autowired
	private VinculosProducer producerVinculos;
	
	@Autowired
	private Validators<ResidenciaDto, AtualizaResidenciaDto> validator;
	
	@Autowired
	private ResidenciaMapper residenciaMapper;
	
	@Autowired
	private MoradorSender moradorSender;
	
	public ResponsePublisherDto salvar(ResidenciaDto residenciaRequestBody) throws RegistroException {
		
		log.info("Cadastrando um morador: {}", residenciaRequestBody.toString());
		
		residenciaRequestBody.setGuide(UUID.randomUUID().toString());
		
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
		
		if (residenciaRequestBody.getTicketMorador() != null) {
			VinculoRequestDto requestDto = VinculoRequestDto.builder()
					.cepResidencia(residenciaRequestBody.getCep())
					.numeroResidencia(residenciaRequestBody.getNumero())
					.complementoResidencia(residenciaRequestBody.getComplemento())
					.ticketMorador(residenciaRequestBody.getTicketMorador())
					.build();
			this.producerVinculos.producerAsync(requestDto);	
		}
		
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
		
		residenciaRequestBody.setGuide(UUID.randomUUID().toString()); 	
		
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
	
	public Response<QueryResidenciaResponseDto> buscarPorIds(List<String> ids) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
		
		log.info("Buscando residencia(s)...");
		
		List<GETResidenciaResponseDto> listaResidencias = new ArrayList<>();
		
		Response<QueryResidenciaResponseDto> response = new Response<QueryResidenciaResponseDto>();
		
		List<Residencia> residencias = this.residenciaRepository.findResidenciasById(ids);
		
		for (Residencia residencia : residencias) {			
			GETResidenciaResponseDto residenciaResponse = residenciaMapper.residenciaToGETResidenciaResponseDto(residencia);
			listaResidencias.add(residenciaResponse);
		}
		
		QueryResidenciaResponseDto queryResidencia = new QueryResidenciaResponseDto();
		
		queryResidencia.setResidencias(listaResidencias);
		
		response.setData(queryResidencia);
		
		return response;
	}
	
	public Response<GETResidenciasDto> buscarPorFiltros(ResidenciaFiltro filtros, Pageable pageable) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
		
		log.info("Buscando residencia(s)...");
		
		List<GETResidenciaResponseDto> listaResidencias = new ArrayList<>();
		
		if (filtros.getDetalhaMorador() == null)
			filtros.setDetalhaMorador(Boolean.FALSE);
		
		Response<GETResidenciasDto> response = new Response<GETResidenciasDto>();
		
		PageRequest residenciaRequest = PageRequest.of(pageable.getPageNumber() == 0 ? 0 : (pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0), pageable.getPageSize());
		
		Page<Residencia> residencias = this.residenciaRepository.findResidenciaBy(filtros, residenciaRequest);
		
		for (Residencia residencia : residencias) {			
			GETResidenciaResponseDto residenciaResponse = residenciaMapper.residenciaToGETResidenciaResponseDto(residencia);
			
			if (filtros.getDetalhaMorador().equals(Boolean.TRUE)) {
				MoradorRequestDto request = MoradorRequestDto.builder()
						.residenciaId(residencia.getId())
						.build();
				GETMoradoresSemResidenciaResponseDto responseMoradores = moradorSender.buscarPorResidenciaId(request);
				
				residenciaResponse.setMoradores(responseMoradores);
			}
			listaResidencias.add(residenciaResponse);

		}
		
		int page = residencias.getNumber() == 0 ? 1 : (residencias.getNumber() >= 1 ? residencias.getNumber()+1 : 1);
		
		PaginacaoDto paginacao = PaginacaoDto.builder()
				.pagina(page)
				.paginaAnterior(page == 1 ? 1 : page-1)
				.proximaPagina(page < residencias.getTotalPages() ? page+1 : residencias.getTotalPages())
				.totalPaginas(residencias.getTotalPages())
				.totalItems(residencias.getTotalElements())
				.build();
		
		GETResidenciasDto residenciasResponse = GETResidenciasDto.builder()
				.residencias(listaResidencias)
				.paginacao(paginacao)
				.build();
		
		response.setData(residenciasResponse);
		
		return response;
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
