package br.com.residencia.validators.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.residencia.dto.AtualizaResidenciaDto;
import br.com.residencia.dto.GETMoradoresSemResidenciaResponseDto;
import br.com.residencia.dto.GETVinculoMoradorResidenciaResponseDto;
import br.com.residencia.dto.MoradorRequestDto;
import br.com.residencia.dto.ResidenciaDto;
import br.com.residencia.dto.VinculoResidenciaRequestDto;
import br.com.residencia.entities.Residencia;
import br.com.residencia.errorheadling.ErroRegistro;
import br.com.residencia.errorheadling.RegistroException;
import br.com.residencia.repositories.ResidenciaRepository;
import br.com.residencia.senders.MoradorSender;
import br.com.residencia.senders.VinculosSender;
import br.com.residencia.validators.Validators;

@Component
public class ValidarCadastroResidencia implements Validators<ResidenciaDto, AtualizaResidenciaDto> {
	
	@Autowired
	private MoradorSender moradorSender;
	
	@Autowired
	private VinculosSender vinculosSender;
	
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	private static final String TITULO = "Cadastro de residência recusado!";
	
	@Override
	public void validarPost(ResidenciaDto t) throws RegistroException {
		
		List<ResidenciaDto> residencias = new ArrayList<ResidenciaDto>();
		residencias.add(t);
		
		validarPost(residencias);
		
	}
	
	@Override
	public void validarPut(AtualizaResidenciaDto t, Long id) throws RegistroException {
		
		List<AtualizaResidenciaDto> residencias = new ArrayList<AtualizaResidenciaDto>();
		residencias.add(t);
		
		validarPut(residencias, id);
		
	}
	
	@Override
	public void validarPost(List<ResidenciaDto> t) throws RegistroException {
		
		RegistroException errors = new RegistroException();

		t.forEach(r -> {
			
			if(r.getEndereco().isBlank() || r.getEndereco().isEmpty())
				errors.getErros().add(new ErroRegistro("", TITULO, " Campo endereço é obrigatório!")); 
				
			if(r.getNumero() == 0L || r.getNumero() == null)
				errors.getErros().add(new ErroRegistro("", TITULO, " Campo número é obrigatório!")); 
				
			if(r.getCep().isBlank() || r.getCep().isEmpty())
				errors.getErros().add(new ErroRegistro("", TITULO, " Campo CEP é obrigatório!"));
				
			if(r.getCidade().isBlank() || r.getCidade().isEmpty())
				errors.getErros().add(new ErroRegistro("", TITULO, " Campo Cidade é obrigatório!"));
				
			if(r.getUf().isBlank() || r.getUf().isEmpty())
				errors.getErros().add(new ErroRegistro("", TITULO, " Campo UF é obrigatório!"));
			
			Optional<Residencia> residencia = this.residenciaRepository.findByCepAndNumeroAndComplemento(r.getCep(), r.getNumero(), r.getComplemento().toUpperCase());
			
			if(r.getTicketMorador() == null) {
				if (residencia.isPresent())
					errors.getErros().add(new ErroRegistro("", TITULO, " Endereço já existente"));	
			}

			GETVinculoMoradorResidenciaResponseDto vinculos = null;
			
			if(r.getTicketMorador() != null) {
				
				MoradorRequestDto requestMorador = MoradorRequestDto.builder()
						.guide(r.getTicketMorador())
						.build();
				
				GETMoradoresSemResidenciaResponseDto moradores = null;
				
				try {
					moradores = moradorSender.buscarPorFiltros(requestMorador);
				} catch (IllegalArgumentException | IllegalAccessException | ClassNotFoundException e) {
					e.printStackTrace();
				}

				if(moradores.getMoradores().size() == 0)
					errors.getErros().add(new ErroRegistro("", TITULO, " Morador a ser vinculado não encontrado"));
				else {
					VinculoResidenciaRequestDto request = VinculoResidenciaRequestDto.builder()
						.moradorId(moradores.getMoradores().get(0).getId())
						.residenciaId(residencia.get().getId())
						.build();
				
					try {
						vinculos = this.vinculosSender.buscarResidenciasPorMorador(request);
					} catch (IllegalArgumentException | IllegalAccessException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				
			}
			
			if(residencia.isPresent() && r.getTicketMorador() != null && vinculos != null) {
				if(vinculos.getMorador().getResidencias().size() > 0) {
					errors.getErros().add(new ErroRegistro("", TITULO, " O morador informado já está vinculado a esta residência"));
				}
			}
			
		});
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}
	
	public void validarPut(List<AtualizaResidenciaDto> t, Long id) throws RegistroException {
		
		RegistroException errors = new RegistroException();

		t.forEach(r -> {
			
			Optional<Residencia> residenciaSource = this.residenciaRepository.findById(r.getId());
				
			if(residenciaSource.isPresent()) {
				if(!r.getCep().equals(residenciaSource.get().getCep()) && !r.getNumero().equals(residenciaSource.get().getNumero())) {
					if(this.residenciaRepository.findByCepAndNumeroAndComplemento(r.getCep(), r.getNumero(), r.getComplemento()).isPresent())
						errors.getErros().add(new ErroRegistro("", TITULO, " Não é possível realizar uma alteração de endereço para um endereço já existente!"));
				}	
			}else {
				errors.getErros().add(new ErroRegistro("", TITULO, " Residência não encontrada!"));
			}
									
			
		});
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
	}

	@Override
	public void validarPut(List<AtualizaResidenciaDto> listDto) throws RegistroException {
		// TODO Auto-generated method stub
		
	}

}
