import java.util.Objects;

public class Cell {

	private final int row;

	private final int col;

	private boolean isWumpusPresent;

	private boolean isGoldPresent;

	private boolean isHolePresent;

	private boolean isSmellPresent;

	private boolean isWindPresent;

	public Cell(int row, int col) {
		this.row = row;
		this.col = col;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cell cell = (Cell) o;
		return row == cell.row &&
				col == cell.col;
	}

	@Override
	public int hashCode() {
		return 13 * Objects.hash(row, col);
	}

	public boolean isWumpusPresent() {
		return isWumpusPresent;
	}

	public void setWumpusPresent(boolean wumpusPresent) {
		isWumpusPresent = wumpusPresent;
	}

	public boolean isGoldPresent() {
		return isGoldPresent;
	}

	public void setGoldPresent(boolean goldPresent) {
		isGoldPresent = goldPresent;
	}

	public boolean isHolePresent() {
		return isHolePresent;
	}

	public void setHolePresent(boolean holePresent) {
		isHolePresent = holePresent;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public boolean isSmellPresent() {
		return isSmellPresent;
	}

	public void setSmellPresent(boolean smellPresent) {
		isSmellPresent = smellPresent;
	}

	public boolean isWindPresent() {
		return isWindPresent;
	}

	public void setWindPresent(boolean windPresent) {
		isWindPresent = windPresent;
	}

	@Override
	public String toString() {
		return "Cell [" + row +
				";" + col +
				"]. Wumpus=" + isWumpusPresent +
				", Gold=" + isGoldPresent +
				", Hole=" + isHolePresent +
				", Smell=" + isSmellPresent +
				", Wind=" + isWindPresent;
	}
}
