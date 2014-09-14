package vitalcenter.osgi.rs232.provider;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import vitalcenter.osgi.rs232.IRS232Service;

public class Activator implements BundleActivator {

	private ServiceRegistration<?> registration = null;
	private IRS232Service rs232Service = null;

	@Override
	public void start(BundleContext context) {

		rs232Service = Rs232ServiceFactory.getInstance();
		if (!rs232Service.isOpen()) {
			JOptionPane.showMessageDialog(null, "No se tiene acceso al actuador de la a puerta", "Problemas",
					JOptionPane.INFORMATION_MESSAGE);
		}
		Dictionary<String, String> props = new Hashtable<String, String>();
		props.put("type", "IRS232Service");
		registration = context.registerService(IRS232Service.class.getName(), rs232Service, props);
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		if (registration != null)
			registration.unregister();

		if (rs232Service != null) {

		}

	}

}
