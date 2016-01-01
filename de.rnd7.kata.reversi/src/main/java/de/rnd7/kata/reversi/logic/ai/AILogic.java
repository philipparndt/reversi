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
package de.rnd7.kata.reversi.logic.ai;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import de.rnd7.kata.reversi.model.Coordinate;

public final class AILogic {
	private AILogic() {
	}

	public static Optional<Coordinate> bestMove(final AIMatrix matrix, final List<Coordinate> possibleMoves) {
		final Optional<Integer> bestMoveCount = possibleMoves.stream().map(matrix::get).sorted(Comparator.reverseOrder()).findFirst();
		return bestMoveCount.map(bmc -> pickRandomElement(matrix, possibleMoves, bmc));
	}

	private static Coordinate pickRandomElement(final AIMatrix matrix, final List<Coordinate> possibleMoves, final int bestMoveCount) {
		final List<Coordinate> bestMoves = possibleMoves.stream().filter(c -> matrix.get(c) == bestMoveCount).collect(Collectors.toList());

		final int index = ThreadLocalRandom.current().nextInt(bestMoves.size());
		return bestMoves.get(index);
	}
}
