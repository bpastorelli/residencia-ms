package br.com.residencia.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RetornoPaginadoDto<T> {
	
	private List<T> content;
			
	private PaginaResponseDto paginacao;
	
	@Getter
	@Setter
	@Builder
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PaginaResponseDto{
			
		private Integer pageNumber;
		private Integer pageSize;
		private Integer totalPages;
			
	}

}
