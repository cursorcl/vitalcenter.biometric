package vitalcenter.osgi.rs232;

import java.io.IOException;

/**
 * Interface para la puerta de comunicaciones RS232.
 * 
 * @author eosorio
 */
public interface IRS232Service {
	public static final String COMM_PORT_NAME = "COM14";

	/**
	 * Crea la conexion a la puerta y la deja abierta para su uso.
	 * 
	 * @return Retorna verdadero si queda conectado.
	 */
	boolean open();

	/**
	 * Cierra la conexion.
	 * 
	 * @return
	 */
	boolean close();

	/**
	 * Escribe los bytes indicados.
	 * 
	 * @param bytes
	 *            Bytes a escribir.
	 * @throws IOException
	 *             Excepcion generada al existir error en la puerta.
	 */
	void write(byte[] bytes) throws IOException;

	/**
	 * Indica si la puerta se encuentra abierta.
	 * 
	 * @return Verdadero si esta abierta.
	 */
	boolean isOpen();

}
