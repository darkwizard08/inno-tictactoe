package com.group4.main.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author darkwizard
 */
public abstract class Player {
	char playerMark = 'X'; // or 'O'
	public abstract Cell makeTurn(Field currentField);
	public abstract boolean isWinningTurn();

	public Player(char playerMark) {
		this.playerMark = playerMark;
	}

	protected List<Cell[]> getNumberOf3InRow(Field currentField, int x, int y) {
		List<Cell[]> result = new ArrayList<Cell[]>();
		Cell[] row;
		// checking left
		// if we can get a full row from (x, y) to the right with our marks, adding it.
		if (currentField.getSize() - x >= 3) {
			row = this.getRowIfFull(x, y, x + 2, y, currentField);
			this.addRow(result, row);
		}

		// now right
		if (x >= 2) {
			row = this.getRowIfFull(x, y, x - 2, y, currentField);
			this.addRow(result, row);
		}

		// now up and down
		if (currentField.getSize() - y >= 3) {
			row = this.getRowIfFull(x, y, x, y + 2, currentField);
			this.addRow(result, row);
		}

		if (y >= 2) {
			row = this.getRowIfFull(x, y, x, y - 2, currentField);
			this.addRow(result, row);
		}

		return result;
	}

	protected Cell[] getDiagonalRow(int sx, int sy, int fx, int fy, Field f) {
		Cell[] result = new Cell[3];
		for (int x = sx; x <= fx; ++x) {
			int y = sy + (int) Math.signum(fy - sy) * (x - sx);
			if (f.getCellAt(x, y).getMark() == this.playerMark)
				result[x - sx] = f.getCellAt(x, y);
			else return null;
		}
		return result;
	}

	protected void addRow(List result, Cell[] row) {
		if (row != null)
			result.add(row);
	}

	private Cell[] getRowIfFull(int sx, int sy, int fx, int fy, Field f) {
		Cell[] result = new Cell[(Math.abs(fx - sx) + 1) * (Math.abs(fy - sy) + 1)];
		int insertions = 0;
		for (int x = sx; (sx <= fx ? x <= fx : x >= fx); x = (fx >= sx ? x + 1 : x - 1)) {
			for (int y = sy; (sy <= fy ? y <= fy : y >= fy); y = (fy >= sy ? y + 1 : y - 1)) {
				if (f.getCellAt(x, y).getMark() == this.playerMark)
					result[insertions++] = f.getCellAt(x, y);
				else return null;
			}
		}
		return result;
	}

	public char getSymbol() {
		return playerMark;
	}
}
