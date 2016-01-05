package de.rnd7.kata.reversi.ui;

import java.io.IOException;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import de.rnd7.kata.reversi.logic.GameLogic;
import de.rnd7.kata.reversi.logic.NoMovePossibleException;
import de.rnd7.kata.reversi.logic.ai.AILogic;
import de.rnd7.kata.reversi.logic.ai.AIMatrix;
import de.rnd7.kata.reversi.logic.ai.MinimaxAI;
import de.rnd7.kata.reversi.logic.ai.ReversiAI;
import de.rnd7.kata.reversi.model.Cell;
import de.rnd7.kata.reversi.model.CellState;
import de.rnd7.kata.reversi.model.Coordinate;
import de.rnd7.kata.reversi.model.GameField;

public class GameDialog {
	private final ReversiAI white;
	private final ReversiAI black;
	private CellState player = CellState.WHITE;
	private GameField field;
	private final Shell shell;

	private static GameField newField() {
		final GameField gameField = new GameField();

		gameField.getCell(new Coordinate(3, 3)).setState(CellState.BLACK);
		gameField.getCell(new Coordinate(3, 4)).setState(CellState.WHITE);
		gameField.getCell(new Coordinate(4, 3)).setState(CellState.WHITE);
		gameField.getCell(new Coordinate(4, 4)).setState(CellState.BLACK);

		return gameField;
	}

	public GameDialog() throws IOException {
		final AIMatrix matrix = AIMatrix.fromResource("matrix.txt");

		this.white = new MinimaxAI(); // MatrixAI(matrix);
		this.black = new HumanPlayerAI(); // new MatrixAI2(new
											// MatrixAI(matrix));

		this.field = newField();

		final Display display = new Display();
		this.shell = new Shell(display);

		this.shell.setLayout(new GridLayout());
		this.shell.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Composite composite = new Composite(this.shell, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Button nextGenButton = new Button(composite, SWT.NONE);
		nextGenButton.setText("AI Play");

		final Label label = new Label(this.shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Canvas canvas = new Canvas(this.shell, SWT.BORDER);
		canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		final GameFieldRenderer renderer = new GameFieldRenderer(this.field);
		canvas.addPaintListener(renderer);

		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(final MouseEvent e) {
				final int x = e.x / GameFieldRenderer.CELL_SIZE;
				final int y = e.y / GameFieldRenderer.CELL_SIZE;
				try {
					final Cell cell = GameDialog.this.field.getCell(new Coordinate(x, y));
					GameDialog.this.humanPlay(canvas, renderer, cell);
				} catch (final IllegalArgumentException e1) {
					// Can be ignored here
				}
			}
		});
		nextGenButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				GameDialog.this.field = GameDialog.this.runGame(GameDialog.this.field);
				renderer.setField(GameDialog.this.field);

				canvas.redraw();
			}
		});

		final Rectangle clientArea = this.shell.getClientArea();
		this.shell.setBounds(clientArea.x + 10, clientArea.y + 10, 300, 300);
		this.shell.open();
		while (!this.shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private void humanPlay(final Canvas canvas, final GameFieldRenderer renderer, final Cell cell) {
		final GameLogic gameLogic = new GameLogic(GameDialog.this.field);
		GameDialog.this.player = CellState.BLACK;
		if (!gameLogic.isValidMove(GameDialog.this.player, cell)) {
			return;
		}

		final ReversiAI reversiAI = new ReversiAI() {
			@Override
			public Coordinate getMove(final GameField field, final CellState player, final List<Coordinate> possibleMoves) {
				return cell.getCoordinate();
			}
		};

		try {
			GameDialog.this.field = AILogic.move(GameDialog.this.field, GameDialog.this.player, reversiAI);
			renderer.setField(GameDialog.this.field);

			GameDialog.this.player = CellState.WHITE;
		} catch (final NoMovePossibleException e1) {
			e1.printStackTrace();
		} finally {

		}

		canvas.redraw();
	}

	private GameField runGame(final GameField field) {
		GameField result = field;
		try {
			result = AILogic.move(field, this.player, this.getAI(this.player));
			this.player = AILogic.nextPlayer(this.player);
		} catch (IllegalStateException | NoMovePossibleException e) {
			final MessageBox messageBox = new MessageBox(this.shell, SWT.OK | SWT.ICON_INFORMATION);
			messageBox.setMessage("It's your turn!");
			messageBox.open();
		}
		return result;
	}

	private ReversiAI getAI(final CellState player) {
		return player == CellState.WHITE ? this.white : this.black;
	}
}