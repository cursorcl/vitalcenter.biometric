package vitalcenter.osgi.rs232.provider;

import vitalcenter.osgi.rs232.IRS232Service;

public class Rs232ServiceFactory {

	private static IRS232Service rs23sService = null;

	public static IRS232Service getInstance() {
		if (rs23sService == null) {
			rs23sService = new RS232Service();
		}
		return rs23sService;
	}
}
