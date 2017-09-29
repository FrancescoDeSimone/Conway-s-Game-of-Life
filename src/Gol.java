import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

public class Gol extends JFrame {
	public Gol(int x, int y) {
		super("Conway's Game Of Life");
		this.c = y;
		this.r = x;
		this.setLayout(new BorderLayout());
		JPanel finestra = new JPanel();
		finestra.setLayout(new BorderLayout());
		cells = new Grid(java.awt.Toolkit.getDefaultToolkit().getScreenSize(), r, c);
		numCells.setText("cells:" + (r * c));
		GOLpanel = new JPanel(new BorderLayout());
		CONTROLpanel = new JPanel();
		finestra.add(CONTROLpanel, BorderLayout.NORTH);
		finestra.add(GOLpanel, BorderLayout.CENTER);
		GOLpanel.add(cells);

		buttons = new JButton("PLAY");
		CONTROLpanel.add(buttons);
		buttons.addActionListener(new ControlButton());

		buttons = new JButton("STOP");
		CONTROLpanel.add(buttons);
		buttons.addActionListener(new ControlButton());

		buttons = new JButton("CLEAR");
		CONTROLpanel.add(buttons);
		buttons.addActionListener(new ControlButton());

		buttons = new JButton("STEP");
		CONTROLpanel.add(buttons);
		buttons.addActionListener(new ControlButton());

		buttons = new JButton("RANDOM");
		CONTROLpanel.add(buttons);
		buttons.addActionListener(new ControlButton());

		toggleGrid = new JToggleButton("GRID");
		toggleGrid.setSelected(true);
		CONTROLpanel.add(toggleGrid);
		toggleGrid.addActionListener(new ControlToggle());

		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu carica = new JMenu("Load");

		boolean success = new File("./Patterns").mkdirs();
		if (!success) {
			for (File f : new File("./Patterns").listFiles()) {
				JMenuItem tmp = new JMenuItem(f.getName());
				tmp.addActionListener(new CaricaPattern());
				carica.add(tmp);
			}

			JMenuItem save = new JMenuItem("Save");
			save.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String nomeFile;
					boolean flag = false;
					do {
						nomeFile = JOptionPane.showInputDialog(
								flag ? "Nome gia' inserito!" + '\n' + "Inserire nome" : "Inserire nome");
						flag = false;
						for (File f : new File("./Patterns").listFiles())
							if ((f.getName().equals(nomeFile)))
								flag = true;
					} while (flag);
					if (nomeFile != null) {
						try {
							cells.saveToFile("./Patterns/" + nomeFile);
							JMenuItem tmp = new JMenuItem(nomeFile);
							tmp.addActionListener(new CaricaPattern());
							carica.add(tmp);
						} catch (HeadlessException | IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			file.add(carica);
			file.add(save);
			menuBar.add(file);
			this.setJMenuBar(menuBar);
		}
		this.add(finestra);
		CONTROLpanel.add(velocity);
		CONTROLpanel.add(offset);
		CONTROLpanel.add(numCells);
		offset.addChangeListener(new OffsetModifierMouse());
	}

	private class CaricaPattern implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
			try {
				cells.loadFromFile(new File("./Patterns/" + item.getText()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void step() {
		cells.GOLplay();
	}

	private class ControlButton implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			switch (((JButton) arg0.getSource()).getText()) {
			case "STEP":
				step();
				break;
			case "PLAY":
				timer = new Timer();
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						step();
						try {
							Thread.sleep(velocity.getValue());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				timer.schedule(task, 0, 1);
				break;
			case "RANDOM":
				cells.randomCells();
				break;
			case "CLEAR":
				cells.clear();
			case "STOP":
				timer.cancel();
				break;
			}
		}
	}

	private class ControlToggle implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			cells.showGrid(((JToggleButton) arg0.getSource()).isSelected());
		}
	}

	private void SetSliderValue(int valueSlider) {
		int r = this.r / (valueSlider);
		int c = this.c / (valueSlider);
		numCells.setText("cells:" + (r * c));
		cells.zoom(r, c);
	}

	private class OffsetModifierMouse implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			SetSliderValue(((JSlider) e.getSource()).getValue());
		}

	}

	private int r, c;
	private Grid cells;
	private Timer timer;
	private JButton buttons;
	private JToggleButton toggleGrid;
	private JPanel CONTROLpanel, GOLpanel;
	private JSlider velocity = new JSlider(125, 1000);
	private JSlider offset = new JSlider(1, 8, 1);
	private JLabel numCells = new JLabel();
}
