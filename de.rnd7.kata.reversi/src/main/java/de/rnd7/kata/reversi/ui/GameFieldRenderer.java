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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;

import de.rnd7.kata.reversi.model.Cell;
import de.rnd7.kata.reversi.model.Coordinate;
import de.rnd7.kata.reversi.model.GameUtils;

public class GameFieldRenderer implements PaintListener {

	static final int CELL_SIZE = 20;
	private final GameController controller;

	public GameFieldRenderer(final GameController controller) {
		this.controller = controller;
	}

	@Override
	public void paintControl(final PaintEvent e) {
		final GC gc = e.gc;

		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_DARK_GRAY));

		gc.fillRectangle(0, 0, GameUtils.COLS * CELL_SIZE, GameUtils.COLS * CELL_SIZE);

		for (int x = 0; x < GameUtils.COLS; x++) {
			for (int y = 0; y < GameUtils.COLS; y++) {

				final int xx = x * CELL_SIZE;
				final int yy = y * CELL_SIZE;

				final Cell cell = this.controller.getField().getCell(new Coordinate(x, y));

				if (cell != null) {
					gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_DARK_GREEN));
					gc.fillRectangle(xx, yy, CELL_SIZE, CELL_SIZE);

					switch (cell.getState()) {
					case WHITE:
						gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
						gc.fillOval(xx + 1, yy + 1, CELL_SIZE - 1, CELL_SIZE - 1);
						break;
					case BLACK:
						gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
						gc.fillOval(xx + 1, yy + 1, CELL_SIZE - 1, CELL_SIZE - 1);
						break;
					default:
						break;
					}
				} else {
					gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_DARK_GRAY));
				}
			}
		}

		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_DARK_GRAY));

		for (int x = 0; x < GameUtils.COLS; x++) {
			final int xx = x * CELL_SIZE;
			gc.drawLine(xx, 0, xx, (GameUtils.COLS * CELL_SIZE) - 1);
		}

		for (int y = 0; y < GameUtils.COLS; y++) {
			final int yy = y * CELL_SIZE;
			gc.drawLine(0, yy, (GameUtils.COLS * CELL_SIZE) - 1, yy);
		}
	}

}
