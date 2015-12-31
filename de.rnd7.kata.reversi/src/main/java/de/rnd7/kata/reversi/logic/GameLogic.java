package de.rnd7.kata.reversi.logic;

import java.util.Arrays;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

import de.rnd7.kata.reversi.model.Cell;
import de.rnd7.kata.reversi.model.CellState;
import de.rnd7.kata.reversi.model.Coordinate;
import de.rnd7.kata.reversi.model.Direction;
import de.rnd7.kata.reversi.model.DirectionIterator;
import de.rnd7.kata.reversi.model.GameField;

public class GameLogic {
	private final Function<Coordinate, Cell> fGetCell;

	private static final class GetCellFunction implements Function<Coordinate, Cell> {
		private final GameField field;

		private GetCellFunction(final GameField field) {
			this.field = field;
		}

		@Override
		public Cell apply(final Coordinate coordinate) {
			return this.field.getCell(coordinate);
		}
	}

	public GameLogic(final GameField field) {
		this.fGetCell = new GetCellFunction(field);
	}

	public boolean isValidMove(final CellState player, final Cell cell) {
		if ((player == CellState.EMPTY) || (player == CellState.ALLOWED)) {
			// TODO better models here
			throw new IllegalArgumentException("WHITE or BLACK");
		}

		return (cell.getState() == CellState.EMPTY) && !Iterables.isEmpty(this.getValidDirections(player, cell));
	}

	private Iterable<Direction> getValidDirections(final CellState player, final Cell cell) {
		return Iterables.filter(Arrays.asList(Direction.values()), new ValidDirectionPredicate(player, cell, this.fGetCell));
	}

	public void apply(final CellState player, final Cell cell, final GameField output) {
		if (!this.isValidMove(player, cell)) {
			throw new IllegalArgumentException("Invalid move");
		}

		final GetCellFunction getCellFunction = new GetCellFunction(output);
		getCellFunction.apply(cell.getCoordinate()).setState(player);

		for (final Direction direction : this.getValidDirections(player, cell)) {
			this.apply(player, cell, getCellFunction, direction);
		}
	}

	private void apply(final CellState player, final Cell cell, final GetCellFunction getCellFunction, final Direction direction) {
		final DirectionIterator iterator = new DirectionIterator(direction, cell.getCoordinate());

		while (iterator.hasNext()) {
			final Coordinate next = iterator.next();
			final Cell targetCell = getCellFunction.apply(next);
			final CellState state = targetCell.getState();

			if ((state == CellState.EMPTY) || (state == player)) {
				return;
			}

			targetCell.setState(player);
		}
	}
}
