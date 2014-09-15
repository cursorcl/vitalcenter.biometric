package vitalcenter.osgi.status.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cl.eos.interfaces.status.api.EStatus;
import cl.eos.interfaces.status.api.IStatusEventListener;
import cl.eos.interfaces.status.api.IStatusValidator;
import cl.eos.interfaces.status.api.IStatusValidatorManager;
import cl.eos.interfaces.status.api.StatusEvent;
import cl.eos.interfaces.status.api.StatusInfo;

public class StatusValidatorManager implements IStatusValidatorManager {

	/**
	 * Lista de validadores inscritos.
	 */
	private List<IStatusValidator> validators = new ArrayList<IStatusValidator>();
	private List<IStatusEventListener> listeners = new ArrayList<IStatusEventListener>();

	private boolean isAlive = true;

	private final Executor service = Executors.newFixedThreadPool(5);
	private PnlStatus viewer = new PnlStatus();

	public StatusValidatorManager() {
		service.execute(new EvaluateTask());
	}

	@Override
	public void addValidator(IStatusValidator validator) {
		synchronized (validators) {
			validators.add(validator);
			viewer.addValidator(validator);
		}

	}

	@Override
	public void removeValidator(IStatusValidator validator) {
		synchronized (validators) {
			validators.remove(validator);
			viewer.removeValidator(validator);
		}

	}

	/**
	 * Clase para la evaluacion periodica del estado de los validadores.
	 * 
	 * @author eosorio
	 */
	private class EvaluateTask implements Runnable {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			while (StatusValidatorManager.this.isAlive) {
				try {
					synchronized (validators) {
						for (final IStatusValidator validator : StatusValidatorManager.this.validators) {
							EStatus actualStatus = validator.getStatus();
							final StatusInfo status = validator.validate();
							if (!actualStatus.equals(status)) {
								synchronized (listeners) {
									for (IStatusEventListener listener : listeners) {
										listener.onStatusEvent(new StatusEvent(validator, actualStatus));
									}
								}
							}
						}
					}
					Thread.sleep(500);
				} catch (final InterruptedException e) {
					StatusValidatorManager.this.isAlive = false;
				}

			}
		}
	}

	@Override
	public void stop() {
		isAlive = false;
	}

	@Override
	public void addStatusEventListener(IStatusEventListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}

	}

	@Override
	public void removeStatusEventListener(IStatusEventListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}

	}
}
