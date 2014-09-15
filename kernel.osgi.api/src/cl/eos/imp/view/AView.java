package cl.eos.imp.view;

import java.util.List;

import javax.swing.JPanel;

import vitalcenter.osgi.status.api.StatusEvent;
import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.view.IView;

public class AView extends JPanel implements IView {

	private static final long serialVersionUID = 1L;
	
	private String title;
	protected IController controller;

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void onSaved(IEntity otObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeleted(IEntity otObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSelected(IEntity otObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setController(IController controller) {
		this.controller = controller;
	}

	@Override
	public IController getController() {
		return this.controller;
	}

	@Override
	public void onSaving(IEntity otObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeleting(IEntity otObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFound(IEntity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addView(IView view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeView(IView view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<IView> getViews() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onChangeStatus(StatusEvent status) {
		// TODO Auto-generated method stub
	}
	
}
