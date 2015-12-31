package de.rnd7.kata.reversi.logic;

import org.junit.Assert;
import org.junit.Test;

import de.rnd7.kata.reversi.model.Cell;
import de.rnd7.kata.reversi.model.CellState;
import de.rnd7.kata.reversi.model.Coordinate;
import de.rnd7.kata.reversi.model.GameField;

public class GameLogicTest {
	@Test
	public void testNoMovePossible() throws Exception {
		final GameField gameField = new GameField();

		gameField.getCell(new Coordinate(3, 3)).setState(CellState.BLACK);
		// gameField.getCell(new Coordinate(3, 4)).setState(CellState.WHITE);
		// gameField.getCell(new Coordinate(4, 3)).setState(CellState.WHITE);
		// gameField.getCell(new Coordinate(4, 4)).setState(CellState.BLACK);

		final GameLogic gameLogic = new GameLogic(gameField);
		for (final Cell cell : gameField.getCells()) {
			if (gameLogic.isValidMove(CellState.BLACK, cell)) {
				Assert.fail();
			}
			if (gameLogic.isValidMove(CellState.WHITE, cell)) {
				Assert.fail();
			}
		}
	}
}
