import java.util.ArrayList;

public class WumpusWorld {

	public final int WORLD_SIZE = 4;

	private ArrayList<Cell> world = new ArrayList<>(WORLD_SIZE * WORLD_SIZE);

	public WumpusWorld() {
		//TODO initialize world
	}

	public static void main(String[] args) {

	}

	public Cell getCellByCoordinates(int row, int col){
		return world.stream()
				.filter(cell -> cell.getCol() == col && cell.getRow() == row)
				.findFirst()
				.orElse(null);
	}

	public Cell getCellWithWumpus(){
		return world.stream()
				.filter(Cell::isWumpusPresent)
				.findFirst()
				.orElse(null);
	}

	public void printToConsole(){
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < WORLD_SIZE; row++){//rows
			for (int col = 0; col < WORLD_SIZE; col++){//cols
				//TODO...
			}
		}
	}
}
