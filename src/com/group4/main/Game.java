package com.group4.main;

import com.group4.main.model.Field;
import com.group4.main.model.HumanPlayer;
import com.group4.main.model.Player;

/**
 * @author darkwizard
 */
public class Game {
	Field gameField = new Field(3);
	Player player1 = new HumanPlayer('X');
	Player player2 = new HumanPlayer('O');

	Player currPlayer;
	private boolean isPlaying = true;

	Game() {
		currPlayer = player2;
		while (this.isPlaying) {
			this.currPlayer = this.swapPlayer();
			currPlayer.makeTurn(this.gameField);
			this.isPlaying = !currPlayer.isWinningTurn();
		}

		endGame();
	}

	/**
	 * Show winning text, maybe
	 */
	public void endGame() {
		System.out.println("\"" + this.currPlayer.getSymbol() + "\" wins!");
	}

	public Player swapPlayer() {
		return currPlayer == player1 ? player2 : player1;
	}

	public static void main(String[] args) {
		new Game();
	}
}
