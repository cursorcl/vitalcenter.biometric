package vitalcenter.osgi.main;

import java.awt.Container;

import javax.swing.JFrame;

import kernel.osgi.api.space.IContainer;
import kernel.osgi.api.space.IMainContainer;
import kernel.osgi.api.space.IMenuContainer;

public class VitalcenterContainer implements IContainer {

	private FrameContainer frame;
	private MenuContainer menuBar;

	/**
	 * Create the application.
	 */
	public VitalcenterContainer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new FrameContainer();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public IMenuContainer getMenuContainer() {
		if (menuBar == null) {
			menuBar = new MenuContainer();
			frame.setJMenuBar(menuBar);
		}
		return menuBar;
	}

	@Override
	public Container getStatusContainer() {
		return null;
	}

	@Override
	public IMainContainer getMainContainer() {
		if (frame == null) {
			frame = new FrameContainer();
		}
		return (IMainContainer)frame.getContentPane();
	}

	public void open()
	{
		frame.setVisible(true);
	}
	public void close()
	{
		if(frame != null)
		{
			frame.setVisible(false);
		}
	}
}
