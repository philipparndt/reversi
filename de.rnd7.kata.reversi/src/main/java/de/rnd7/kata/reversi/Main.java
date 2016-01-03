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

import de.rnd7.kata.reversi.logic.NoMovePossibleException;
import de.rnd7.kata.reversi.logic.ai.AILogic;
import de.rnd7.kata.reversi.logic.ai.AIMatrix;
import de.rnd7.kata.reversi.logic.ai.AlphaBetaPruningAI;
import de.rnd7.kata.reversi.logic.ai.MatrixAI;
import de.rnd7.kata.reversi.logic.ai.ReversiAI;
import de.rnd7.kata.reversi.model.CellState;
import de.rnd7.kata.reversi.model.Coordinate;
import de.rnd7.kata.reversi.model.GameField;

public class Main {
	public static void main(final String[] args) throws IOException {
		final GameField field = newField();

		final CellState player = CellState.WHITE;

		for (int i = 0; i < 100; i++) {
			runGame(field, player);
		}
	}

	private static void runGame(GameField field, CellState player) throws IOException {
		final int iteration = 1;

		final ReversiAI white = new MatrixAI(AIMatrix.fromResource("matrix.txt"));
		final ReversiAI black = new AlphaBetaPruningAI();

		try {
			while (true) {
				// System.out.println(String.format("Iteration #%d, Player: %s",
				// iteration++, player));
				try {
					field = AILogic.move(field, player, getAI(player, white, black));
				} catch (final NoMovePossibleException e) {
					player = AILogic.nextPlayer(player);
					field = AILogic.move(field, player, getAI(player, white, black));
				}
				player = AILogic.nextPlayer(player);
			}
		} catch (final NoMovePossibleException e) {

			final long whiteCount = field.countState(CellState.WHITE);
			final long blackCount = field.countState(CellState.BLACK);

			if (whiteCount == blackCount) {
				System.out.println("The game was a draw.");
			} else {
				final String name = blackCount > whiteCount ? "black" : "white";
				final String ainame = (blackCount > whiteCount ? black.getClass() : white.getClass()).getSimpleName();
				System.out.println(String.format("%d vs. %d, %s wins. (%s)", whiteCount, blackCount, name, ainame));
			}
		}
	}

	private static ReversiAI getAI(final CellState player, final ReversiAI white, final ReversiAI black) {
		return player == CellState.WHITE ? white : black;
	}

	private static GameField newField() {
		final GameField gameField = new GameField();

		gameField.getCell(new Coordinate(3, 3)).setState(CellState.BLACK);
		gameField.getCell(new Coordinate(3, 4)).setState(CellState.WHITE);
		gameField.getCell(new Coordinate(4, 3)).setState(CellState.WHITE);
		gameField.getCell(new Coordinate(4, 4)).setState(CellState.BLACK);

		return gameField;
	}

}