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
import de.rnd7.kata.reversi.logic.ai.MatrixAI;
import de.rnd7.kata.reversi.logic.ai.MatrixAI2;
import de.rnd7.kata.reversi.logic.ai.MinimaxAI;
import de.rnd7.kata.reversi.logic.ai.ReversiAI;
import de.rnd7.kata.reversi.model.CellState;
import de.rnd7.kata.reversi.model.Coordinate;
import de.rnd7.kata.reversi.model.GameField;

public class Main {
	public static void main(final String[] args) throws IOException {
		final GameField field = newField();

		final CellState player = CellState.WHITE;

		final ReversiAI white = new MinimaxAI(); // new MatrixAI(AIMatrix.fromResource("matrix.txt"));
		final ReversiAI black = new MatrixAI2(new MatrixAI(AIMatrix.fromResource("matrix.txt"))); //

		int draw = 0;
		int whiteWins = 0;
		int blackWins = 0;

		for (int i = 0; i < 100; i++) {
			System.out.print(".");
			switch (runGame(field, player, black, white)) {
			case BLACK:
				blackWins++;
				break;
			case WHITE:
				whiteWins++;
				break;
			default:
				draw++;
			}
		}

		System.out.println(String.format("\n\nDraw: %d\n%s: %d\n%s: %d\n", draw, black.getClass().getSimpleName(), blackWins, white.getClass().getSimpleName(), whiteWins));
	}

	private static CellState runGame(GameField field, CellState player, final ReversiAI black, final ReversiAI white) throws IOException {

		try {
			while (true) {
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
				return CellState.EMPTY;
			} else {
				return blackCount > whiteCount ? CellState.BLACK : CellState.WHITE;
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