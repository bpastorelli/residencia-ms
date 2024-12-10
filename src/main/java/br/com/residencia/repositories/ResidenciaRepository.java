package br.com.residencia.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.residencia.entities.Residencia;
import br.com.residencia.filter.ResidenciaFiltro;

@Repository
@Transactional(readOnly = true)
public interface ResidenciaRepository extends JpaRepository<Residencia, Long> {
	
	@Transactional(readOnly = true)
	Optional<Residencia> findById(Long id);
	
	@Transactional(readOnly = true)
	Page<Residencia> findByIdOrEnderecoContainsOrNumero(Long id, String endereco, Long numero, Pageable pageable);
	
	@Transactional(readOnly = true)
	Page<Residencia> findByEnderecoContainsAndNumero(String endereco, Long numero, Pageable pageable);
	
	Residencia findByEnderecoContainsAndNumero(String endereco, Long numero);
	
	Page<Residencia> findByEnderecoAndNumero(String endereco, Long numero, Pageable pageable);
	
	Optional<Residencia> findByCepAndNumeroAndComplemento(String cep, Long numero, String complemento);
	
	Optional<Residencia> findByGuide(String guide);
	
	@Query(value = "select *"
			+ " from residencia r"
			+ " where (r.id IN (:#{#ids})) "
			, nativeQuery = true)
	public List<Residencia> findResidenciasById(@Param("ids") List<String> ids);
	
	@Query(value = "select *"
			+ " from residencia r "
			+ " where (r.id = :#{#filter.id} OR :#{#filter.id} IS NULL) "
			+ " and (r.endereco like %:#{#filter.endereco}% OR :#{#filter.endereco} IS NULL) "
			+ " and (r.numero = :#{#filter.numero} OR :#{#filter.numero} IS NULL) "
			+ " and (r.complemento = :#{#filter.complemento} OR :#{#filter.complemento} IS NULL) "
			+ " and (r.cep = :#{#filter.cep} OR :#{#filter.cep} IS NULL) "
			+ " and (r.cidade = :#{#filter.cidade} OR :#{#filter.cidade} IS NULL) "
			+ " and (r.uf = :#{#filter.uf} OR :#{#filter.uf} IS NULL) "
			+ " and (r.guide = :#{#filter.guide} OR :#{#filter.guide} IS NULL) "
			, nativeQuery = true)
	public List<Residencia> findResidenciaBy(@Param("filter") ResidenciaFiltro filter, Pageable pageable);
	
	@Query(value = "select *"
			+ " from residencia r "
			+ " where (r.id = :#{#filter.id} OR :#{#filter.id} IS NULL) "
			+ " and (r.endereco like %:#{#filter.endereco}% OR :#{#filter.endereco} IS NULL) "
			+ " and (r.numero = :#{#filter.numero} OR :#{#filter.numero} IS NULL) "
			+ " and (r.complemento = :#{#filter.complemento} OR :#{#filter.complemento} IS NULL) "
			+ " and (r.cep = :#{#filter.cep} OR :#{#filter.cep} IS NULL) "
			+ " and (r.cidade = :#{#filter.cidade} OR :#{#filter.cidade} IS NULL) "
			+ " and (r.uf = :#{#filter.uf} OR :#{#filter.uf} IS NULL) "
			+ " and (r.guide = :#{#filter.guide} OR :#{#filter.guide} IS NULL) "
			, nativeQuery = true)
	public List<Residencia> findResidenciaBy(@Param("filter") ResidenciaFiltro filter);
	
	@Query(value = "select count(*)"
			+ " from residencia r "
			+ " where (r.id = :#{#filter.id} OR :#{#filter.id} IS NULL) "
			+ " and (r.endereco like %:#{#filter.endereco}% OR :#{#filter.endereco} IS NULL) "
			+ " and (r.numero = :#{#filter.numero} OR :#{#filter.numero} IS NULL) "
			+ " and (r.complemento = :#{#filter.complemento} OR :#{#filter.complemento} IS NULL) "
			+ " and (r.cep = :#{#filter.cep} OR :#{#filter.cep} IS NULL) "
			+ " and (r.cidade = :#{#filter.cidade} OR :#{#filter.cidade} IS NULL) "
			+ " and (r.uf = :#{#filter.uf} OR :#{#filter.uf} IS NULL) "
			+ " and (r.guide = :#{#filter.guide} OR :#{#filter.guide} IS NULL) "
			, nativeQuery = true)
	public Long totalRegistros(@Param("filter") ResidenciaFiltro filter);

}
