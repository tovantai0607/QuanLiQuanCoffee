package Main;

import javax.swing.SwingUtilities;

import GUI.MainFrame;

public class Application {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new MainFrame().setVisible(true);
		});
	}
}
