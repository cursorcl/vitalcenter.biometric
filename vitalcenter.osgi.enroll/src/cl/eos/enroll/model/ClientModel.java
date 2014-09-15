package cl.eos.enroll.model;

import vitalcenter.osgi.persistence.ClientServiceFactory;
import vitalcenter.osgi.persistence.models.Client;
import cl.eos.imp.model.AModel;
import cl.eos.interfaces.entity.IEntity;

public class ClientModel extends AModel {


	@Override
	public void save(IEntity entity) {
		entities.add(entity);
		ClientServiceFactory.getInstance().saveClient((Client) entity);
	}

	@Override
	public void delete(IEntity entity) {
		entities.remove(entity);
		ClientServiceFactory.getInstance().deleteClient(((Client) entity));

	}

	@Override
	public void update(IEntity entity) {
		delete(entity);
		save(entity);

	}

}
