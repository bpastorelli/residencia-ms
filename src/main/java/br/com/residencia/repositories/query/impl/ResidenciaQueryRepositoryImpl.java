package br.com.residencia.repositories.query.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.residencia.entities.Residencia;
import br.com.residencia.filter.ResidenciaFiltro;
import br.com.residencia.repositories.query.QueryRepository;

@Repository
public class ResidenciaQueryRepositoryImpl implements QueryRepository<Residencia, ResidenciaFiltro> {
	
	@PersistenceContext
	private EntityManager manager;
	private static Root<Residencia> entity_;
	private CriteriaBuilder builder;
	private CriteriaQuery<Residencia> query;
	
	
	@Override
	public List<Residencia> query(ResidenciaFiltro filters, Pageable pageable){
		
		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Residencia.class);
		
        entity_ = query.from(Residencia.class);
        
        this.query.where(this.criarFiltros(entity_, filters, builder));
        this.query.orderBy(builder.asc(entity_.get("endereco")));
        
        TypedQuery<Residencia> typedQuery = manager.createQuery(query);
        
        this.aplicarFiltroPaginacao(typedQuery, pageable);
        
        return typedQuery.getResultList();
	}
	
	@Override
	public Predicate[] criarFiltros(Root<Residencia> root, ResidenciaFiltro filters, CriteriaBuilder builder) {
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(filters.getId() != null)
			predicates.add(builder.equal(root.get("id"), filters.getId()));
		
		if(filters.getEndereco() != null)
			predicates.add(builder.like(root.get("endereco"), filters.getEndereco() + '%'));
		
		if(filters.getNumero() != null)
			predicates.add(builder.equal(root.get("numero"), filters.getNumero()));
		
		if(filters.getComplemento() != null)
			predicates.add(builder.equal(root.get("complemento"), filters.getComplemento()));
		
		if(filters.getCep() != null)
			predicates.add(builder.equal(root.get("cep"), filters.getCep()));
		
		if(filters.getCidade() != null)
			predicates.add(builder.equal(root.get("cidade"), filters.getCidade()));
		
		if(filters.getUf() != null)
			predicates.add(builder.equal(root.get("uf"), filters.getUf()));
		
		if(filters.getGuide() != null)
			predicates.add(builder.equal(root.get("guide"), filters.getGuide()));
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public long totalRegistros(ResidenciaFiltro filters) {
		
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		entity_ = countQuery.from(query.getResultType());
		entity_.alias("total");
		countQuery.select(builder.count(entity_));
		countQuery.where(this.criarFiltros(entity_, filters, builder));
		
		return manager.createQuery(countQuery).getSingleResult();	
		
	}

	@Override
	public void aplicarFiltroPaginacao(TypedQuery<Residencia> typedQuery, Pageable pageable) {
		
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
        
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
		
	}

	@Override
	public List<Residencia> query(ResidenciaFiltro filters) {

		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Residencia.class);
		
        entity_ = query.from(Residencia.class);
        
        this.query.where(this.criarFiltros(entity_, filters, builder));
        
        TypedQuery<Residencia> typedQuery = manager.createQuery(query);
        
        return typedQuery.getResultList();
		
	}
	
}
