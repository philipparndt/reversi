package de.rnd7.kata.reversi.model;

import java.util.Iterator;

public class DirectionIterator implements Iterator<Coordinate> {
	private final Direction direction;
	private Coordinate currentPosition;

	public DirectionIterator(final Direction direction, final Coordinate startCoordinate) {
		this.direction = direction;
		this.currentPosition = startCoordinate;
	}

	@Override
	public boolean hasNext() {
		return this.currentPosition.hasNeighbour(this.direction);
	}

	@Override
	public Coordinate next() {
		this.currentPosition = this.currentPosition.getNeighbour(this.direction);
		return this.currentPosition;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
