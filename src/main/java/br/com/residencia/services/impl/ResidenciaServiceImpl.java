package br.com.residencia.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.residencia.converter.Converter;
import br.com.residencia.dto.GETResidenciaResponseDto;
import br.com.residencia.entities.Residencia;
import br.com.residencia.filter.ResidenciaFiltro;
import br.com.residencia.repositories.query.QueryRepository;
import br.com.residencia.response.Response;
import br.com.residencia.services.ServicesCore;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResidenciaServiceImpl implements ServicesCore<GETResidenciaResponseDto, ResidenciaFiltro> {
	
	@Autowired
	private QueryRepository<Residencia, ResidenciaFiltro> queryRepository;
	
	@Autowired
	private Converter<List<GETResidenciaResponseDto>, List<Residencia>> converter;

	@Override
	public Page<GETResidenciaResponseDto> buscar(ResidenciaFiltro filtros, Pageable pageable) {
		
		log.info("Buscando residencia(s)...");
		
		Response<List<GETResidenciaResponseDto>> response = new Response<List<GETResidenciaResponseDto>>();
		
		response.setData(this.converter.convert(
				this.queryRepository.query(filtros, pageable)));
		
		long total = this.queryRepository.totalRegistros(filtros);
		
		return new PageImpl<>(response.getData(), pageable, total);
	}

	@Override
	public Optional<List<GETResidenciaResponseDto>> buscar(ResidenciaFiltro filter) {
		// TODO Auto-generated method stub
		return null;
	}

}
