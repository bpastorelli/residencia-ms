package br.com.residencia.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.residencia.entities.Residencia;

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

}
