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
package de.rnd7.kata.reversi.logic.ai;

import java.util.List;
import java.util.stream.Collectors;

import de.rnd7.kata.reversi.logic.GameLogic;
import de.rnd7.kata.reversi.logic.NoMovePossibleException;
import de.rnd7.kata.reversi.model.Cell;
import de.rnd7.kata.reversi.model.CellState;
import de.rnd7.kata.reversi.model.Coordinate;
import de.rnd7.kata.reversi.model.GameField;

public final class AILogic {

	private AILogic() {
	}

	public static CellState nextPlayer(final CellState player) {
		return player == CellState.BLACK ? CellState.WHITE : CellState.BLACK;
	}

	public static GameField move(final GameField field, final CellState player, final ReversiAI ai) throws NoMovePossibleException {
		final List<Cell> possibleCells = AIUtils.getPossibleMoves(field, player);

		if (possibleCells.isEmpty()) {
			throw new NoMovePossibleException();
		}

		final GameField output = AIUtils.cloneField(field);

		final List<Coordinate> possibleMoves = possibleCells.stream().map(Cell::getCoordinate).collect(Collectors.toList());
		final Coordinate move = ai.getMove(field, player, possibleMoves);

		final GameLogic gameLogic = new GameLogic(field);
		gameLogic.apply(player, output.getCell(move), output);

		return output;
	}

}
