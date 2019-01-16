package states;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class LoadingScreen extends State {
	private Game gm;

	public LoadingScreen() {
		init();
	}

	public void init() {
		initComponents();
	}

	public Game loadGame() {
		gm = new Game();
		gm.init();
		return gm;
	}

	// Initializing what appears on screen
	public void initComponents() {
		setBackground(Color.DARK_GRAY);

		setLayout(new BorderLayout());

		JLabel text = new JLabel("\t\tLOADING...");
		text.setFont(new Font(Font.SERIF, Font.ITALIC, 50));
		text.setForeground(Color.LIGHT_GRAY);
		add(text, BorderLayout.CENTER);
		
		// For the actual loading bar, Maybe use a flow layout jpanel in the
		// center?
	}
}