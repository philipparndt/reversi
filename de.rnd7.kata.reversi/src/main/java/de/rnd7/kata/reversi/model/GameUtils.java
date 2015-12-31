package de.rnd7.kata.reversi.model;

public class GameUtils {
	public static final int COLS = 8;

	public static boolean isValidCellPos(final int pos) {
		return (pos >= 0) && (pos < COLS);
	}

}
