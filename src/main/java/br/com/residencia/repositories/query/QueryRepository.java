package br.com.residencia.repositories.query;
	
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;

public interface QueryRepository<T, Z> {
	
	public List<T> query(Z filters, Pageable pageable);
	
	public List<T> query(Z filters);
	
	public Predicate[] criarFiltros(Root<T> root, Z filters, CriteriaBuilder builder);
	
	public long totalRegistros(Z filters);
	
	public void aplicarFiltroPaginacao(TypedQuery<T> typedQuery, Pageable pageable);
}
