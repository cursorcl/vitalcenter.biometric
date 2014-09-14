/**
 * 14/09/2011 - ayachan
 */
package kernel.osgi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

/**
 * An activator that perform a minimal monitoring of required services.<br />
 * Users should extend this class and implement the methods that reveal the
 * required services and react to service register and unregister.
 * <p>
 * As the monitoring can include more than one service to track, and the
 * listener registering works just for one filter, we need to register to any
 * service and make the filter as the services are registered.
 * </p>
 * 
 * TODO Classical registering with filter can be used when only one service is
 * required. This should optimize event handling.
 * 
 * @author ayachan
 */
abstract public class MonitoringActivator implements BundleActivator,
		ServiceListener {
	/**
	 * Filter to use when asking for the service.<br />
	 * It could look something like "<code>(&(language=*))</code>".
	 */
	static protected final String PROPERTY_SERVICE_FILTER = "SERVICE-FILTER";
	/**
	 * Path to be used when registering the service event listener.<br />
	 * It could look something like "
	 * <code>(&(objectClass=%s)(language=*))</code>" including the class name
	 * (in place of '%s').
	 * <p>
	 * This implementation provides a default one like "
	 * <code>(&(objectClass=%s))</code>" when not given.
	 */
	static protected final String PROPERTY_LISTENER_REGISTER = "SERVICE-LISTENER";

	private Logger logger = Logger.getLogger(MonitoringActivator.class
			.getPackage().getName());

	private BundleContext context = null;
	private Map<String, Properties> reqs = null;

	private Map<String, Filter> filters = null;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		this.context = bundleContext;

		filters = new HashMap<String, Filter>();

		// get the requirements
		logger.log(Level.FINE, "getting service requirements");
		reqs = getServiceRequirements();

		// iterate on each key first asking and then registering
		logger.log(Level.FINE, "checking existing services");
		Iterator<String> kit = reqs.keySet().iterator();
		while (kit.hasNext()) {
			String name = kit.next();

			String serviceFilter = null;
			String registry = null;
			if (reqs.get(name) != null) {
				serviceFilter = reqs.get(name).getProperty(
						PROPERTY_SERVICE_FILTER);
				registry = reqs.get(name).getProperty(
						PROPERTY_LISTENER_REGISTER, null);
			}

			checkService(name, serviceFilter);

			if (registry == null) {
				// create default service register path
				registry = String.format("(&(objectClass=%s))", name);
			}
			filters.put(name, bundleContext.createFilter(registry));
		}

		logger.log(Level.FINE, "registering service listening");
		context.addServiceListener(this);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		logger.log(Level.FINE, "unregistering services listener");
		bundleContext.removeServiceListener(this);
	}

	/**
	 * @return the context
	 */
	public BundleContext getContext() {
		return context;
	}

	/**
	 * Check required services prior to subscribing.
	 * 
	 * @throws InvalidSyntaxException
	 */
	protected void checkService(String name, String serviceFilter) throws InvalidSyntaxException {
		logger.log(Level.FINE, String.format("checking service %s...", name));
		ServiceReference<?>[] srefs = context.getAllServiceReferences(name, serviceFilter);
		if (srefs != null) {
			for (ServiceReference<?> sref : srefs)
				serviceRegistered(name, sref);
		}
	}

	/**
	 * Look if any of my registered services works with this report.
	 * 
	 * @param sref
	 * @return
	 */
	protected String fetchService(ServiceReference<?> sref) {
		String result = null;
		Iterator<String> nit = filters.keySet().iterator();
		while (nit.hasNext()) {
			String name = nit.next();
			if (filters.get(name).match(sref)) {
				result = name;
				break;
			}
		}
		return result;
	}

	static private final String OBJECT_CLASS_PROP = "objectClass";

	@Override
	public void serviceChanged(ServiceEvent event) {
		ServiceReference<?> sref = event.getServiceReference();
		if (sref != null) {
			String[] objectClass = (String[]) event.getServiceReference()
					.getProperty(OBJECT_CLASS_PROP);

			String serviceName = fetchService(sref);
			if (serviceName != null) {
				switch (event.getType()) {
				case ServiceEvent.REGISTERED: {
					serviceRegistered(objectClass[0], sref);
				}
					break;
				case ServiceEvent.UNREGISTERING: {
					serviceUnregistered(objectClass[0], sref);
				}
					break;

				case ServiceEvent.MODIFIED:
				case ServiceEvent.MODIFIED_ENDMATCH:
				default: {
					// nothing to do here
				}
					break;
				}
			} else {
				logger.log(Level.FINE, String.format(
						"unhandled event type %s on service of type %s\n",
						event.getType(), objectClass[0]));
			}
		}
	}

	/**
	 * Retrieve the service requirements of the intended bundle.<br />
	 * The key of each entry the map should be the class name of the service and
	 * the properties should have necessary associations to make the request and
	 * register of the service (at least registering filter under the key
	 * <code>PROPERTY_SERVICE_FILTER</code>).
	 * 
	 * @return The services required.
	 */
	abstract protected Map<String, Properties> getServiceRequirements();

	/**
	 * Sort of callback to report that a service (from the list) was detected as
	 * registered.<br />
	 * The same service specification can have be reported with more than one
	 * instance.
	 * 
	 * @param name
	 *            The name of the service.
	 * @param sref
	 *            The service reference.
	 */
	abstract protected void serviceRegistered(String name,
			ServiceReference<?> sref);

	/**
	 * Same as <code>serviceRegistered</code> for service unregistering.
	 * 
	 * @param name
	 * @param sref
	 */
	abstract protected void serviceUnregistered(String name,
			ServiceReference<?> sref);
}
