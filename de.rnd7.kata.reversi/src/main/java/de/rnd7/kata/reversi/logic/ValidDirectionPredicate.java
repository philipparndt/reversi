package de.rnd7.kata.reversi.logic;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;

import de.rnd7.kata.reversi.model.Cell;
import de.rnd7.kata.reversi.model.CellState;
import de.rnd7.kata.reversi.model.Coordinate;
import de.rnd7.kata.reversi.model.Direction;
import de.rnd7.kata.reversi.model.DirectionIterator;

final class ValidDirectionPredicate implements Predicate<Direction> {
	private final Predicate<Coordinate> pSamePlayerOnCoordinate;
	private final Predicate<Coordinate> pEmptyFieldOnCoordinate;
	private final Cell cell;
	private final Function<Coordinate, Cell> fGetCell;

	public ValidDirectionPredicate(final CellState player, final Cell cell, final Function<Coordinate, Cell> fGetCell) {
		this.cell = cell;
		this.fGetCell = fGetCell;
		this.pSamePlayerOnCoordinate = this.createPredicate(player);
		this.pEmptyFieldOnCoordinate = this.createPredicate(CellState.EMPTY);
	}

	private Predicate<Coordinate> createPredicate(final CellState player) {
		final Predicate<Cell> pSamePlayer = new Predicate<Cell>() {
			@Override
			public boolean apply(final Cell cell) {
				return cell.getState() == player;
			}
		};

		final Predicate<Coordinate> pSamePlayerOnCoordinate = Predicates.compose(pSamePlayer, this.fGetCell);
		return pSamePlayerOnCoordinate;
	}

	@Override
	public boolean apply(final Direction direction) {
		return this.isValid(this.cell, direction, this.pSamePlayerOnCoordinate, this.pEmptyFieldOnCoordinate);
	}

	private boolean isValid(final Cell cell, final Direction direction, final Predicate<Coordinate> pSamePlayerOnCoordinate, final Predicate<Coordinate> pEmptyFieldOnCoordinate) {
		DirectionIterator iterator = new DirectionIterator(direction, cell.getCoordinate());
		final int samePlayer = Iterators.indexOf(iterator, pSamePlayerOnCoordinate);

		iterator = new DirectionIterator(direction, cell.getCoordinate());
		final int emptyField = Iterators.indexOf(iterator, pEmptyFieldOnCoordinate);

		if ((emptyField < 0) || (emptyField > samePlayer)) {
			if (samePlayer > 0) {
				return true;
			}
		}

		return false;
	}
}