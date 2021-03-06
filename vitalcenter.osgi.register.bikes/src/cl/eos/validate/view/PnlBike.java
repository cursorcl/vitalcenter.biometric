package cl.eos.validate.view;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.SwingConstants;

import vitalcenter.osgi.persistence.models.Client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PnlBike extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final String PROP_CLIENTE = "PROP_CLIENTE";
	private JToggleButton btnRegister;
	private JLabel lblName;

	private Client client;
	private String number;
	private String rut;
	private static final DlgHuellasBikes dlgPideHuella = new DlgHuellasBikes();

	/**
	 * Create the panel.
	 */
	public PnlBike() {
		setSize(new Dimension(167, 94));
		setPreferredSize(new Dimension(121, 65));
		setMinimumSize(new Dimension(167, 94));
		setMaximumSize(new Dimension(167, 94));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								Alignment.TRAILING,
								groupLayout
										.createSequentialGroup()
										.addGap(4)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(
																getLblName(),
																Alignment.LEADING,
																GroupLayout.DEFAULT_SIZE,
																113,
																Short.MAX_VALUE)
														.addComponent(
																getBtnRegister(),
																GroupLayout.DEFAULT_SIZE,
																99,
																Short.MAX_VALUE))
										.addGap(4)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addGap(4)
						.addComponent(getBtnRegister(),
								GroupLayout.PREFERRED_SIZE, 33,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(getLblName())
						.addContainerGap(GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));
		setLayout(groupLayout);

	}

	private JToggleButton getBtnRegister() {
		if (btnRegister == null) {
			btnRegister = new JToggleButton("1");
			btnRegister.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					Point p = new Point(btnRegister.getLocation());

					SwingUtilities.convertPointToScreen(p, PnlBike.this);
					dlgPideHuella.setLocation(p);
					dlgPideHuella.setVisible(true);
					dlgPideHuella.addPropertyChangeListener(PROP_CLIENTE,
							new PropertyChangeListener() {
								@Override
								public void propertyChange(
										PropertyChangeEvent evt) {
									if (evt.getNewValue() != null
											&& evt.getNewValue() instanceof Client) {
										client = (Client)evt.getNewValue();
										setClient(client);
									}
								}
							});

				}
			});
		}
		return btnRegister;
	}

	private JLabel getLblName() {
		if (lblName == null) {
			lblName = new JLabel("Sin asignar");
			lblName.setFont(new Font("Tahoma", Font.PLAIN, 9));
			lblName.setHorizontalAlignment(SwingConstants.CENTER);
			lblName.setHorizontalTextPosition(SwingConstants.CENTER);
			lblName.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
		}
		return lblName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
		btnRegister.setText(number);
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
		lblName.setText(client.getName());
	}

	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

}
