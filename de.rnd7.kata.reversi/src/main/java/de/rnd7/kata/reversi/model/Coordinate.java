package de.rnd7.kata.reversi.model;

public class Coordinate {
	private final int x;
	private final int y;

	public Coordinate(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	@Override
	public String toString() {
		return String.format("(%d, %d)", this.x, this.y);
	}

	public Coordinate getNeighbour(final Direction direction) {
		final int newX = this.x + direction.getX();
		final int newY = this.y + direction.getY();

		if (GameUtils.isValidCellPos(newY) && GameUtils.isValidCellPos(newX)) {
			return new Coordinate(newX, newY);
		} else {
			return null;
		}
	}

	public boolean hasNeighbour(final Direction direction) {
		return this.getNeighbour(direction) != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + this.x;
		result = (prime * result) + this.y;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final Coordinate other = (Coordinate) obj;
		if (this.x != other.x) {
			return false;
		}
		if (this.y != other.y) {
			return false;
		}
		return true;
	}
}
