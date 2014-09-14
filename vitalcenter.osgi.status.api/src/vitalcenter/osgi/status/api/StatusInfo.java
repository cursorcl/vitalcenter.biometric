package vitalcenter.osgi.status.api;

public class StatusInfo {

	private EStatus status;
	private String  message;
	
	
	public StatusInfo() {
		status = EStatus.OK;
	}
	
	public StatusInfo(EStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public EStatus getStatus() {
		return status;
	}
	public void setStatus(EStatus status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
