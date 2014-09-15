package vitalcenter.osgi.status.api;

/**
 * Evento que notifica cuando hay cambio de estado en un componente.
 * 
 * @author eosorio
 */
public class StatusEvent {
	private Object source;
	private EStatus status;

	public StatusEvent() {
		source = null;
		status = EStatus.UNKNOWN;
	}

	public StatusEvent(Object owner, EStatus status) {
		super();
		this.source = owner;
		this.status = status;
	}

	public void setSource(Object owner) {
		this.source = owner;
	}

	public void setStatus(EStatus status) {
		this.status = status;
	}

	/**
	 * Quien gatilla el evento.
	 * 
	 * @return Objeto que gatilla el evento.
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * El estado del evento.
	 * 
	 * @return El valor del estado.
	 */
	public EStatus getStatus() {
		return status;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return source.toString() + " [" + status.name() + "]";
	}
	
	
}
