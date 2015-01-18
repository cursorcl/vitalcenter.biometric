package vitalcenter.osgi.persistence.models;

import java.util.Arrays;
import java.util.Date;

import cl.eos.interfaces.entity.IEntity;

/**
 * Esta clase contiene la informacion de un cliente.
 * 
 * @author cursor
 * 
 */
public class Client implements IEntity {
	/**
	 * Identificador de la BD.
	 */
	private Long id;
	/**
	 * Numero de identificacion unico.
	 */
	private String rut;
	/**
	 * Nombre en la BD.
	 */
	private String name;
	/**
	 * Fecha de la inscripcion.
	 */
	private Date inscription;
	/**
	 * Fecha de expiracion de su contrato.
	 */
	private Date expiration;
	/**
	 * Fecha de congelamiento.
	 */
	private Date frozen;
	/**
	 * State actual.
	 */
	private String state;
	/**
	 * Informacion de su huella digital. Corresponde al template serializado.
	 */
	private TemplateInfo template;

	public Client() {
		template = new TemplateInfo();
	}

	public Client(long id) {
		super();
		template = new TemplateInfo();
		this.id = id;
	}

	public Client(long id, String rut, String name, Date inscription, Date expiration, Date frozen, String state,
			TemplateInfo template) {
		super();
		this.id = id;
		this.rut = rut;
		this.name = name;
		this.inscription = inscription;
		this.expiration = expiration;
		this.frozen = frozen;
		this.state = state;
		this.template = template;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;

	}

	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public Date getInscription() {
		return inscription;
	}

	public void setInscription(Date inscription) {
		this.inscription = inscription;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	/**
	 * @return the frozen
	 */
	public Date getFrozen() {
		return frozen;
	}

	/**
	 * @param frozen
	 *            the frozen to set
	 */
	public void setFrozen(Date frozen) {
		this.frozen = frozen;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	public TemplateInfo getTemplate() {
		return template;
	}

	public void setTemplate(TemplateInfo template) {
		this.template = template;
	}

	public Client copy() {
		TemplateInfo newTemplate = new TemplateInfo(this.template.getFingerIndex(), Arrays.copyOf(
				this.template.getTemplate(), this.template.getTemplate().length));
		return new Client(id, rut, name, inscription, expiration, frozen, state, newTemplate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Client [rut=%s, name=%s, inscription=%s]", rut, name, inscription);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		Client other = (Client) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return id != null && name != null && rut != null && !rut.isEmpty();
	}

}
