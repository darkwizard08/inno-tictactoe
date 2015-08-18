package com.group4.main.model;

/**
 * @author darkwizard
 */
public abstract class Player {
	char playerMark = 'X'; // or 'O'
	public abstract Cell getSelection(Field currentField);
	public abstract boolean isWinningTurn();

	public Player(char playerMark) {
		this.playerMark = playerMark;
	}
}
