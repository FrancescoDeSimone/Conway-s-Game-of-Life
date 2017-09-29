import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame finestra = new Gol(683 / 2, 384 / 2); // the costructor Gol(int numbCellsX, numbCellsY)
		finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		finestra.setVisible(true);
	}

}
