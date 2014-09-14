package cl.eos.imp.view;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.view.IFormView;

public class AFormView extends AView implements IFormView {

	private static final long serialVersionUID = 1L;

	@Override
	public void save(IEntity otObject) {
		if (controller != null && validate(otObject)) {
			controller.save(otObject);
		}
	}

	@Override
	public void delete(IEntity otObject) {
		if (controller != null && validate(otObject)) {
			controller.save(otObject);
		}
	}

	@Override
	public void select(IEntity otObject) {
		if (controller != null) {
			controller.select(otObject);
		}
	}

	@Override
	public boolean validate(IEntity otObject) {
		return true;
	}

}
