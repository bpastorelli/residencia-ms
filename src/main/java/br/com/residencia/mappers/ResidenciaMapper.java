package br.com.residencia.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import br.com.residencia.dto.AtualizaResidenciaDto;
import br.com.residencia.dto.GETResidenciaResponseDto;
import br.com.residencia.dto.GETResidenciaSemMoradoresResponseDto;
import br.com.residencia.dto.ResidenciaDto;
import br.com.residencia.entities.Residencia;

@Mapper(componentModel = "spring")
public interface ResidenciaMapper {
	
	public abstract Residencia residenciaDtoToResiencia(ResidenciaDto dto);
	
	public abstract ResidenciaDto residenciaToResidenciaDto(Residencia residencia);
	
	public abstract ResidenciaDto atualizaResidenciaDtoToResidenciaDto(AtualizaResidenciaDto dto);
	
	public abstract GETResidenciaResponseDto residenciaToGETResidenciaResponseDto(Residencia residencia);
	
	public abstract GETResidenciaSemMoradoresResponseDto residenciaToGETResidenciaSemMoradoresResponseDto(Residencia residencia);
	
	public abstract List<Residencia> listResidenciaDtoToListResidencia(List<ResidenciaDto> dtos);

	public abstract List<ResidenciaDto> listResidenciaToListResidenciaDto(List<Residencia> residencias);
	
	public abstract List<GETResidenciaResponseDto> listResidenciaToListGETResidenciaResponseDto(List<Residencia> residencias);
}
