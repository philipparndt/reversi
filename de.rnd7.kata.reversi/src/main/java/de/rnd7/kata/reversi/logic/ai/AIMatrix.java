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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.CharEncoding;

import de.rnd7.kata.reversi.model.Coordinate;

public class AIMatrix {

	private final Map<Coordinate, Integer> map = new HashMap<>();

	AIMatrix() {
	}

	public static AIMatrix empty() {
		return new AIMatrix();
	}

	public static AIMatrix fromResource(final String name) throws IOException {
		final AIMatrix matrix = new AIMatrix();

		try (InputStream input = AIMatrix.class.getResourceAsStream(name)) {
			final LineIterator iterator = IOUtils.lineIterator(input, CharEncoding.UTF_8);

			int lineNumber = 0;
			while (iterator.hasNext()) {
				processLine(matrix, lineNumber++, iterator.next());
			}
		}

		return matrix;
	}

	private static void processLine(final AIMatrix matrix, final int y, final String line) {
		int x = 0;
		for (final char ch : line.toCharArray()) {
			final int weight;
			if (Character.isLowerCase(ch)) {
				weight = -Character.toUpperCase(ch);
			} else {
				weight = ch;
			}

			matrix.set(new Coordinate(x++, y), weight);
		}
	}

	private void set(final Coordinate coordinate, final int weight) {
		this.map.put(coordinate, weight);
	}

	public int get(final Coordinate coordinate) {
		final Integer result = this.map.get(coordinate);
		return result == null ? 0 : result;
	}
}
