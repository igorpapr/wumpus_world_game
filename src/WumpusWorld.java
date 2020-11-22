import java.util.ArrayList;

public class WumpusWorld {

	public final int WORLD_SIZE = 4;

	private final ArrayList<Cell> world = new ArrayList<>(WORLD_SIZE * WORLD_SIZE);

	private final Agent agent = new Agent();

	private boolean isGameOver;

	private boolean isWumpusAlive;

	public WumpusWorld() {
		isGameOver = false;
		//TODO initialize world
	}

	public static void main(String[] args) {
		WumpusWorld world = new WumpusWorld();
		world.startGame();
	}

	public void startGame(){
		while(!isGameOver){
			//think with the KnowledgeBase
			//make moves or shoot or grab the gold
			//tell things to the KnowledgeBase
			printStateToConsole();
		}
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

	public void agentTryGoForward(){
		int targetRow = agent.getCurrentCell().getRow();
		int targetCol = agent.getCurrentCell().getCol();
		switch (agent.getDirection()){
			case UP:
				targetCol += 1;
				break;
			case DOWN:
				targetRow -= 1;
				break;
			case LEFT:
				targetCol -= 1;
				break;
			case RIGHT:
				targetCol =+ 1;
				break;
		}
		Cell cell = getCellByCoordinates(targetRow, targetCol);
		if (cell != null){
			agent.setCurrentCell(cell);
			if (cell.isWumpusPresent() && isWumpusAlive){
				isGameOver = true;
				System.err.println("Agent has died from the Wumpus. Game over...");
				System.out.println("Current cell: " + cell);
				return;
			}
			if (cell.isHolePresent()){
				isGameOver = true;
				System.err.println("Agent has fallen into the hole and died. Game over...");
				System.out.println("Current cell: " + cell);
				//return
			}
		}else{
			System.err.println("An attempt to go into the wall. Target cell (row;col): " + targetRow + ';' + targetCol);
		}
	}

	public void printStateToConsole(){
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < WORLD_SIZE; row++){//rows

			for (int col = 0; col < WORLD_SIZE; col++){//cols
				//TODO...
			}
		}
	}

	public int getWORLD_SIZE() {
		return WORLD_SIZE;
	}

	public ArrayList<Cell> getWorld() {
		return world;
	}

	public Agent getAgent() {
		return agent;
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public void setGameOver(boolean gameOver) {
		isGameOver = gameOver;
	}

	public boolean isWumpusAlive() {
		return isWumpusAlive;
	}

	public void setWumpusAlive(boolean wumpusAlive) {
		isWumpusAlive = wumpusAlive;
	}
}
