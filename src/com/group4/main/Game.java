package com.group4.main;

import com.group4.main.model.Cell;
import com.group4.main.model.Field;
import com.group4.main.model.Player;

/**
 * @author darkwizard
 */
public class Game {
	Field gameField;
	Player player1;
	Player player2;

	Player currPlayer;
	private boolean isPlaying;

	Game() {
		currPlayer = player1;
		while (this.isPlaying) {
			setMark(currPlayer.getSelection(this.gameField));
			this.isPlaying = currPlayer.isWinningTurn();
			this.currPlayer = this.swapPlayer();
		}

		endGame();
	}

	public void setMark(Cell at) {

	}

	/**
	 * Show winning text, maybe
	 */
	public void endGame() {

	}

	public Player swapPlayer() {
		return currPlayer == player1 ? player2 : player1;
	}

	public static void main(String[] args) {
		new Game();
	}
}
