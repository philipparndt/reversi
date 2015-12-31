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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.rnd7.kata.reversi.model.Coordinate;

public class AIMatrixTest {
	@Test
	public void testLoadFromResource() throws Exception {
		final AIMatrix matrix = AIMatrix.fromResource("black.txt");
		assertEquals(90, matrix.get(new Coordinate(0, 0)));
		assertEquals(-90, matrix.get(new Coordinate(1, 0)));
	}

	@Test
	public void testNeverNull() throws Exception {
		final AIMatrix matrix = new AIMatrix();
		assertEquals(0, matrix.get(new Coordinate(0, 0)));
	}
}
