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
import de.rnd7.kata.reversi.model.Cell;
import de.rnd7.kata.reversi.model.CellState;
import de.rnd7.kata.reversi.model.Coordinate;
import de.rnd7.kata.reversi.model.GameField;

public class MinimaxAI implements ReversiAI {

	private static final int MAX_DEPTH = 2;

	@Override
	public Coordinate getMove(final GameField field, final CellState player, final List<Coordinate> possibleMoves) {
		final List<Cell> possibleCells = possibleMoves.stream().map(field::getCell).collect(Collectors.toList());
		final Cell best = findBest(field, player, possibleCells);
		return best.getCoordinate();
	}

	static Cell findBest(final GameField field, final CellState player, final List<Cell> possibleCells) {
		final CellState otherPlayer = AILogic.nextPlayer(player);

		Cell result = null;
		final int wins = Integer.MIN_VALUE;

		for (final Cell cell : possibleCells) {
			final GameField cloneField = AIUtils.cloneField(field);
			final GameLogic gameLogic = new GameLogic(cloneField);
			gameLogic.apply(player, cell, cloneField);

			final Statistic statistic = new Statistic();
			rec(statistic, field, otherPlayer, 0);

			final int newWins = statistic.getBlackWins() - statistic.getWhiteWins();
			if (newWins > wins) {
				result = cell;
			}

		}

		return result;
	}

	private static void rec(final Statistic statistic, final GameField field, final CellState player, final int depth) {
		if (depth > MAX_DEPTH) {
			return;
		}

		List<Cell> possibleCells = AIUtils.getPossibleMoves(field, player);
		CellState current = player;

		if (possibleCells.isEmpty()) {
			current = AILogic.nextPlayer(player);
			possibleCells = AIUtils.getPossibleMoves(field, current);

			if (possibleCells.isEmpty()) {
				statistic.set(field.countState(CellState.WHITE), field.countState(CellState.BLACK));
			}
		}

		for (final Cell cell : possibleCells) {
			final GameField cloneField = AIUtils.cloneField(field);
			final GameLogic gameLogic = new GameLogic(cloneField);
			gameLogic.apply(current, cell, cloneField);

			rec(statistic, cloneField, AILogic.nextPlayer(current), depth + 1);
		}
	}

}
