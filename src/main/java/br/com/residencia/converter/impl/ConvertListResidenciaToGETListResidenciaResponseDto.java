package br.com.residencia.converter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.residencia.converter.Converter;
import br.com.residencia.dto.GETResidenciaResponseDto;
import br.com.residencia.entities.Residencia;
import br.com.residencia.mappers.ResidenciaMapper;

@Component
public class ConvertListResidenciaToGETListResidenciaResponseDto implements Converter<List<GETResidenciaResponseDto>, List<Residencia>> {
	
	@Autowired
	private ResidenciaMapper residenciaMapper;
	
	@Override
	public List<GETResidenciaResponseDto> convert(List<Residencia> residencias) {
		
		List<GETResidenciaResponseDto> response = new ArrayList<GETResidenciaResponseDto>();
		
		residencias.forEach(m -> {
			
			GETResidenciaResponseDto residencia = new GETResidenciaResponseDto();
			residencia = this.residenciaMapper.residenciaToGETResidenciaResponseDto(m);
			
			response.add(residencia);
			
		});
		
		
		return response;
	}

}
