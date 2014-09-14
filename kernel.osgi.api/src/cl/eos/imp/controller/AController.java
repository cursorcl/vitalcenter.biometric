package cl.eos.imp.controller;

import java.util.ArrayList;
import java.util.List;

import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.model.IModel;
import cl.eos.interfaces.view.IView;

public abstract class AController implements IController {

	protected List<IView> views;
	protected IEntity selectedEntity;

	protected IModel model;

	@Override
	public void save(IEntity entity) {
		if (model != null) {
			model.save(entity);
			notifySaved(entity);
		}
	}

	@Override
	public void delete(IEntity entity) {
		if (model != null) {
			model.delete(entity);
		}
	}

	@Override
	public void select(IEntity otObject) {
		if (model != null) {
			selectedEntity = model.get(otObject);
		}
	}

	@Override
	public void addView(IView view) {
		if (views == null) {
			views = new ArrayList<IView>();
		}
		views.add(view);
		view.setController(this);
	}

	@Override
	public void removeView(IView view) {
		if (views != null) {
			views.remove(view);
		}

	}

	@Override
	public List<IView> getViews() {
		return views;
	}

	@Override
	public void add(IEntity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyFound(IEntity entity) {
		if (views != null) {
			for (IView view : views) {
				view.onFound(entity);
			}
		}
	}

	@Override
	public void notifySaved(IEntity entity) {
		if (views != null) {
			for (IView view : views) {
				view.onSaved(entity);
			}
		}
	}

}
