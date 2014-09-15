package cl.eos.validate.view;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Font;

public class PnlBike extends JPanel {
	private static final long serialVersionUID = 1L;
	private JToggleButton btnRegister;
	private JLabel lblName;

	private String number;
	private String name;

	/**
	 * Create the panel.
	 */
	public PnlBike() {
		setSize(new Dimension(167, 94));
		setPreferredSize(new Dimension(121, 65));
		setMinimumSize(new Dimension(167, 94));
		setMaximumSize(new Dimension(167, 94));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				Alignment.TRAILING,
				groupLayout
						.createSequentialGroup()
						.addGap(4)
						.addGroup(
								groupLayout
										.createParallelGroup(Alignment.TRAILING)
										.addComponent(getLblName(), Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 113,
												Short.MAX_VALUE)
										.addComponent(getBtnRegister(), GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
						.addGap(4)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
				groupLayout.createSequentialGroup().addGap(4)
						.addComponent(getBtnRegister(), GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(getLblName())
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		setLayout(groupLayout);

	}

	private JToggleButton getBtnRegister() {
		if (btnRegister == null) {
			btnRegister = new JToggleButton("1");
		}
		return btnRegister;
	}

	private JLabel getLblName() {
		if (lblName == null) {
			lblName = new JLabel("Eliecer Osorio Verdugo");
			lblName.setFont(new Font("Tahoma", Font.PLAIN, 9));
			lblName.setHorizontalAlignment(SwingConstants.CENTER);
			lblName.setHorizontalTextPosition(SwingConstants.CENTER);
			lblName.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		lblName.setText(name);
	}

}
