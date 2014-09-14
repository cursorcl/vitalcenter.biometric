package vitalcenter.osgi.persistence.provider;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import vitalcenter.osgi.persistence.db.DBBridge;
import vitalcenter.osgi.persistence.models.Client;
import vitalcenter.osgi.persistence.models.IClientService;

public class ClientProvider implements IClientService {

	@Override
	public Client getClient(String rut) {
		Client client = null;
		try {
			client = DBBridge.getInstance().getClient(rut);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al buscar cliente en la BD", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return client;
	}

	@Override
	public boolean saveClient(Client client) {
		boolean saved = true;
		try {
			DBBridge.getInstance().saveTemplate(client);
		} catch (SQLException e) {
			saved = false;
			JOptionPane.showMessageDialog(null, "Error al grabar huellas del cliente en la BD", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return saved;
	}

	@Override
	public boolean deleteClient(Client client) {

		boolean deleted = true;
		try {
			DBBridge.getInstance().deleteTemplate(client);
		} catch (SQLException e) {
			deleted = false;
			JOptionPane.showMessageDialog(null, "Error al borrar cliente desde la BD", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return deleted;
	}

	@Override
	public List<Client> getClients() {
		return null;
	}

	@Override
	public List<Client> getTemplates() {
		List<Client> templates = null;
		try {
			templates = DBBridge.getInstance().getTemplates();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al obtener las huellas desde la BD", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return templates;
	}

	@Override
	public void saveAssistance(Client client) {
		try {
			DBBridge.getInstance().saveAssistance(client);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al grabar la asistencia del cliente en la BD", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

}
