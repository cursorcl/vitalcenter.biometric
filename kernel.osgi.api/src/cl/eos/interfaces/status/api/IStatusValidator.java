package cl.eos.interfaces.status.api;


/**
 * Interface que debe implmentar el o los validadores de estado.
 * 
 * @author eosorio
 */
public interface IStatusValidator {
	/**
	 * Obtiene el nombre del validaor.
	 * 
	 * @return String con el nombre del validador.
	 */
	String getName();

	/**
	 * Realiza la validacion del estado asociado.
	 * 
	 * @return EStatus con el valor del estado.
	 */
	StatusInfo validate();

	/**
	 * Obtiene el ultimo valor de estado.
	 * 
	 * @return EStatus con el ultimo valor calculado.
	 */
	EStatus getStatus();

}
