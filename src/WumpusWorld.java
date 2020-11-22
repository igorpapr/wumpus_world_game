import java.util.ArrayList;
import java.util.Random;

public class WumpusWorld {

	public static final int WORLD_SIZE = 4;

	private static final double WUMPUS_IN_CELL_CHANCE = (double) 1 / (WORLD_SIZE * WORLD_SIZE - 1);
	private static final double GOLD_IN_CELL_CHANCE = (double) 1 / (WORLD_SIZE * WORLD_SIZE - 1);
	private static final double HOLE_IN_CELL_CHANCE = 0.2;

	private final ArrayList<Cell> world = new ArrayList<>(WORLD_SIZE * WORLD_SIZE);

	private final Agent agent = new Agent();

	private boolean isGameOver;

	private boolean isWumpusAlive;

	private final Random random = new Random();

	public WumpusWorld() {
		random.setSeed(0); //specify some another seed if needed
		isGameOver = false;
		isWumpusAlive = true;
		initializeWorld();
		agent.setCurrentCell(getCellByCoordinates(0, 0));
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
				System.out.println("Agent has died from the Wumpus. Game over...");
				System.out.println("Current cell: " + cell);
				return;
			}
			if (cell.isHolePresent()){
				isGameOver = true;
				System.out.println("Agent has fallen into the hole and died. Game over...");
				System.out.println("Current cell: " + cell);
				//return
			}
		}else{
			System.out.println("An attempt to go into the wall. Target cell (row;col): " + targetRow + ';' + targetCol);
		}
	}

	public void agentTryGrabTheGold(){
		if (agent.getCurrentCell().isGoldPresent()){
			System.out.println("Agent found the gold and took it. Congratulations! You have won!");
			setGameOver(true);
		}else{
			System.out.println("Unsuccessful attempt to grab the gold. There is nothing underneath the agent.");
		}
	}

	private void initializeWorld(){
		//generating cells
		for (int row = 0; row < WORLD_SIZE; row++){
			for (int col = 0; col < WORLD_SIZE; col++){
				world.add(new Cell(row, col));
			}
		}
		generateProps();
	}

	/**
	 * Generates Wumpus, golds and holes
	 */
	private void generateProps(){
		boolean isWumpusSet = false;
		boolean isGoldSet = false;
		for (Cell cell: world){
			if (cell.getRow() == 0 && cell.getCol() == 0)
				continue;
			double val = random.nextDouble();
			if (val < WUMPUS_IN_CELL_CHANCE && !isWumpusSet){ //wumpus
				cell.setWumpusPresent(true);
				setSmellAround(cell.getRow(), cell.getCol());
				isWumpusSet = true;
			}
			val = random.nextDouble();
			if (val < GOLD_IN_CELL_CHANCE && !isGoldSet){ //gold
				cell.setGoldPresent(true);
				isGoldSet = true;
			}
			val = random.nextDouble();
			if (val < HOLE_IN_CELL_CHANCE){
				cell.setHolePresent(true);
				setWindAround(cell.getRow(), cell.getCol());
			}
		}
	}

	private void setSmellAround(int row, int col){
		setSmellToCell(row + 1, col); //top
		setSmellToCell(row, col + 1); //right
		setSmellToCell(row - 1, col); //down
		setSmellToCell(row, col - 1); //left
	}

	private void setSmellToCell(int row, int col){
		Cell cell = getCellByCoordinates(row, col);
		if (cell != null){
			cell.setSmellPresent(true);
		}
	}

	private void setWindAround(int row, int col){
		setWindToCell(row + 1, col); //top
		setWindToCell(row, col + 1); //right
		setWindToCell(row - 1, col); //down
		setWindToCell(row, col - 1); //left
	}

	private void setWindToCell(int row, int col){
		Cell cell = getCellByCoordinates(row, col);
		if (cell != null){
			cell.setWindPresent(true);
		}
	}

	public void printStateToConsole(){
		StringBuilder str = new StringBuilder();
		str.append("[W - wupmus|G - gold|X - agent|H - hole|S - smell|I - wind]\n");
		str.append("Wupmus is ").append(isWumpusAlive?"alive":"dead").append('\n');
		for (int row = WORLD_SIZE - 1; row > 0; row--){//rows
			drawRowDelimiter(str);
			str.append('|');
			for (int col = 0; col < WORLD_SIZE; col++){//cols
				Cell cell = getCellByCoordinates(row, col);
				if (agent.getCurrentCell().equals(cell))
					str.append('X');
				if (cell.isWumpusPresent())
					str.append('W');
				if(cell.isGoldPresent())
					str.append('G');
				if(cell.isHolePresent())
					str.append('H');
				if(cell.isSmellPresent())
					str.append('S');
				if(cell.isWindPresent())
					str.append('I');
				str.append('\t').append('|');
			}
		}
		drawRowDelimiter(str);
		System.out.println(str);
	}

	private void drawRowDelimiter(StringBuilder str){
		str.append(' ');
		for (int i = 0; i < 7 * WORLD_SIZE; i++){
			str.append("—");
		}
		str.append('\n');
	}

	public void agentShoot(){
		switch (agent.getDirection()){
			case UP:
				agentShootCol(1);
				break;
			case LEFT:
				agentShootRow(-1);
				break;
			case DOWN:
				agentShootCol(-1);
				break;
			case RIGHT:
				agentShootRow(1);
				break;
		}
		agent.setHasArrow(false);
	}

	/**
	 * Agent shoots with his bow in the given direction of current row
	 * @param qualifier represents the direction of shooting.
	 *                  The negative value of qualifier is considered as LEFT direction
	 *                  The positive value of qualifier is considered as RIGHT direction
	 */
	private void agentShootRow(int qualifier){
		int sign = Integer.signum(qualifier);
		if (sign == 0) throw new RuntimeException("Non-zero value of qualifier expected");
		int currentRow = agent.getCurrentCell().getCol();
		for (int i = agent.getCurrentCell().getCol(); i >= 0 && i < WORLD_SIZE ; i += sign){
			if (getCellByCoordinates(currentRow, i).isWumpusPresent()){
				isWumpusAlive = false;
				wumpusLastScream();
			}
		}
	}


	/**
	 * Agent shoots with his bow in the given direction of current column
	 * @param qualifier represents the direction of shooting.
	 *                  The negative value of qualifier is considered as DOWN direction
	 *                  The positive value of qualifier is considered as UP direction
	 */
	private void agentShootCol(int qualifier){
		int sign = Integer.signum(qualifier);
		if (sign == 0) throw new RuntimeException("Non-zero value of qualifier expected");
		int currentCol = agent.getCurrentCell().getCol();
		for (int i = agent.getCurrentCell().getRow(); i >= 0 && i < WORLD_SIZE ; i += sign){
			if (getCellByCoordinates(i, currentCol).isWumpusPresent()){
				isWumpusAlive = false;
				wumpusLastScream();
			}
		}
	}

	private void wumpusLastScream(){
		agent.setKnowsWumpusAlive(false);
		//TODO maybe connect to KnowledgeBase here
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
