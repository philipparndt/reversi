/**
 * Copyright 2016 Philipp Arndt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.rnd7.kata.reversi.ui;

import java.util.List;

import de.rnd7.kata.reversi.logic.GameLogic;
import de.rnd7.kata.reversi.logic.ai.AILogic;
import de.rnd7.kata.reversi.logic.ai.AIUtils;
import de.rnd7.kata.reversi.model.Cell;
import de.rnd7.kata.reversi.model.CellState;
import de.rnd7.kata.reversi.model.Coordinate;
import de.rnd7.kata.reversi.model.GameField;

public class GameController {
	private CellState player = CellState.WHITE;
	private GameField field;

	public GameController() {
		this.reset();
	}

	public void reset() {
		final GameField gameField = new GameField();

		gameField.getCell(new Coordinate(3, 3)).setState(CellState.BLACK);
		gameField.getCell(new Coordinate(3, 4)).setState(CellState.WHITE);
		gameField.getCell(new Coordinate(4, 3)).setState(CellState.WHITE);
		gameField.getCell(new Coordinate(4, 4)).setState(CellState.BLACK);

		this.player = CellState.WHITE;

		this.field = gameField;
	}

	public void setField(final GameField field) {
		this.field = field;
	}

	public GameField getField() {
		return this.field;
	}

	public void doMove(final CellState player, final Coordinate coordinate) {
		if (player != this.player) {
			throw new IllegalStateException("It is not your turn!");
		}

		this.apply(player, coordinate);

		final CellState nextPlayer = AILogic.nextPlayer(this.player);
		if (this.canMove(nextPlayer)) {
			this.player = nextPlayer;
		}
	}

	private void apply(final CellState player, final Coordinate coordinate) {
		final GameField output = AIUtils.cloneField(this.field);
		final GameLogic gameLogic = new GameLogic(this.field);
		gameLogic.apply(player, output.getCell(coordinate), output);
		this.field = output;
	}

	public CellState getPlayer() {
		return this.player;
	}

	public boolean isGameCompleted() {
		return !this.canMove(CellState.WHITE) && !this.canMove(CellState.BLACK);
	}

	private boolean canMove(final CellState player) {
		return !AIUtils.getPossibleMoves(this.field, player).isEmpty();
	}

	public List<Cell> getPossible() {
		return AIUtils.getPossibleMoves(this.getField(), this.getPlayer());
	}

}
