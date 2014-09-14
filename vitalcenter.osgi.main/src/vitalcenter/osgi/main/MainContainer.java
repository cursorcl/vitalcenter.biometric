package vitalcenter.osgi.main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.DefaultDesktopManager;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import kernel.osgi.api.space.IMainContainer;
import cl.eos.interfaces.view.IViewContainer;

/**
 * Contenedor de la aplicacion. En este caso se implementa con un JDesktopPane.
 * Es responsabilidad de este componente construir el InternalFrame de acuerdo
 * al IViewContainer que le asignan.
 * 
 * @author eosorio
 * 
 */
public class MainContainer extends JDesktopPane implements IMainContainer {

	private static final long serialVersionUID = 1L;
	
	class MyDesktopManager extends DefaultDesktopManager
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void openFrame(JInternalFrame f) {
			
			super.openFrame(f);
		}
		
	}
	
	
	public MainContainer() {
		setDesktopManager(new MyDesktopManager());
		
	}

	@Override
	public void add(IViewContainer view) {
		if (view instanceof Container) {
			JInternalFrame frame = new JInternalFrame();

			Dimension dim = view.getPreferredSize();
			frame.setSize((int)(dim.getWidth() + 10), (int)(dim.getHeight()  + 10));
			frame.setTitle(view.getTitle());
			frame.setName(view.getName());
			frame.getContentPane().setLayout(new BorderLayout());
			frame.setContentPane((Container) view);
			super.add(frame);
			frame.setVisible(true);
		}

	}

	@Override
	public void remove(IViewContainer view) {
		if (view instanceof Container) {
			for (JInternalFrame frame : getAllFrames()) {
				if (frame.getContentPane().equals(view)) {
					frame.setVisible(false);
					remove(frame);
					break;
				}
			}
		}
	}
	
	

}
