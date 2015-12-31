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
import java.util.Optional;
import java.util.stream.Stream;

import de.rnd7.kata.reversi.model.Coordinate;

public final class AILogic {
	private AILogic() {
	}

	public static Optional<Coordinate> bestMove(final AIMatrix matrix, final Stream<Coordinate> possibleMoves) {
		final Comparator<Coordinate> comp = Comparator.comparing(c -> matrix.get(c));
		return possibleMoves.sorted(comp.reversed()).findFirst();
	}
}
