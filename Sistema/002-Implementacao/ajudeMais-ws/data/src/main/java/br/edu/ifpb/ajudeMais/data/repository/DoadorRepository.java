/**
 * <p>
 * Ajude Mais - Módulo Web Service
 * </p>
 * 
 * <p>
 * Sistema para potencializar o processo de doação.
 * </p>
 * 
 * <a href="https://github.com/AjudeMais/AjudeMais">Ajude Mais</a>
 * <a href="https://franckaj.github.io">Franck Aragão"></a>
 * 
 * AJUDE MAIS - 2017®
 * 
 */
package br.edu.ifpb.ajudeMais.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.ajudeMais.domain.entity.Doador;

/**
 * 
 * <p>
 * {@link DoadorRepository}
 * </p>
 * 
 * <p>
 * Classe utilizada para acesso a dados em BD de um {@link Doador}
 * </p>
 *
 * <pre>
 * </pre
 *
 * @author <a href="https://franckaj.github.io">Franck Aragão</a>
 *
 */
public interface DoadorRepository extends JpaRepository<Doador, Long> {

	/**
	 * Monta uma query filtrando um doador pelo nome
	 * 
	 * @param nome
	 *            do doador
	 * @return uma lista de doadores que possui esse nome
	 */
	List<Doador> findByNome(String nome);
}
