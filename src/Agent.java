import logic.LogicalExpression;

import java.util.Collections;

import static logic.LogicalExpression.And;

public class Agent {
    private Cell currentCell;

    private AgentDirection direction = AgentDirection.RIGHT;

    private boolean hasArrow = true;

    private boolean knowsWumpusAlive = false;

    private final LogicalExpression KB = And(Collections.emptyList());

    public void tell(LogicalExpression expr) {
    	KB.addChildren(expr);
	}

	public boolean ask(LogicalExpression alpha) {
        return LogicalExpression.ttEntails(KB, alpha);
	}

    public void turnRight() {
        turn(true);
    }

    public void turnLeft() {
        turn(false);
    }

    public void turn(boolean clockwise) {
        switch (this.direction) {
            case RIGHT:
                if (clockwise) {
                    setDirection(AgentDirection.DOWN);
                } else {
                    setDirection(AgentDirection.UP);
                }
                break;
            case DOWN:
                if (clockwise) {
                    setDirection(AgentDirection.LEFT);
                } else {
                    setDirection(AgentDirection.RIGHT);
                }
                break;
            case UP:
                if (clockwise) {
                    setDirection(AgentDirection.RIGHT);
                } else {
                    setDirection(AgentDirection.LEFT);
                }
                break;
            case LEFT:
                if (clockwise) {
                    setDirection(AgentDirection.UP);
                } else {
                    setDirection(AgentDirection.DOWN);
                }
                break;
        }
    }

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
