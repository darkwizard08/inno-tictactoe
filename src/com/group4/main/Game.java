package com.group4.main;

import com.group4.main.model.BotPlayer;
import com.group4.main.model.Field;
import com.group4.main.model.HumanPlayer;
import com.group4.main.model.Player;

/**
 * @author darkwizard
 */
public class Game {
	Field gameField = new Field(3);
//	Player player1 = new BotPlayer('X');
	Player player1 = new HumanPlayer('X');
//	Player player2 = new HumanPlayer('X');
	Player player2 = new BotPlayer('O');
	Player currPlayer = player2;

	Game() {
		boolean isPlaying = true;
		while (isPlaying) {
			this.currPlayer = this.swapPlayer();
			this.gameField.printField();
			currPlayer.makeTurn(this.gameField);
			isPlaying = !currPlayer.isWinningTurn(this.gameField);
		}

		this.gameField.printField();
		endGame();
	}

	/**
	 * Show winning text, maybe
	 */
	public void endGame() {
		if (this.gameField.isFilledCompletely())
			System.out.println("Draw!");
		else
			System.out.println("\"" + this.currPlayer.getSymbol() + "\" wins!");
	}

	public Player swapPlayer() {
		return currPlayer == player1 ? player2 : player1;
	}

	public static void main(String[] args) {
		new Game();
	}
}
