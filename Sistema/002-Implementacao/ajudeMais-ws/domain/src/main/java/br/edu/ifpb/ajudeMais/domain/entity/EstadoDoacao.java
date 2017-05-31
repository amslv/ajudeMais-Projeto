package br.edu.ifpb.ajudeMais.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.edu.ifpb.ajudeMais.domain.enumerations.Estado;

/**
 * 
 * 
 * <p>
 * <b> EstadoDoacao </b>
 * </p>
 *
 * <p>
 *	Classe que representa o estado de uma doação no sistema
 * </p>
 * 
 * @author <a href="https://github.com/amslv">Ana Silva</a>
 *
 */
@Entity
public class EstadoDoacao implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7652424045014577650L;
	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	/**
	 * 
	 */
	@Temporal(TemporalType.DATE)
	private Date data;
	
	/**
	 * 
	 */
	private Boolean notificado;
	
	/**
	 * 
	 */
	@Column(name="estado_atual", nullable=false)
	private Estado estadoDoacao;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the data
	 */
	public Date getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Date data) {
		this.data = data;
	}
	/**
	 * @return the notificado
	 */
	public Boolean getNotificado() {
		return notificado;
	}
	/**
	 * @param notificado the notificado to set
	 */
	public void setNotificado(Boolean notificado) {
		this.notificado = notificado;
	}
	/**
	 * @return the estadoDoacao
	 */
	public Estado getEstadoDoacao() {
		return estadoDoacao;
	}
	/**
	 * @param estadoDoacao the estadoDoacao to set
	 */
	public void setEstadoDoacao(Estado estadoDoacao) {
		this.estadoDoacao = estadoDoacao;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((estadoDoacao == null) ? 0 : estadoDoacao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((notificado == null) ? 0 : notificado.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EstadoDoacao other = (EstadoDoacao) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (estadoDoacao != other.estadoDoacao)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (notificado == null) {
			if (other.notificado != null)
				return false;
		} else if (!notificado.equals(other.notificado))
			return false;
		return true;
	}
}
