package cl.eos.validate.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import vitalcenter.osgi.persistence.ClientServiceFactory;
import vitalcenter.osgi.persistence.models.Client;

import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.ui.swing.DPFPVerificationControl;
import com.digitalpersona.onetouch.ui.swing.DPFPVerificationEvent;
import com.digitalpersona.onetouch.ui.swing.DPFPVerificationListener;
import com.digitalpersona.onetouch.ui.swing.DPFPVerificationVetoException;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;

public class DlgHuellasBikes extends JDialog {
	private static final long serialVersionUID = 1L;
	private DPFPVerificationControl verificationControl;
	private JTextField txtRutNombre;
	private JTextField txtEstado;
	private JPanel panel;
	private JButton btnAceptar;
	private JButton btnCancelar;
	
	private List<Client> listTemplates;
	private Client client;

	/**
	 * Create the dialog.
	 */
	public DlgHuellasBikes() {
		setTitle("Coloque la huella");
		setType(Type.UTILITY);
		setBounds(100, 100, 450, 204);
		getContentPane().setLayout(new MigLayout("", "[][][grow]", "[30.00][15.00][36.00][][35.00]"));
		getContentPane().add(getVerificationControl(), "cell 0 0 1 3,growx,aligny center");
		getContentPane().add(getTxtRutNombre(), "cell 2 0,grow");
		getContentPane().add(getTxtEstado(), "cell 2 2,grow");
		getContentPane().add(getPanel(), "cell 0 4 3 1,grow");
		listTemplates = ClientServiceFactory.getInstance().getTemplates();
	}

	private DPFPVerificationControl getVerificationControl() {
		if (verificationControl == null) {
			verificationControl = new DPFPVerificationControl();
			verificationControl.addVerificationListener(new DPFPVerificationListener() {
				public void captureCompleted(DPFPVerificationEvent e) throws DPFPVerificationVetoException {
					e.setStopCapture(false);
					boolean result = validate(e.getFeatureSet());
					e.setMatched(result);
				}
			});
		}
		return verificationControl;
	}
	private JTextField getTxtRutNombre() {
		if (txtRutNombre == null) {
			txtRutNombre = new JTextField();
			txtRutNombre.setEditable(false);
			txtRutNombre.setColumns(10);
		}
		return txtRutNombre;
	}
	private JTextField getTxtEstado() {
		if (txtEstado == null) {
			txtEstado = new JTextField();
			txtEstado.setEditable(false);
			txtEstado.setColumns(10);
		}
		return txtEstado;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			panel.add(getBtnAceptar());
			panel.add(getBtnCancelar());
		}
		return panel;
	}
	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					firePropertyChange(PnlBike.PROP_CLIENTE, null, client);
					DlgHuellasBikes.this.setVisible(false);
				}
			});
		}
		return btnAceptar;
	}
	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					DlgHuellasBikes.this.setVisible(false);
				}
			});
		}
		return btnCancelar;
	}
	
	/**
	 * Valida contra la base de datos de clientes.
	 * 
	 * @param featureSet
	 *            La huella en si misma.
	 */
	public boolean validate(DPFPFeatureSet featureSet) {
		Client cliente = null;
		boolean isValid = false;
		final DPFPVerification verification = DPFPGlobal.getVerificationFactory().createVerification(
				DPFPVerification.MEDIUM_SECURITY_FAR);
		DPFPVerificationResult result = null;
		for (Client lClient : listTemplates) {
			DPFPTemplate template = DPFPGlobal.getTemplateFactory().createTemplate();
			template.deserialize(lClient.getTemplate().getTemplate());
			result = verification.verify(featureSet, template);
			if (result.isVerified()) {
				cliente = ClientServiceFactory.getInstance().getClient(lClient.getRut());
				cliente.setTemplate(lClient.getTemplate());

				txtRutNombre.setText(String.format("%s %s", cliente.getRut(), cliente.getName()));
				isValid = true;
				if (cliente.getExpiration().before(new Date())) {
					isValid = false;
					txtEstado.setText(String.format("Contrato caducado en:%s", cliente.getExpiration().toString()));
					getBtnAceptar().setEnabled(false);
				}
				else
				{
					txtEstado.setText(String.format("Contrato vigente hasta :%s", cliente.getExpiration().toString()));
					getBtnAceptar().setEnabled(true);
					client = lClient;
				}
				
				break;
			}
			else
			{
				txtRutNombre.setText("Huella no encotrada");
				txtEstado.setText("");
			}
		}
		return isValid;
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			verificationControl.start();
		}
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
