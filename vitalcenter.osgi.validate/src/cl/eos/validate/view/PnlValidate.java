package cl.eos.validate.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import vitalcenter.osgi.digitalperson.status.validate.DPValidator;
import vitalcenter.osgi.persistence.models.Client;
import vitalcenter.osgi.persistence.models.IClientService;
import vitalcenter.osgi.rs232.IRS232Service;
import vitalcenter.osgi.rs232.provider.Rs232ServiceFactory;
import vitalcenter.osgi.rs232.status.validator.Rs232Validator;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.status.api.EStatus;
import cl.eos.interfaces.status.api.StatusEvent;
import cl.eos.interfaces.view.IViewContainer;
import cl.eos.validate.controller.ValidateController;

import com.digitalpersona.onetouch.ui.swing.DPFPVerificationControl;
import com.digitalpersona.onetouch.ui.swing.DPFPVerificationEvent;
import com.digitalpersona.onetouch.ui.swing.DPFPVerificationListener;
import com.digitalpersona.onetouch.ui.swing.DPFPVerificationVetoException;

public class PnlValidate extends AFormView implements IViewContainer {

	public final byte[] OFF = { (byte) 255, (byte) 1, (byte) 0 };
	public final byte[] ON = { (byte) 255, (byte) 1, (byte) 1 };
	private static final long serialVersionUID = 1L;
	private IClientService service;
	private DPFPVerificationControl verificationControl;
	private JButton btnActivate;
	private JButton btnStop;
	private JLabel lblName;
	private JLabel lblRut;
	private JLabel lblExpiration;
	private JPanel panel;
	private IRS232Service rs232;
	private Timer timer;
	private boolean isReaderOk;
	private boolean isActionerOk;

	/**
	 * Create the panel.
	 */
	public PnlValidate() {
		setSize(new Dimension(580, 140));
		setPreferredSize(new Dimension(580, 140));
		setMinimumSize(new Dimension(580, 140));
		setMaximumSize(new Dimension(580, 140));

		setTitle("Ingreso");
		setLayout(new MigLayout("", "[][277.00,grow][68.00]", "[37.00,baseline][23.00][43.00]"));

		verificationControl = new DPFPVerificationControl();
		verificationControl.setBorder(null);
		verificationControl.addVerificationListener(new DPFPVerificationListener() {
			public void captureCompleted(DPFPVerificationEvent e) throws DPFPVerificationVetoException {
				e.setStopCapture(false); // we want to continue capture until
											// the dialog is closed
				boolean result = ((ValidateController) controller).validate(e.getFeatureSet());
				e.setMatched(result);
			}
		});
		add(verificationControl, "cell 0 0 1 2,grow");

		panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel, "cell 1 0 1 3,grow");
		panel.setLayout(new MigLayout("", "[261.00,grow]", "[40.00][45.00][41.00]"));

		lblRut = new JLabel("");
		lblRut.setFocusable(false);
		panel.add(lblRut, "cell 0 0,growx");
		lblRut.setFont(new Font("Tahoma", Font.BOLD, 15));

		lblName = new JLabel("");
		lblName.setFocusable(false);
		panel.add(lblName, "cell 0 1,growx");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 15));

		lblExpiration = new JLabel("");
		lblExpiration.setFocusable(false);
		panel.add(lblExpiration, "cell 0 2,growx");
		lblExpiration.setFont(new Font("Tahoma", Font.BOLD, 15));

		btnActivate = new JButton("Activar");
		btnActivate.setIcon(new ImageIcon(PnlValidate.class.getResource("/img/dialog-apply.png")));
		btnActivate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.setBackground(UIManager.getColor("Panel.background"));
				verificationControl.start();
				btnActivate.setEnabled(false);
				btnStop.setEnabled(true);
			}
		});
		add(btnActivate, "cell 2 0,grow");

		btnStop = new JButton("Detener");
		btnStop.setIcon(new ImageIcon(PnlValidate.class.getResource("/img/dialog-cancel-4.png")));
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setBackground(Color.GRAY);
				verificationControl.stop();
				btnStop.setEnabled(false);
				btnActivate.setEnabled(true);
			}
		});
		add(btnStop, "cell 2 1,grow");

		timer = new Timer();

		rs232 = Rs232ServiceFactory.getInstance();

	}

	public IClientService getService() {
		return service;
	}

	public void setService(IClientService service) {
		this.service = service;
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity != null) {
			Client activeClient = (Client) entity;
			lblName.setText(activeClient.getName());
			lblRut.setText(activeClient.getRut());
			lblExpiration.setText(activeClient.getExpiration().toString());

			if (activeClient.getExpiration().getTime() < System.currentTimeMillis()) {
				lblExpiration.setText("Contrato caducado:" + activeClient.getExpiration().toString());
			} else {
				lblExpiration.setText(activeClient.getExpiration().toString());
				this.service.saveAssistance(activeClient);
				if (rs232 != null) {
					try {
						rs232.write(ON);
					} catch (IOException e) {

					}
					try {
						rs232.write(OFF);
					} catch (IOException e) {
					}
				}
			}
		} else {
			lblName.setText("Huella no encontrada");
			lblRut.setText("");
			lblExpiration.setText("");
		}
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				lblName.setText("");
				lblRut.setText("");
				lblExpiration.setText("");
			}
		}, 2000);

	}
	
	@Override
	public void onChangeStatus(StatusEvent status) {
		if(status.getSource() instanceof DPValidator)
		{
			EStatus lStatus = status.getStatus();
			isReaderOk = lStatus.equals(EStatus.OK);
			verificationControl.setEnabled(isReaderOk);
			btnActivate.setEnabled(isReaderOk);
			btnStop.setEnabled(isReaderOk);
			panel.setEnabled(isReaderOk);
			if(!isReaderOk)
			{
				JOptionPane.showMessageDialog(this, "Problemas con el lector de huellas", "Problema", JOptionPane.ERROR_MESSAGE);
			}
		} else 
		if(status.getSource() instanceof Rs232Validator)
		{
			EStatus lStatus = status.getStatus();
			isActionerOk = lStatus.equals(EStatus.OK);
			verificationControl.setEnabled(isActionerOk);
			btnActivate.setEnabled(isActionerOk);
			btnStop.setEnabled(isActionerOk);
			panel.setEnabled(isActionerOk);
			if(!isActionerOk && !EStatus.UNKNOWN.equals(lStatus))
			{
				JOptionPane.showMessageDialog(this, "Problemas con el actuador de la puerta", "Problema", JOptionPane.ERROR_MESSAGE);
			}
		}

	}
}
