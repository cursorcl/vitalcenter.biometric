package vitalcenter.osgi.status.validator;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.status.api.IStatusValidator;
import cl.eos.interfaces.view.IViewContainer;

public class PnlStatus extends AFormView implements IViewContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<IStatusValidator, JPanel> views = new HashMap<IStatusValidator, JPanel>();

	/**
	 * Create the panel.
	 */
	public PnlStatus() {
		super();
		setTitle("Estado");
		setPreferredSize(new Dimension(200, 100));
		setSize(new Dimension(200, 100));
		setMaximumSize(new Dimension(200, 100));
		setMinimumSize(new Dimension(200, 100));
		setLayout(new MigLayout());
	}

	public void addValidator(IStatusValidator validator) {

		PnlLine panel = new PnlLine(validator.getName());
		add(panel, "grow,push, span");
		views.put(validator, panel);
		invalidate();
		repaint();
	}

	public void removeValidator(IStatusValidator validator) {
		JPanel panel = views.get(validator);
		if (panel != null) {
			remove(panel);
		}
		views.remove(validator);
		repaint();
	}
}
