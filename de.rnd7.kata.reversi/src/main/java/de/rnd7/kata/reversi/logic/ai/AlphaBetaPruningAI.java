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

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import de.rnd7.kata.reversi.logic.GameLogic;
import de.rnd7.kata.reversi.model.Cell;
import de.rnd7.kata.reversi.model.CellState;
import de.rnd7.kata.reversi.model.Coordinate;
import de.rnd7.kata.reversi.model.GameField;

public class AlphaBetaPruningAI implements ReversiAI {

	private static final int DEPTH = 5;
	private final AIMatrix matrix;

	private class State {
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MIN_VALUE;
	}

	private class Result {
		int value = 0;
		Cell cell;
	}

	public AlphaBetaPruningAI() throws IOException {
		this.matrix = AIMatrix.fromResource("matrix.txt");
	}

	@Override
	public Coordinate getMove(final GameField field, final CellState player, final List<Coordinate> possibleMoves) {
		final State state = new State();
		final Result alphabeta = this.alphabeta(field, DEPTH, state, true, player);
		return alphabeta.cell == null ? possibleMoves.iterator().next() : alphabeta.cell.getCoordinate();
	}

	private Result alphabeta(final GameField node, final int depth, final State state, final boolean maximizingPlayer, final CellState player) {
		if ((depth == 0) || this.isTerminalNode(node, depth)) {
			final Result result = new Result();
			result.value = this.theHeuristicValueOfNode(node, player);
			return result;
		} else if (maximizingPlayer) {
			final List<Cell> moves = AIUtils.getPossibleMoves(node, player);
			final Result result = new Result();
			result.value = Integer.MIN_VALUE;

			for (final Cell cell : moves) {
				final GameField child = this.applyMove(node, player, cell);
				final Result alphabeta = this.alphabeta(child, depth - 1, state, false, player);
				if (alphabeta.value > result.value) {
					result.cell = cell;
					result.value = alphabeta.value;
				}

				state.alpha = Math.max(state.alpha, result.value);

				if (state.beta <= state.alpha) {
					// beta cut off
					break;
				}
			}

			return result;
		} else {
			final List<Cell> moves = AIUtils.getPossibleMoves(node, player);
			final Result result = new Result();
			result.value = Integer.MAX_VALUE;

			for (final Cell cell : moves) {
				final GameField child = this.applyMove(node, player, cell);

				final Result alphabeta = this.alphabeta(child, depth - 1, state, true, player);
				if (alphabeta.value < result.value) {
					result.cell = cell;
					result.value = alphabeta.value;
				}

				state.beta = Math.min(state.beta, result.value);

				if (state.beta <= state.alpha) {
					// alpha cut off
					break;
				}
			}

			return result;
		}

	}

	private GameField applyMove(final GameField node, final CellState player, final Cell cell) {
		final GameField child = AIUtils.cloneField(node);
		final GameLogic gameLogic = new GameLogic(child);
		gameLogic.apply(player, cell, child);
		return child;
	}

	private boolean isTerminalNode(final GameField node, final int depth) {
		final List<Cell> movesA = AIUtils.getPossibleMoves(node, CellState.WHITE);
		final List<Cell> movesB = AIUtils.getPossibleMoves(node, CellState.BLACK);
		return movesA.isEmpty() && movesB.isEmpty();
	}

	private int theHeuristicValueOfNode(final GameField node, final CellState player) {
		final IntStream intStream = node.getCells().stream().filter(cell -> cell.getState() == player).map(cell -> cell.getCoordinate()).mapToInt(this.matrix::get);

		return intStream.sum();
	}

}
