package de.rnd7.kata.reversi.model;

public class Cell {
	private CellState state = CellState.EMPTY;
	private final Coordinate coordinate;

	public Cell(final Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public void setState(final CellState state) {
		this.state = state;
	}

	public CellState getState() {
		return this.state;
	}

	public Coordinate getCoordinate() {
		return this.coordinate;
	}

	@Override
	public String toString() {
		return String.format("[%s, %s]", this.state, this.coordinate.toString());
	}
}
