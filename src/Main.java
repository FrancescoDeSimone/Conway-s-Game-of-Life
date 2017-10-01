import java.awt.Toolkit;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame finestra = new Gol(Toolkit.getDefaultToolkit().getScreenSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2); // the costructor Gol(int numbCellsX, numbCellsY)
		finestra.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		finestra.setUndecorated(true);
		finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		finestra.setVisible(true);
	}

}
