package de.rnd7.kata.reversi.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DirectionIteratorTest {
	@Test
	public void testName() throws Exception {
		final DirectionIterator iterator = new DirectionIterator(Direction.NORTH_WEST, new Coordinate(7, 7));

		int step = 0;
		Coordinate last = null;
		while (iterator.hasNext()) {
			last = iterator.next();
			step++;
		}

		assertEquals(7, step);
		assertEquals(new Coordinate(0, 0), last);
	}
}
