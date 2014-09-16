package cl.eos.enroll.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import vitalcenter.osgi.persistence.models.Client;
import vitalcenter.osgi.persistence.models.IClientService;
import vitalcenter.osgi.persistence.models.TemplateInfo;
import cl.eos.enroll.controller.EnrollController;
import cl.eos.enroll.validator.DPValidator;
import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.status.api.EStatus;
import cl.eos.interfaces.status.api.StatusEvent;
import cl.eos.interfaces.view.IViewContainer;

import com.digitalpersona.onetouch.DPFPFingerIndex;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.ui.swing.DPFPEnrollmentControl;
import com.digitalpersona.onetouch.ui.swing.DPFPEnrollmentEvent;
import com.digitalpersona.onetouch.ui.swing.DPFPEnrollmentListener;
import com.digitalpersona.onetouch.ui.swing.DPFPEnrollmentVetoException;

public class PnlEnroll extends AFormView implements IViewContainer {

	private static final long serialVersionUID = 1L;
	private static final String EMPTY = null;
	private DPFPEnrollmentControl enrollmentControl;
	private JLabel lblTagRut;
	private JTextField txtTxtrut;
	private JLabel lblTagName;
	private JLabel lblName;
	private JButton btnSearch;
	private IClientService service;

	private Client activeClient;
	private JButton btnGrabar;
	private JButton btnCancelar;
	protected DPFPTemplate template;
	protected DPFPFingerIndex fingerIndex;
	private JButton btnBorrar;

	private boolean isReaderOk = true;
	private boolean isFounded = false;

	/**
	 * Create the panel.
	 */
	public PnlEnroll() {
		super();
		setTitle("Registrar huellas");
		setPreferredSize(new Dimension(500, 470));
		setSize(new Dimension(500, 470));
		setMaximumSize(new Dimension(500, 470));
		setMinimumSize(new Dimension(500, 470));
		setLayout(new MigLayout("", "[][228.00][95.00][95.00][grow]", "[][][grow][]"));
		add(getLblTagRut(), "cell 0 0,alignx trailing");
		add(getTxtTxtrut(), "cell 1 0,growx");
		add(getBtnSearch(), "cell 3 0,growx");
		add(getLblTagName(), "cell 0 1");
		add(getLblName(), "cell 1 1 2 1,grow");
		add(getBtnBorrar(), "cell 3 1,growx,aligny center");
		add(getEnrollmentControl(), "cell 0 2 4 1,grow");
		add(getBtnGrabar(), "cell 2 3,growx,aligny bottom");
		add(getBtnCancelar(), "cell 3 3,growx");
	}

	private DPFPEnrollmentControl getEnrollmentControl() {
		if (enrollmentControl == null) {
			enrollmentControl = new DPFPEnrollmentControl();
			enrollmentControl.setEnabled(false);
			EnumSet<DPFPFingerIndex> fingers = EnumSet.noneOf(DPFPFingerIndex.class);
			enrollmentControl.setEnrolledFingers(fingers);
			enrollmentControl.setMaxEnrollFingerCount(1);

			enrollmentControl.addEnrollmentListener(new DPFPEnrollmentListener() {
				public void fingerDeleted(DPFPEnrollmentEvent e) throws DPFPEnrollmentVetoException {
					int result = JOptionPane.showConfirmDialog(PnlEnroll.this,
							"Está seguro que quiere eliminar el registro?", "Eliminar registro",
							JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.OK_OPTION) {
						controller.delete(activeClient);
					}
				}

				public void fingerEnrolled(DPFPEnrollmentEvent e) throws DPFPEnrollmentVetoException {
					TemplateInfo template = new TemplateInfo(e.getFingerIndex().ordinal(), e.getTemplate().serialize());
					activeClient.setTemplate(template);
					btnGrabar.setEnabled(true);
				}
			});

		}
		return enrollmentControl;
	}

	private JLabel getLblTagRut() {
		if (lblTagRut == null) {
			lblTagRut = new JLabel("Rut cliente:");
		}
		return lblTagRut;
	}

	private JTextField getTxtTxtrut() {
		if (txtTxtrut == null) {
			txtTxtrut = new JTextField();
			txtTxtrut.setColumns(10);
		}
		return txtTxtrut;
	}

	private JLabel getLblTagName() {
		if (lblTagName == null) {
			lblTagName = new JLabel("Nombre");
		}
		return lblTagName;
	}

	private JLabel getLblName() {
		if (lblName == null) {
			lblName = new JLabel("");
		}
		return lblName;
	}

