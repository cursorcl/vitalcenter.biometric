package vitalcenter.osgi.rs232.status.validator;

import vitalcenter.osgi.rs232.IRS232Service;
import vitalcenter.osgi.rs232.provider.Rs232ServiceFactory;
import vitalcenter.osgi.status.api.EStatus;
import vitalcenter.osgi.status.api.IStatusValidator;
import vitalcenter.osgi.status.api.StatusInfo;

public class Rs232Validator implements IStatusValidator {
	private static final String NAME = "ACTUADOR PUERTA";
	private StatusInfo status = new StatusInfo(EStatus.UNKNOWN, "No se ha detectado la conexión.");
	private IRS232Service rs232;
	private boolean initialized = false;

	public Rs232Validator() {
		rs232 = Rs232ServiceFactory.getInstance();
		initialized = true;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public StatusInfo validate() {
		if (initialized) {
		if (rs232.isOpen()) {
			status.setStatus(EStatus.OK);
			status.setMessage("Actuador conectado.");
		} else {
			status.setStatus(EStatus.FAIL);
			status.setMessage("Actuador no conectado.");
			}
		}
		return status;
	}

	@Override
	public EStatus getStatus() {
		return status.getStatus();
	}

}
