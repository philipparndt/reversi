package de.rnd7.kata.reversi.utils;

import de.rnd7.kata.reversi.model.Cell;
import de.rnd7.kata.reversi.model.GameField;
import de.rnd7.kata.reversi.model.GameUtils;

public final class GameFieldPrinter {
	public static String print(final GameField gameField) {
		int pos = 0;
		final StringBuilder sb = new StringBuilder();

		for (final Cell cell : gameField.getCells()) {
			if ((pos != 0) && ((pos % GameUtils.COLS) == 0)) {
				sb.append(System.lineSeparator());
			}

			sb.append(printCell(cell));
			pos++;
		}

		return sb.toString();
	}

	private static String printCell(final Cell cell) {
		switch (cell.getState()) {
		case WHITE:
			return "W";
		case BLACK:
			return "B";
		case ALLOWED:
			return "O";
		default:
			return ".";
		}
	}
}