	private JButton getBtnSearch() {
		if (btnSearch == null) {
			btnSearch = new JButton("Buscar");
			btnSearch.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					String rutNormalizado = normalizaRut(txtTxtrut.getText());
					((EnrollController) controller).findRut(rutNormalizado);
				}
			});
		}
		return btnSearch;
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity != null) {
			activeClient = (Client) entity;
			lblName.setText(activeClient.getName());

			if (activeClient.getTemplate() == null) {
				enrollmentControl.setEnabled(isReaderOk);
				isFounded = true;
			} else {
				lblName.setText("Ya registrado: " + activeClient.getName());
				enrollmentControl.setEnabled(false);
				btnGrabar.setEnabled(false);
				getBtnBorrar().setVisible(true);
				isFounded = false;
			}

		} else {
			isFounded = false;
			activeClient = null;
			lblName.setText("Rut no encontrado");
			enrollmentControl.setEnabled(false);
			btnGrabar.setEnabled(false);

		}
	}

	@Override
	public void onSaved(IEntity otObject) {
		EnumSet<DPFPFingerIndex> fingers = EnumSet.noneOf(DPFPFingerIndex.class);
		lblName.setText(EMPTY);
		txtTxtrut.setText(EMPTY);
		txtTxtrut.requestFocus();
		enrollmentControl.setEnrolledFingers(fingers);
		enrollmentControl.setMaxEnrollFingerCount(1);
	}

	@Override
	public void onDeleted(IEntity otObject) {
		EnumSet<DPFPFingerIndex> fingers = EnumSet.noneOf(DPFPFingerIndex.class);
		lblName.setText(EMPTY);
		txtTxtrut.setText(EMPTY);
		txtTxtrut.requestFocus();
		enrollmentControl.setEnrolledFingers(fingers);
		enrollmentControl.setMaxEnrollFingerCount(1);
	}

	@Override
	public void onChangeStatus(StatusEvent status) {
		if (status.getSource() instanceof DPValidator) {
			EStatus lStatus = (EStatus) status.getStatus();
			isReaderOk = lStatus.equals(EStatus.OK);
			getEnrollmentControl().setEnabled(isReaderOk && isFounded);
			getBtnSearch().setEnabled(isReaderOk);
			getBtnGrabar().setEnabled(isReaderOk && isFounded);
			getBtnCancelar().setEnabled(isReaderOk);
			getTxtTxtrut().setEnabled(isReaderOk);
			if (!isReaderOk) {
				JOptionPane.showMessageDialog(this, "Problemas con el lector de huellas", "Problema",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public IClientService getService() {
		return service;
	}

	public void setService(IClientService service) {
		this.service = service;
	}

	private JButton getBtnGrabar() {
		if (btnGrabar == null) {
			btnGrabar = new JButton("Grabar");
			btnGrabar.setEnabled(false);
			btnGrabar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					btnGrabar.setEnabled(false);
					enrollmentControl.setEnabled(false);
					save(activeClient);
				}
			});
		}
		return btnGrabar;
	}

	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					EnumSet<DPFPFingerIndex> fingers = EnumSet.noneOf(DPFPFingerIndex.class);
					btnGrabar.setEnabled(false);
					lblName.setText(EMPTY);
					txtTxtrut.setText(EMPTY);
					enrollmentControl.setEnabled(false);
					enrollmentControl.setEnrolledFingers(fingers);
					enrollmentControl.setMaxEnrollFingerCount(1);
					getBtnBorrar().setVisible(false);
				}
			});

		}
		return btnCancelar;
	}

	private JButton getBtnBorrar() {
		if (btnBorrar == null) {
			btnBorrar = new JButton("Borrar");
			btnBorrar.setToolTipText("Elimina el registro de huellas digital");
			btnBorrar.setVisible(false);
			btnBorrar.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					int result = JOptionPane.showConfirmDialog(PnlEnroll.this,
							"Está seguro que quiere eliminar el registro?", "Eliminar registro",
							JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.OK_OPTION) {
						controller.delete(activeClient);
						lblName.setText(EMPTY);
						txtTxtrut.setText(EMPTY);
						enrollmentControl.setEnabled(false);
						EnumSet<DPFPFingerIndex> fingers = EnumSet.noneOf(DPFPFingerIndex.class);
						enrollmentControl.setEnrolledFingers(fingers);
						enrollmentControl.setMaxEnrollFingerCount(1);
						getBtnBorrar().setVisible(false);
					}

				}
			});
		}
		return btnBorrar;
	}

	private String normalizaRut(String rut) {
		rut = rut.replace(".", "");
		rut = rut.replace("-", "");
		int n = rut.length() - 1;
		String c = rut.substring(n, n + 1);
		StringBuffer nRut = new StringBuffer();

		nRut.append("-");
		nRut.append(c);

		char[] chars = rut.toCharArray();
		for (n = chars.length - 2; n >= 0; n = n - 3) {
			for (int m = n; m >= n - 2; m--) {
				if (m >= 0) {
					nRut.insert(0, chars[m]);
				}
			}
			if (n >= 3) {
				nRut.insert(0, ".");
			}

		}
		return nRut.toString();
	}

	public static void main(String[] args) {
		String rut = "16.13781-1";
		rut = rut.replace(".", "");
		rut = rut.replace("-", "");
		int n = rut.length() - 1;
		String c = rut.substring(n, n + 1);
		StringBuffer nRut = new StringBuffer();

		nRut.append("-");
		nRut.append(c);

		char[] chars = rut.toCharArray();
		for (n = chars.length - 2; n >= 0; n = n - 3) {
			for (int m = n; m >= n - 2; m--) {
				if (m >= 0) {
					nRut.insert(0, chars[m]);
				}
			}
			if (n >= 3) {
				nRut.insert(0, ".");
			}

		}
		System.out.println(nRut.toString());

	}
}
