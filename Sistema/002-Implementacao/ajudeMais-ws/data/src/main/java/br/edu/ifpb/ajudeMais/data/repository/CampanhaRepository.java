package br.edu.ifpb.ajudeMais.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import br.edu.ifpb.ajudeMais.domain.entity.Campanha;
import br.edu.ifpb.ajudeMais.domain.entity.InstituicaoCaridade;

/**
 * 
 * Interface de percistecia para {@link Campanha}
 * 
 * @author elson / Franck
 * 
 *
 */
public interface CampanhaRepository extends JpaRepository<Campanha, Long> {

	/**
	 * Busca campanhas por instituição
	 * 
	 * @param instituicaoCaridade
	 * @return
	 */
	List<Campanha> findByInstituicaoCaridade(InstituicaoCaridade instituicaoCaridade);

	/**
	 * 
	 * <p>
	 * Busca campanhas por instituição, filtrando por localização e uf.
	 * </p>
	 * 
	 * @param localidade
	 * @param uf
	 * @return
	 */
	List<Campanha> filterByInstituicaoLocal(@Param("localidade") String localidade, @Param("uf") String uf);

	/**
	 * 
	 * <p>
	 * Busca campanhas por estatus.
	 * </p>
	 * 
	 * @param status
	 * @return
	 */
	List<Campanha> findByStatus(boolean status);
	
	/**
	 * Conta quantos donativos com a categoria passada na instituição com id passado existem.
	 * 
	 * @param nome
	 * @return
	 */
	Long findByMetasCategoria();
	
	/**
	 * 
	 * <p>
	 * Conta campanhas que utilizam que tem metas com a categoria com id passado
	 * </p>
	 * 
	 * @param id
	 * @return
	 */
	Long filterCountCampanhasMetaWithCategoriaId(@Param("idCategoria") Long id, @Param("idInstituicao") Long idInstituicao);

}
