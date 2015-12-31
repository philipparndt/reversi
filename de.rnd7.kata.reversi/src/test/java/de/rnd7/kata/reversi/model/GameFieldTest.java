package de.rnd7.kata.reversi.model;

import org.junit.Assert;
import org.junit.Test;

public class GameFieldTest {
	@Test
	public void testBounds() throws Exception {
		GameField.assertBounds(1, 5);
		GameField.assertBounds(0, 0);
		GameField.assertBounds(7, 7);
		GameField.assertBounds(0, 7);
		GameField.assertBounds(7, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBoundsIllegal1() throws Exception {
		GameField.assertBounds(-1, 5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBoundsIllegal2() throws Exception {
		GameField.assertBounds(1, 9);
	}

	@Test
	public void testGetColPos() throws Exception {
		Assert.assertEquals(0, GameField.getColPos(0, 0));
	}

	@Test
	public void testCoordinates() throws Exception {
		Assert.assertEquals(new Coordinate(0, 0), GameField.getCoordinate(0));
		Assert.assertEquals(new Coordinate(7, 7), GameField.getCoordinate(63));
	}
}
