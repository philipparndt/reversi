package de.rnd7.kata.reversi.utils;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import de.rnd7.kata.reversi.model.CellState;
import de.rnd7.kata.reversi.model.Coordinate;
import de.rnd7.kata.reversi.model.GameField;

public class GameFieldPrinterTest {
	@Test
	public void testPrintField() throws Exception {
		assertEquals(this.ignoreLineBreaks(this.getField("emptyField.txt")), this.ignoreLineBreaks(GameFieldPrinter.print(new GameField())));
	}

	@Test
	public void testDefaultField() throws Exception {
		final GameField gameField = new GameField();

		gameField.getCell(new Coordinate(3, 3)).setState(CellState.BLACK);
		gameField.getCell(new Coordinate(3, 4)).setState(CellState.WHITE);
		gameField.getCell(new Coordinate(4, 3)).setState(CellState.WHITE);
		gameField.getCell(new Coordinate(4, 4)).setState(CellState.BLACK);

		assertEquals(this.ignoreLineBreaks(this.getField("defaultField.txt")), this.ignoreLineBreaks(GameFieldPrinter.print(gameField)));
	}

	private String getField(final String name) throws IOException {
		return IOUtils.toString(GameFieldPrinterTest.class.getResourceAsStream(name));
	}

	private String ignoreLineBreaks(final String s) {
		return s.replaceAll("\\r\\n|\\n", "\\n");
	}
}
