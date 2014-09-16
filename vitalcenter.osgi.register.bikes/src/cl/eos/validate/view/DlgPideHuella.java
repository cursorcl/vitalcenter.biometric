package cl.eos.validate.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Date;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

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

import java.awt.Window.Type;

public class DlgPideHuella extends JDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private DPFPVerificationControl verificationControl;
	private List<Client> listTemplates;
	private JPanel buttonPane;
	private JTextField txtRutNombre;
	private JTextField txtEstado;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DlgPideHuella dialog = new DlgPideHuella();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DlgPideHuella() {
		setTitle("Coloque huella");
		setType(Type.POPUP);
		setAlwaysOnTop(true);
		setBounds(100, 100, 438, 159);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel
				.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(
						gl_contentPanel
								.createSequentialGroup()
								.addComponent(getVerificationControl(), GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(10)
								.addGroup(
										gl_contentPanel
												.createParallelGroup(Alignment.LEADING)
												.addComponent(getTxtRutNombre())
												.addComponent(getTxtEstado(), GroupLayout.DEFAULT_SIZE, 307,
														Short.MAX_VALUE))));
		gl_contentPanel.setVerticalGroup(gl_contentPanel
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						gl_contentPanel
								.createSequentialGroup()
								.addComponent(getVerificationControl(), GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(12))
				.addGroup(
						gl_contentPanel
								.createSequentialGroup()
								.addComponent(getTxtRutNombre(), GroupLayout.PREFERRED_SIZE, 27,
										GroupLayout.PREFERRED_SIZE)
								.addGap(11)
								.addComponent(getTxtEstado(), GroupLayout.PREFERRED_SIZE, 36,
										GroupLayout.PREFERRED_SIZE)));
		contentPanel.setLayout(gl_contentPanel);
		getContentPane().add(getButtonPane(), BorderLayout.SOUTH);
		listTemplates = ClientServiceFactory.getInstance().getTemplates();
	}

	private JPanel getButtonPane() {
		if (buttonPane == null) {
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			JButton okButton = new JButton("Aceptar");
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
			JButton cancelButton = new JButton("Cancelar");
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}
		return buttonPane;
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
				}
				else
				{
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

	private JTextField getTxtRutNombre() {
		if (txtRutNombre == null) {
			txtRutNombre = new JTextField();
			txtRutNombre.setEnabled(false);
			txtRutNombre.setEditable(false);
			txtRutNombre.setColumns(10);
		}
		return txtRutNombre;
	}

	private JTextField getTxtEstado() {
		if (txtEstado == null) {
			txtEstado = new JTextField();
			txtEstado.setEnabled(false);
			txtEstado.setEditable(false);
			txtEstado.setColumns(10);
		}
		return txtEstado;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			verificationControl.start();
		}
	}
}
