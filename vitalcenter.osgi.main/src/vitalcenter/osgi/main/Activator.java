package vitalcenter.osgi.main;

import java.util.Dictionary;
import java.util.Hashtable;

import kernel.osgi.api.space.IContainer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "vitalcenter.osgi.main"; //$NON-NLS-1$

	private ServiceRegistration<?> registration = null;
	private VitalcenterContainer vContainer = null;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	public void start(BundleContext context) throws Exception {

		vContainer = new VitalcenterContainer();

		vContainer.open();
		Dictionary<String, String> props = new Hashtable<String, String>();
		props.put("type", "IContainer");

		
		registration = context.registerService(IContainer.class.getName(),
				vContainer, props);
		
	}

	public void stop(BundleContext context) throws Exception {
		if (registration != null)
			registration.unregister();

		if (vContainer != null) {
			vContainer.close();
			vContainer = null;
		}
	}

}
