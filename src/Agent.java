public class Agent {
	private Cell currentCell;

	private AgentDirection direction;

	private boolean hasArrow;

	private boolean knowsWumpusAlive;

	public Cell getCurrentCell() {
		return currentCell;
	}

	public void setCurrentCell(Cell currentCell) {
		this.currentCell = currentCell;
	}

	public AgentDirection getDirection() {
		return direction;
	}

	public void setDirection(AgentDirection direction) {
		this.direction = direction;
	}

	public boolean isHasArrow() {
		return hasArrow;
	}

	public void setHasArrow(boolean hasArrow) {
		this.hasArrow = hasArrow;
	}

	public boolean isKnowsWumpusAlive() {
		return knowsWumpusAlive;
	}

	public void setKnowsWumpusAlive(boolean knowsWumpusAlive) {
		this.knowsWumpusAlive = knowsWumpusAlive;
	}
}
