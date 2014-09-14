package vitalcenter.osgi.persistence.models;

import java.util.List;

/**
 * Interface del servicio para acceder a clientes.
 * 
 * @author cursor
 * 
 */
public interface IClientService {

	Client getClient(String rut);

	boolean saveClient(Client client);

	boolean deleteClient(Client client);

	List<Client> getClients();
	
	List<Client> getTemplates();
	
	void saveAssistance(Client client);
}
