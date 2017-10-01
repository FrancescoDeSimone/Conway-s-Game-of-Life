import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JComponent;

public class Grid extends JComponent {
	private static final long serialVersionUID = 3718432610344631189L;

	public Grid(int widthTable, int heightTable, int x, int y) {
		this.x = this.xZoom = x;
		this.y = this.yZoom = y;
		cells = new Cell[x][y];
		this.widthTable = widthTable;
		this.heightTable = heightTable;
		this.addMouseListener(new ChangeColor());
		widthCells = widthTable / x;
		heightCells = heightTable / y;
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++){
				ArrayList<Point> neighbour = new ArrayList<>();
				if ((i + 1) < x)
					neighbour.add(new Point(i + 1,j));
				if ((i - 1) >= 0)
					neighbour.add(new Point(i - 1,j));
				if ((j + 1) < y)
					neighbour.add(new Point(i,j + 1));
				if ((j - 1) >= 0)
					neighbour.add(new Point(i,j - 1));
				if ((i + 1) < x && (j + 1) < y)
					neighbour.add(new Point(i + 1,j + 1));
				if ((i + 1) < x && (j - 1) >= 0)
					neighbour.add(new Point(i +1,j - 1));
				if ((i - 1) >= 0 && (j + 1) < y)
					neighbour.add(new Point(i - 1,j + 1));
				if ((i - 1) >= 0 && (j - 1) >= 0)
					neighbour.add(new Point(i - 1,j - 1));
				
				cells[i][j] = new Cell(widthCells * i, heightCells * j, widthCells, heightCells, neighbour);
			}
	}

	public Grid(Dimension d, int x, int y) {
		this((int) d.getWidth(), (int) d.getHeight(), x, y);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for (int i = 0; i < xZoom; i++)
			for (int j = 0; j < yZoom; j++) {
				if (cells[i + (x - xZoom + moveX) / 2][j + (y - yZoom + moveY) / 2].isStatus()) {
					g2.setColor(Color.green);
					g2.fill(cells[i][j]);
				} else {
					g2.setColor(Color.black);
					g2.fill(cells[i][j]);
				}
				if (toggleGriglia) {
					g2.setColor(Color.DARK_GRAY);
					g2.draw(cells[i][j]);
				}
			}
	}

	public void randomCells() {
		Random rand = new Random();
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++)
				if (rand.nextBoolean())
					cells[i][j].changeCell();
		repaint();
	}

	public void loadFromFile(File f) throws IOException {
		clear();
		InputStream in = new FileInputStream(f);
		int c;
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++)
				if ((c = in.read()) != -1)
					if (c == '1')
						cells[i][j].setStatus(true);
					else
						cells[i][j].setStatus(false);
		in.close();
		repaint();
	}

	public void saveToFile(String name) throws IOException {
		File file = new File(name);
		file.createNewFile();
		OutputStream out = new FileOutputStream(file);
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++) {
				out.write(cells[i][j].isStatus() ? '1' : '0');
			}
		out.close();
	}

	public void showGrid(boolean trueORfalse) {
		toggleGriglia = trueORfalse;
		repaint();
	}

	public void clear() {
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++)
				cells[i][j].setStatus(false);
		repaint();
	}

	private class ChangeColor extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			int i, j;
			i = (arg0.getX() / widthCells) + (x - xZoom + moveX) / 2;
			j = (arg0.getY() / heightCells) + (y - yZoom + moveY) / 2;
			if (i < x && j < y)
				cells[i][j].changeCell();
			repaint();
		}
	}

	public void GOLplay() {
		int r = x;
		int c = y;
		boolean[][] tmpcelle = new boolean[r][c];
		for (int i = 0; i < r; i++)	
			for (int j = 0; j < c; j++) {
				int cont = 0;
				
				for(Point cellNeighboursPosition:cells[i][j].getNeighboursPoint())
					if(cells[cellNeighboursPosition.x][cellNeighboursPosition.y].isStatus())
						cont++;
				
					if (cont == 2)
						tmpcelle[i][j] = cells[i][j].isStatus();
					else if (cont == 3)
						tmpcelle[i][j] = true;
					else
						tmpcelle[i][j] = false;
			}

		for (int i = 0; i < r; i++)	
			for (int j = 0; j < c; j++) {
				if(cells[i][j].isStatus() != tmpcelle[i][j])
					cells[i][j].changeCell();
			}
		repaint();
	}
	
	public void zoom(int newX, int newY) {
		xZoom = newX;
		yZoom = newY;
		moveX=moveY=0;
		widthCells = widthTable / newX;
		heightCells = heightTable / newY;
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++)
				cells[i][j].changeDimension(widthCells, heightCells);
		repaint();
	}
	
	public static final short UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;

	public void move(short directions) {
		boolean isMoving = true;
		switch (directions) {
		case UP:
			if ((y - yZoom + moveY)/2 > 0)
				moveY--;
			break;
		case DOWN:
			if ((y - yZoom + moveY)/2 < y-yZoom)
				moveY++;
			break;
		case LEFT:
			if ((x - xZoom + moveX)/2 > 0)
				moveX--;
			break;
		case RIGHT:
			if ((x - xZoom + moveX)/2 < x-xZoom){
				moveX++;
			}
			break;
		default:
			isMoving = false;
			break;
		}
		if (isMoving)
			repaint();
	}
	
	Cell[][] cells;
	private int widthCells, heightCells, widthTable, heightTable, x, y, xZoom, yZoom, moveX, moveY;
	private boolean toggleGriglia = true;
}
