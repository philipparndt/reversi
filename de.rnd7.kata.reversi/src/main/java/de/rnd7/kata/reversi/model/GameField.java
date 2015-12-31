package de.rnd7.kata.reversi.model;

import java.util.List;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableList;

public class GameField {
	private final Cell[] cells = new Cell[GameUtils.COLS * GameUtils.COLS];

	public GameField() {
		this.clearField();
	}

	private void clearField() {
		for (int i = 0; i < this.cells.length; i++) {
			this.cells[i] = new Cell(getCoordinate(i));
		}
	}

	Cell getCell(final int x, final int y) {
		assertBounds(x, y);

		return this.cells[getColPos(x, y)];
	}

	static Coordinate getCoordinate(final int colPos) {
		final int x = colPos % GameUtils.COLS;
		final int y = colPos / GameUtils.COLS;

		return new Coordinate(x, y);
	}

	static int getColPos(final int x, final int y) {
		return x + (y * GameUtils.COLS);
	}

	public Cell getCell(final Coordinate coordinate) {
		return this.getCell(coordinate.getX(), coordinate.getY());
	}

	static void assertBounds(final int x, final int y) {
		if (!GameUtils.isValidCellPos(x)) {
			throw new IllegalArgumentException("Out of bounds: x");
		}
		if (!GameUtils.isValidCellPos(y)) {
			throw new IllegalArgumentException("Out of bounds: y");
		}
	}

	public List<Cell> getCells() {
		return ImmutableList.copyOf(this.cells);
	}

	public long countState(final CellState state) {
		return this.getCells().stream().filter(hasState(state)).count();
	}

	private static Predicate<? super Cell> hasState(final CellState state) {
		return cell -> cell.getState() == state;
	}

}
