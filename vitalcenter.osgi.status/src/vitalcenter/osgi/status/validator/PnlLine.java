package vitalcenter.osgi.status.validator;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import vitalcenter.osgi.status.api.IStatusValidator;
import net.miginfocom.swing.MigLayout;
import java.awt.Dimension;

public class PnlLine extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public PnlLine(String title) {
		setPreferredSize(new Dimension(190, 70));
		setMinimumSize(new Dimension(190, 70));
		setMaximumSize(new Dimension(190, 70));
		setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new MigLayout("", "[39.00][12.00][grow]", "[28.00][43.00]"));
		
		JLabel lblTitle = new JLabel(title);
		lblTitle.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		lblTitle.setForeground(Color.BLUE);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblTitle, "cell 0 0 3 1,growx");
		
		JLabel lblIconStatus = new JLabel("");
		lblIconStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblIconStatus.setIcon(new ImageIcon(PnlLine.class.getResource("/com/sun/java/swing/plaf/windows/icons/Error.gif")));
		add(lblIconStatus, "cell 0 1");
		
		JLabel lblTextStatus = new JLabel("Status");
		lblTextStatus.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		add(lblTextStatus, "cell 2 1,growx");
	}

	public void setValidator(IStatusValidator validator)
	{
		
	}
}
