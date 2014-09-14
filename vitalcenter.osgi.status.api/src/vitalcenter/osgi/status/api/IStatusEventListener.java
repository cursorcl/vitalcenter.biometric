package vitalcenter.osgi.status.api;

public interface IStatusEventListener {

	/**
	 * Metodo que se gatilla cuando ocurre un evento de cambio de estado. 
	 * @param event Evento a generar.
	 */
	void onStatusEvent(StatusEvent event);
}
