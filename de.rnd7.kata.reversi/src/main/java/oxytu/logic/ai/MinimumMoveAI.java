package oxytu.logic.ai;

import java.util.List;
import java.util.TreeMap;

import de.rnd7.kata.reversi.logic.GameLogic;
import de.rnd7.kata.reversi.logic.ai.AIUtils;
import de.rnd7.kata.reversi.logic.ai.ReversiAI;
import de.rnd7.kata.reversi.model.Cell;
import de.rnd7.kata.reversi.model.CellState;
import de.rnd7.kata.reversi.model.Coordinate;
import de.rnd7.kata.reversi.model.Direction;
import de.rnd7.kata.reversi.model.GameField;
import de.rnd7.kata.reversi.model.GameUtils;

public class MinimumMoveAI implements ReversiAI {
	

	@Override
	public Coordinate getMove(GameField field, CellState player, List<Coordinate> possibleMoves) {
		TreeMap<Long, Coordinate> rankedCoordinates = new TreeMap<>();
		for (Coordinate c : possibleMoves) {
			int penalty = 0;
			GameField newField = applyMove(field, player, field.getCell(c));
			long changedFields = compare(player, field, newField);
			if (isPreCorner(c)) {
				penalty += 30;
			} else if (isEdge(c)) {
				penalty -= 10;
			} else if (isCorner(c)) {
				penalty -= 0;
			}
			rankedCoordinates.put(Math.min(changedFields + penalty, 0), c);
		}
		return rankedCoordinates.lastEntry().getValue();
	}
	
	private boolean isPreCorner(Coordinate c) {
		return (c.getX() == 1 && c.getY() == 1)
				|| (c.getX() == 1 && c.getY() == GameUtils.COLS - 2)
				|| (c.getY() == 1 && c.getX() == GameUtils.COLS - 2)
				|| (c.getY() == GameUtils.COLS - 2 && c.getX() == GameUtils.COLS - 2);
	}
	
	private boolean isEdge(Coordinate c) {
		return countNeighbors(c) == 5;
	}
	
	private boolean isCorner(Coordinate c) {
		return countNeighbors(c) == 3;
	}

	private int countNeighbors(Coordinate c) {
		int i = 0;
		for (Direction d : Direction.values()) {
			if (c.hasNeighbour(d)) {
				i++;
			}
		}
		return i;
	}

	private long compare(CellState currentPlayer, GameField field, GameField newField) {
		long oldCount = countFieldsForPlayer(currentPlayer, field);
		long newCount = countFieldsForPlayer(currentPlayer, newField);
		return newCount - oldCount;
	}


	private long countFieldsForPlayer(CellState currentPlayer, GameField field) {
		return field.getCells().stream().filter(x -> x.getState().equals(currentPlayer)).count();
	}


	private GameField applyMove(final GameField node, final CellState player, final Cell cell) {
		final GameField child = AIUtils.cloneField(node);
		final GameLogic gameLogic = new GameLogic(child);
		gameLogic.apply(player, cell, child);
		return child;
	}
}
