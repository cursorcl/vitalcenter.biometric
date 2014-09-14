package vitalcenter.osgi.persistence;

import vitalcenter.osgi.persistence.models.IClientService;
import vitalcenter.osgi.persistence.provider.ClientProvider;

public class ClientServiceFactory {
	private static IClientService clientService = new ClientProvider();

	public static IClientService getInstance() {
		return clientService;
	}
}
