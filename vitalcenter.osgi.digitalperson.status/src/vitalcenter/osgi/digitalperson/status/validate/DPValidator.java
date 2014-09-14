package vitalcenter.osgi.digitalperson.status.validate;

import vitalcenter.osgi.status.api.EStatus;
import vitalcenter.osgi.status.api.IStatusValidator;
import vitalcenter.osgi.status.api.StatusInfo;

import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.readers.DPFPReaderDescription;
import com.digitalpersona.onetouch.readers.DPFPReadersCollection;

public class DPValidator implements IStatusValidator {
	private static final String NAME = "Huella";
	private StatusInfo status = new StatusInfo();

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public StatusInfo validate() {
		DPFPReadersCollection readers = DPFPGlobal.getReadersFactory().getReaders();
		if (readers == null || readers.size() == 0) {
			status.setMessage("No hay lectores conectados");
			status.setStatus(EStatus.FAIL);
		} else {
			for (DPFPReaderDescription readerDescription : readers) {
				status.setMessage("Conectado:" + readerDescription.getProductName());
				status.setStatus(EStatus.OK);
				break;
			}
		}
		return status;
	}

	@Override
	public EStatus getStatus() {
		return status.getStatus();
	}

}
