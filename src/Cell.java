import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Cell extends Rectangle {
	private static final long serialVersionUID = -5585811967385653488L;
	
	public Cell(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.x = x / width;
		this.y = y / height;
		setStatus(false);
	}

	public void addNeibours(ArrayList<Cell> cells){
		this.neighbours = cells;
	}

	public void changeDimension(int width, int height) {
		this.setBounds(this.x * width, this.y * height, width, height);
	}

	public void changeCell() {
		setStatus(!status);
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public ArrayList<Cell> getNeighbours() {
		return neighbours;
	}
	
	public Point getPosition(){
		return new Point(x,y);
	}

	private boolean status;
	private int x, y;
	private ArrayList<Cell>  neighbours;
}
