package vitalcenter.osgi.main;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import kernel.osgi.api.space.IMenuContainer;
import cl.eos.interfaces.view.IMenu;
import cl.eos.interfaces.view.IMenuItem;

public class MenuContainer extends JMenuBar implements IMenuContainer {

	private static final long serialVersionUID = 1L;

	public MenuContainer() {
		super();
	}

	@Override
	public void addMenu(IMenu menu) {
		if (menu != null && menu instanceof JMenu) {
			add((JMenu) menu);
		}
	}

	@Override
	public void addMenu(IMenu parent, IMenu child) {
		if (parent != null && child != null && parent instanceof JMenu
				&& child instanceof JMenu) {
			((JMenu) parent).add((JMenu) child);
		}
	}

	@Override
	public void addMenuItem(IMenu parent, IMenuItem menuItem) {
		if (parent != null && menuItem != null && parent instanceof JMenu
				&& menuItem instanceof JMenuItem) {
			((JMenu) parent).add((JMenuItem) menuItem);
		}
	}

	@Override
	public void remove(IMenu menu) {
		if (menu != null && menu instanceof JMenu) {
			remove((JMenu) menu);
		}
	}

	@Override
	public void remove(IMenu parent, IMenu child) {
		if (parent != null && child != null && parent instanceof JMenu
				&& child instanceof JMenu) {
			((JMenu) parent).remove((JMenu) child);
		}
	}

	@Override
	public void remove(IMenu parent, IMenuItem menuItem) {
		if (parent != null && menuItem != null && parent instanceof JMenu
				&& menuItem instanceof JMenuItem) {
			((JMenu) parent).remove((JMenuItem) menuItem);
		}
	}

}
