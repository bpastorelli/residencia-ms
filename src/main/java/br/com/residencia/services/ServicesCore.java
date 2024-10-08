package br.com.residencia.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 
 * @author bodl
 *
 * @param <T> Response DTO
 * @param <F> Filter DTO
 */
public interface ServicesCore<T, F> {
	
	Page<T> buscar(F filter, Pageable pageable);
	
	Optional<List<T>> buscar(F filter);

}
