package br.com.residencia.controllers;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.residencia.dto.AtualizaResidenciaDto;
import br.com.residencia.dto.GETResidenciaResponseDto;
import br.com.residencia.dto.ResidenciaDto;
import br.com.residencia.dto.ResponsePublisherDto;
import br.com.residencia.errorheadling.RegistroException;
import br.com.residencia.errorheadling.RegistroExceptionHandler;
import br.com.residencia.filter.ResidenciaFiltro;
import br.com.residencia.services.ResidenciaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "Cadastro de Residências")
@RequestMapping("/sgc/residencia")
@CrossOrigin(origins = "*")
class ResidenciaController extends RegistroExceptionHandler {
	
	@Autowired
	private ResidenciaService residenciaService;
	
	@ApiOperation(value = "Produz uma mensagem no Kafka para cadastramento de uma nova residência.")
	@PostMapping(value = "/nova")
	public ResponseEntity<?> cadastrar(@Valid @RequestBody ResidenciaDto residenciaRequestBody,
											   BindingResult result ) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		ResponsePublisherDto response = this.residenciaService.salvar(residenciaRequestBody);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}
	
	@ApiOperation(value = "Pesquisa residências a partir dos filtros informados.")
	@GetMapping(value = "/filtro")
	public ResponseEntity<?> buscarResidenciasFiltro(
			ResidenciaFiltro filters,
			@PageableDefault(sort = "endereco", direction = Direction.ASC, page = 0, size = 10) Pageable paginacao) throws NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
		
		Page<GETResidenciaResponseDto> residencias = this.residenciaService.buscar(filters, paginacao);
		
		return filters.isContent() ? new ResponseEntity<>(residencias.getContent(), HttpStatus.OK) :
					new ResponseEntity<>(residencias, HttpStatus.OK);
		
	}
	
	@ApiOperation(value = "Produz uma mensagem no Kafka para atualização de uma residência.")
	@PutMapping(value = "/alterar")
	public ResponseEntity<?> alterar( 
			@Valid @RequestBody AtualizaResidenciaDto residenciaRequestBody,
			@RequestParam(value = "id", defaultValue = "null") Long id,
			BindingResult result) throws RegistroException{
		
		log.info("Enviando mensagem para o consumer...");
		
		residenciaRequestBody.setId(id);
		ResponsePublisherDto response = this.residenciaService.atualizar(residenciaRequestBody, id);
		
		return response.getTicket() == null ? 
				ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response.getErrors()) : 
				ResponseEntity.status(HttpStatus.ACCEPTED).body(response.getTicket());
		
	}

}
