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

public class Statistic {
	private int whiteWins;
	private int blackWins;

	public Statistic() {
	}

	public void set(final long white, final long black) {
		if (white > black) {
			this.whiteWins++;
		} else if (black > white) {
			this.blackWins++;
		}
	}

	public int getBlackWins() {
		return this.blackWins;
	}

	public int getWhiteWins() {
		return this.whiteWins;
	}
}
