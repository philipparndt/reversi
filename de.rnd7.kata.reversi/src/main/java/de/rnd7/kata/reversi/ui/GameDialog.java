package de.rnd7.kata.reversi.ui;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
import de.rnd7.kata.reversi.logic.ai.AIMatrix;
import de.rnd7.kata.reversi.logic.ai.AlphaBetaPruningAI;
import de.rnd7.kata.reversi.logic.ai.ReversiAI;
import de.rnd7.kata.reversi.model.Cell;
import de.rnd7.kata.reversi.model.CellState;
import de.rnd7.kata.reversi.model.Coordinate;

public class GameDialog {
	private final ReversiAI white;
	private final ReversiAI black;
	// private CellState player = CellState.WHITE;
	// private GameField field;
	private final Shell shell;

	private final GameController controller = new GameController();

	public GameDialog() throws IOException {
		final AIMatrix matrix = AIMatrix.fromResource("matrix.txt");

		this.white = new AlphaBetaPruningAI(); // MatrixAI(matrix);
		this.black = null; // new MatrixAI2(new
							// MatrixAI(matrix));

		// this.field = newField();

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
		final GameFieldRenderer renderer = new GameFieldRenderer(this.controller);
		canvas.addPaintListener(renderer);

		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(final MouseEvent e) {
				final int x = e.x / GameFieldRenderer.CELL_SIZE;
				final int y = e.y / GameFieldRenderer.CELL_SIZE;
				try {
					final Cell cell = GameDialog.this.controller.getField().getCell(new Coordinate(x, y));
					GameDialog.this.humanPlay(canvas, renderer, cell);
				} catch (final IllegalArgumentException e1) {
					// Can be ignored here
				}
			}
		});
		nextGenButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final List<Cell> possibleCells = GameDialog.this.controller.getPossible();
				if (!possibleCells.isEmpty()) {
					final List<Coordinate> possibleMoves = possibleCells.stream().map(Cell::getCoordinate).collect(Collectors.toList());
					final Coordinate move = GameDialog.this.white.getMove(GameDialog.this.controller.getField(), CellState.WHITE, possibleMoves);
					GameDialog.this.controller.doMove(CellState.WHITE, move);
				}

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
		final GameLogic gameLogic = new GameLogic(this.controller.getField());
		if (!gameLogic.isValidMove(CellState.BLACK, cell)) {
			return;
		}

		try {
			this.controller.doMove(CellState.BLACK, cell.getCoordinate());
		} catch (final Exception e1) {
			final MessageBox messageBox = new MessageBox(this.shell, SWT.OK | SWT.ICON_INFORMATION);
			messageBox.setMessage(e1.getMessage());
			messageBox.open();
		}

		canvas.redraw();
	}
}