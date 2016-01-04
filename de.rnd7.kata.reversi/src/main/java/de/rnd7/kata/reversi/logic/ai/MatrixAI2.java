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

import java.util.Comparator;
import java.util.List;

import de.rnd7.kata.reversi.logic.GameLogic;
import de.rnd7.kata.reversi.model.Cell;
import de.rnd7.kata.reversi.model.CellState;
import de.rnd7.kata.reversi.model.Coordinate;
import de.rnd7.kata.reversi.model.GameField;

public class MatrixAI2 implements ReversiAI {

	private final MatrixAI matrixAI;

	public MatrixAI2(final MatrixAI matrixAI) {
		this.matrixAI = matrixAI;
	}

	@Override
	public Coordinate getMove(final GameField field, final CellState player, final List<Coordinate> possibleMoves) {
		final Comparator<Coordinate> comp = Comparator.comparing(coordinate -> {
			return this.calcValue(field, player, coordinate);
		});

		return possibleMoves.stream().sorted(comp.reversed()).findFirst().get();
	}

	private int calcValue(final GameField field, final CellState player, final Coordinate coordinate) {
		final GameField cloned = AIUtils.cloneField(field);
		GameLogic logic = new GameLogic(cloned);
		final GameField output = AIUtils.cloneField(field);

		final AIMatrix matrix = this.matrixAI.getMatrix();
		final int myValue = matrix.get(coordinate);
		logic.apply(player, cloned.getCell(coordinate), output);

		logic = new GameLogic(output);
		final List<Cell> possibleCells = AIUtils.getPossibleMoves(field, AILogic.nextPlayer(player));

		final int otherValue = possibleCells.stream().map(Cell::getCoordinate).mapToInt(matrix::get).max().orElse(Integer.MIN_VALUE);

		return myValue - otherValue;
	}

}
