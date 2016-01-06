package de.rnd7.kata.reversi.ui;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.google.common.collect.ImmutableList;

import de.rnd7.kata.reversi.logic.GameLogic;
import de.rnd7.kata.reversi.logic.ai.AIMatrix;
import de.rnd7.kata.reversi.logic.ai.AlphaBetaPruningAI;
import de.rnd7.kata.reversi.logic.ai.MatrixAI;
import de.rnd7.kata.reversi.logic.ai.MatrixAI2;
import de.rnd7.kata.reversi.logic.ai.MinimaxAI;
import de.rnd7.kata.reversi.logic.ai.ReversiAI;
import de.rnd7.kata.reversi.model.Cell;
import de.rnd7.kata.reversi.model.CellState;
import de.rnd7.kata.reversi.model.Coordinate;

public class GameDialog {
	private ReversiAI white;
	private final Shell shell;

	private final AIMatrix matrix = AIMatrix.fromResource("matrix.txt");
	private final ImmutableList<ReversiAI> ais = ImmutableList.of(new MatrixAI(this.matrix), new MatrixAI2(new MatrixAI(this.matrix)), new AlphaBetaPruningAI(), new MinimaxAI());
	private final Map<String, ReversiAI> aiMap = this.ais.stream().collect(Collectors.toMap(x -> x.getClass().getSimpleName(), x -> x));

	private final GameController controller = new GameController();

	public GameDialog() throws IOException {

		final Display display = new Display();
		this.shell = new Shell(display);

		this.shell.setLayout(new GridLayout());
		this.shell.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Combo combo = new Combo(this.shell, SWT.READ_ONLY);
		combo.setItems(this.aiMap.keySet().stream().toArray(i -> new String[i]));
		this.registerChangeListener(combo);

		final Button aiPlay = new Button(this.shell, SWT.NONE);
		aiPlay.setText("AI Play");

		final Label label = new Label(this.shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Canvas canvas = new Canvas(this.shell, SWT.BORDER);
		canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		final GameFieldRenderer renderer = new GameFieldRenderer(this.controller);
		canvas.addPaintListener(renderer);

		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(final MouseEvent e) {
				GameDialog.this.toCell(e).ifPresent(cell -> GameDialog.this.blackPlay(canvas, renderer, cell));
			}

		});
		aiPlay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				GameDialog.this.whitePlay(canvas);
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

	private void registerChangeListener(final Combo combo) {
		final SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				GameDialog.this.white = GameDialog.this.aiMap.get(combo.getText());
			}
		};

		combo.addSelectionListener(selectionAdapter);
		combo.select(0);
		selectionAdapter.widgetSelected(null);
	}

	private Optional<Cell> toCell(final MouseEvent e) {
		final int x = e.x / GameFieldRenderer.CELL_SIZE;
		final int y = e.y / GameFieldRenderer.CELL_SIZE;
		try {
			return Optional.of(this.controller.getField().getCell(new Coordinate(x, y)));
		} catch (final IllegalArgumentException e1) {
			return Optional.empty();
		}
	}

	private void whitePlay(final Canvas canvas) {
		final List<Cell> possibleCells = this.controller.getPossible();
		if (!possibleCells.isEmpty()) {
			final List<Coordinate> possibleMoves = possibleCells.stream().map(Cell::getCoordinate).collect(Collectors.toList());
			final Coordinate move = this.white.getMove(this.controller.getField(), CellState.WHITE, possibleMoves);
			try {
				this.controller.doMove(CellState.WHITE, move);
			} catch (final Exception e1) {
				final MessageBox messageBox = new MessageBox(this.shell, SWT.OK | SWT.ICON_INFORMATION);
				messageBox.setMessage(e1.getMessage());
				messageBox.open();
			}
		}

		canvas.redraw();
	}

	private void blackPlay(final Canvas canvas, final GameFieldRenderer renderer, final Cell cell) {
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