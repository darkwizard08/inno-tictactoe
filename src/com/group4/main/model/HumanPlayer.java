package com.group4.main.model;

import java.util.List;
import java.util.Scanner;

/**
 * @author darkwizard
 */
public class HumanPlayer extends Player {
	private Scanner sc = new Scanner(System.in);

	@Override
	public Cell makeTurn(Field currentField) {
		//currentField.printField();
		System.out.println("Enter coordinates: [x y]");
		String xy = sc.nextLine();
		int x = Integer.parseInt(xy.split(" ")[0]);
		int y = Integer.parseInt(xy.split(" ")[1]);
		currentField.markCell(x, y, this.playerMark);

		return currentField.getCellAt(x, y);
	}

	public HumanPlayer(char playerMark) {
		super(playerMark);
	}
}
