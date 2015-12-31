/**
 * Copyright 2016 Philipp Arndt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.rnd7.kata.reversi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.rnd7.kata.reversi.logic.GameLogic;
import de.rnd7.kata.reversi.logic.NoMovePossibleException;
import de.rnd7.kata.reversi.logic.ai.AILogic;
import de.rnd7.kata.reversi.logic.ai.AIMatrix;
import de.rnd7.kata.reversi.model.Cell;
import de.rnd7.kata.reversi.model.CellState;
import de.rnd7.kata.reversi.model.Coordinate;
import de.rnd7.kata.reversi.model.GameField;
import de.rnd7.kata.reversi.utils.GameFieldPrinter;

public class Main {
	public static void main(final String[] args) throws IOException {
		GameField field = newField();

		CellState player = CellState.WHITE;
		int iteration = 1;

		final AIMatrix white = AIMatrix.fromResource("white.txt");
		final AIMatrix black = AIMatrix.fromResource("black.txt");

		try {
			while (true) {
				System.out.println(String.format("Iteration #%d, Player: %s", iteration++, player));
				try {
					field = move(field, player, getMatrix(player, white, black));
				} catch (final NoMovePossibleException e) {
					player = nextPlayer(player);
					field = move(field, player, getMatrix(player, white, black));
				}
				player = nextPlayer(player);
			}
		} catch (final NoMovePossibleException e) {
			System.out.println("Game completed");

			final long whiteCount = field.countState(CellState.WHITE);
			final long blackCount = field.countState(CellState.BLACK);

			if (whiteCount == blackCount) {
				System.out.println("The game was a draw.");
			} else {
				System.out.println(String.format("%d vs. %d, %s wins.", whiteCount, blackCount, blackCount > whiteCount ? "black" : "white"));
			}
		}
	}

	private static AIMatrix getMatrix(final CellState player, final AIMatrix white, final AIMatrix black) {
		return player == CellState.WHITE ? white : black;
	}

	private static GameField move(final GameField field, final CellState player, final AIMatrix matrix) throws NoMovePossibleException {
		final GameField possibleMoves = cloneField(field);
		final GameField output = cloneField(field);

		final List<Cell> possibleCells = new ArrayList<Cell>();
		final GameLogic gameLogic = new GameLogic(field);
		for (final Cell cell : field.getCells()) {
			if (gameLogic.isValidMove(player, cell)) {
				possibleMoves.getCell(cell.getCoordinate()).setState(CellState.ALLOWED);
				possibleCells.add(cell);
			}
		}

		if (possibleCells.isEmpty()) {
			throw new NoMovePossibleException();
		}

		final Optional<Coordinate> bestMove = AILogic.bestMove(matrix, possibleCells.stream().map(Cell::getCoordinate));
		final Cell cell = field.getCell(bestMove.get());

		gameLogic.apply(player, cell, output);

		System.out.println("Possible:");
		System.out.println(GameFieldPrinter.print(possibleMoves));
		System.out.println("Done:");
		System.out.println(GameFieldPrinter.print(output));

		return output;
	}

	private static CellState nextPlayer(final CellState player) {
		return player == CellState.BLACK ? CellState.WHITE : CellState.BLACK;
	}

	private static GameField newField() {
		final GameField gameField = new GameField();

		gameField.getCell(new Coordinate(3, 3)).setState(CellState.BLACK);
		gameField.getCell(new Coordinate(3, 4)).setState(CellState.WHITE);
		gameField.getCell(new Coordinate(4, 3)).setState(CellState.WHITE);
		gameField.getCell(new Coordinate(4, 4)).setState(CellState.BLACK);

		return gameField;
	}

	private static GameField cloneField(final GameField original) {
		final GameField gameField = new GameField();

		for (final Cell cell : original.getCells()) {
			gameField.getCell(cell.getCoordinate()).setState(cell.getState());
		}

		return gameField;
	}
}