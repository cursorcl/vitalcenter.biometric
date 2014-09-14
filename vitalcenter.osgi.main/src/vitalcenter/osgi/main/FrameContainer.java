package vitalcenter.osgi.main;

import javax.swing.JFrame;

public class FrameContainer extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the frame.
	 */
	public FrameContainer() {
		setName("frmContainer");
		setTitle("Vitalcenter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setContentPane(new MainContainer());
	}

}
