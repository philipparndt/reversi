package de.rnd7.kata.reversi.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CoordinateTest {
	@Test
	public void testValidNeighbour() throws Exception {
		final Coordinate coordinate = new Coordinate(5, 5);

		assertEquals(new Coordinate(5, 4), coordinate.getNeighbour(Direction.NORTH));
		assertEquals(new Coordinate(6, 4), coordinate.getNeighbour(Direction.NORTH_EAST));

		assertEquals(new Coordinate(6, 5), coordinate.getNeighbour(Direction.EAST));

		assertEquals(new Coordinate(6, 6), coordinate.getNeighbour(Direction.SOUTH_EAST));
		assertEquals(new Coordinate(5, 6), coordinate.getNeighbour(Direction.SOUTH));

		assertEquals(new Coordinate(4, 6), coordinate.getNeighbour(Direction.SOUTH_WEST));
		assertEquals(new Coordinate(4, 5), coordinate.getNeighbour(Direction.WEST));

		assertEquals(new Coordinate(4, 4), coordinate.getNeighbour(Direction.NORTH_WEST));
	}

	@Test
	public void testInvalidNeighbour() throws Exception {
		final Coordinate coordinate = new Coordinate(0, 0);

		assertNull(coordinate.getNeighbour(Direction.NORTH));
	}
}
