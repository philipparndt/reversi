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
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.rnd7.kata.reversi.logic.GameLogic;
import de.rnd7.kata.reversi.model.Cell;
import de.rnd7.kata.reversi.model.CellState;
import de.rnd7.kata.reversi.model.Coordinate;
import de.rnd7.kata.reversi.model.GameField;

public class MatrixAI3 implements ReversiAI {

	private static final int ENDGAME_LIMIT = 10;
	private final MatrixAI matrixAI;
	private Random rand = new Random();
	
	private class EndGameStats {
		int me = 0;
		int other = 0;
	}
	
	public MatrixAI3(final MatrixAI matrixAI) {
		this.matrixAI = matrixAI;
	}

	@Override
	public Coordinate getMove(final GameField field, final CellState player, final List<Coordinate> possibleMoves) {
		final Comparator<Coordinate> comp;
		if (field.countState(CellState.EMPTY) <= ENDGAME_LIMIT) {
			comp = Comparator.comparing(c -> addRandom(this.calcEndGameValue(field, player, c)));
		}
		else {
			comp = Comparator.comparing(c -> addRandom(this.calcValue(field, player, c, true)));
		}
		return possibleMoves.stream().sorted(comp.reversed()).findFirst().get();
	}

	private int calcEndGameValue(final GameField field, final CellState player, final Coordinate coordinate) {
		final GameField cloned = AIUtils.cloneField(field);
		return 0;
	}
	
	private void endGameRecursion(EndGameStats stats, final GameField field, final CellState player) {
		
		List<Coordinate> possibleCells = getPossible(field, player);
		
		if (possibleCells.isEmpty()) {
			
		}
		
		CellState nextPlayer = AILogic.nextPlayer(player);
		possibleCells = getPossible(field, nextPlayer);
		
//		final GameField cloned = AIUtils.cloneField(field);
	}

	private List<Coordinate> getPossible(final GameField field, final CellState player) {
		return AIUtils.getPossibleMoves(field, player).stream()
				.map(Cell::getCoordinate)
				.collect(Collectors.toList());
	}
	
	private int addRandom(int value) {
		return value * 100 + rand.nextInt(50); 
	}
	
	private int calcValue(final GameField field, final CellState player, final Coordinate coordinate, boolean recursive) {
		final GameField cloned = AIUtils.cloneField(field);
		GameLogic logic = new GameLogic(cloned);
		final GameField output = AIUtils.cloneField(field);

		final AIMatrix matrix = this.matrixAI.getMatrix();
		final int myValue = matrix.get(coordinate);
		logic.apply(player, cloned.getCell(coordinate), output);
		int myTotal = countTotal(output, player);
		
		int otherTotal = 0;
		if (recursive) {
			CellState nextPlayer = AILogic.nextPlayer(player);
			final GameField clonedOther = AIUtils.cloneField(output);
			final Comparator<Coordinate> comp = Comparator.comparing(c -> this.calcValue(clonedOther, nextPlayer, c, false));
			
			final Stream<Coordinate> possibleCells = AIUtils.getPossibleMoves(clonedOther, nextPlayer).stream().map(Cell::getCoordinate);
			Optional<Coordinate> first = possibleCells.sorted(comp.reversed()).findFirst();
			
			otherTotal = Integer.MIN_VALUE;
			if (first.isPresent()) {
				logic = new GameLogic(clonedOther);
				logic.apply(nextPlayer, clonedOther.getCell(first.get()), clonedOther);
				
				otherTotal = countTotal(clonedOther, nextPlayer);
			}
		}
		return (myValue + myTotal) - otherTotal;
	}
	
	private int countTotal(final GameField field, final CellState player) {
		final AIMatrix matrix = this.matrixAI.getMatrix();
		return field.getCells().stream().map(Cell::getCoordinate).mapToInt(matrix::get).sum();
	}

}
