package com.group4.main.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author darkwizard
 */
public class HumanPlayer extends Player {
	private Scanner sc = new Scanner(System.in);
	private boolean winning = false;

	@Override
	public Cell makeTurn(Field currentField) {
		currentField.printField();
		System.out.println("Enter coordinates: [x y]");
		String xy = sc.nextLine();
		int x = Integer.parseInt(xy.split(" ")[0]);
		int y = Integer.parseInt(xy.split(" ")[1]);
		currentField.markCell(x, y, this.playerMark);

		List<Cell[]> list = null;

		// now checking if it's winning turn
		for (int i = 0; i < currentField.getSize(); ++i)
			for (int j = 0; j < currentField.getSize(); ++j) {
				list = this.getNumberOf3InRow(currentField, i, j);
				if (list.size() > 0)
					winning = true;
			}

		if (!winning) {
			// now checking diagonals
			this.addRow(list, this.getDiagonalRow(0, 0, 2, 2, currentField));
			this.addRow(list, this.getDiagonalRow(0, 2, 2, 0, currentField));
			if (list.size() > 0)
				winning = true;
		}

		return currentField.getCellAt(x, y);
	}

	@Override
	public boolean isWinningTurn() {
		return winning;
	}

	public HumanPlayer(char playerMark) {
		super(playerMark);
	}
}
