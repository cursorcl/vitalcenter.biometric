package vitalcenter.osgi.status.validator;

import cl.eos.interfaces.status.api.IStatusValidatorManager;

public class StatusValidatorFactory {

	private static IStatusValidatorManager statusValidatorManager = new StatusValidatorManager();

	public static IStatusValidatorManager getInstance() {
		return statusValidatorManager;
	}
}
