package vitalcenter.osgi.persistence.models;

public class TemplateInfo {
	private int fingerIndex;
	private byte[] template;

	public TemplateInfo() {
		
	}

	public TemplateInfo(int fingerIndex, byte[] template) {
		super();
		this.fingerIndex = fingerIndex;
		this.template = template;
	}

	public int getFingerIndex() {
		return fingerIndex;
	}

	public void setFingerIndex(int fingerIndex) {
		this.fingerIndex = fingerIndex;
	}

	public byte[] getTemplate() {
		return template;
	}

	public void setTemplate(byte[] template) {
		this.template = template;
	}

}
