package vitalcenter.osgi.status.api;


public interface IStatusValidatorManager {

	void addValidator(IStatusValidator validator);
	void removeValidator(IStatusValidator validator);
	
	
	void addStatusEventListener(IStatusEventListener listener);
	void removeStatusEventListener(IStatusEventListener listener);
	
	void stop();
}
