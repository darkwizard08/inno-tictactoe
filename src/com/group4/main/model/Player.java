package com.group4.main.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author darkwizard
 */
public abstract class Player {
	char playerMark = '*';
	public abstract Cell makeTurn(Field currentField);

	public boolean isWinningTurn(Field currentField) {
		List<List<Cell>> list = null;
		boolean winning = false;

		// now checking if it's winning turn
		for (int i = 0; i < currentField.getSize(); ++i)
			for (int j = 0; j < currentField.getSize(); ++j) {
				list = this.getNumberOf3InRow(currentField, i, j);
				if (list.size() > 0)
					winning = true;
			}

		if (!winning) {
			// now checking diagonals
			this.addRow(list, this.getNumberOf3InDiagonal(0, 0, 2, 2, currentField, this.playerMark));
			this.addRow(list, this.getNumberOf3InDiagonal(0, 2, 2, 0, currentField, this.playerMark));
			if (list.size() > 0)
				winning = true;
		}

		return winning || currentField.isFilledCompletely();
	}

	public Player(char playerMark) {
		this.playerMark = playerMark;
	}

	protected List<List<Cell>> getNumberOf3InRow(Field currentField, int x, int y, char playerMark) {
		List<List<Cell>> result = new ArrayList<>();
		List<Cell> row;
		// checking left
		// if we can get a full row from (x, y) to the right with our marks, adding it.
		if (currentField.getSize() - x >= 3) {
			row = this.getRowByMark(x, y, x + 2, y, currentField, playerMark);
			this.addRow(result, row);
		}

		// now right
		if (x >= 2) {
			row = this.getRowByMark(x, y, x - 2, y, currentField, playerMark);
			this.addRow(result, row);
		}

		// now up and down
		if (currentField.getSize() - y >= 3) {
			row = this.getRowByMark(x, y, x, y + 2, currentField, playerMark);
			this.addRow(result, row);
		}

		if (y >= 2) {
			row = this.getRowByMark(x, y, x, y - 2, currentField, playerMark);
			this.addRow(result, row);
		}

		return result;
	}

	protected List<List<Cell>> getNumberOf3InRow(Field currentField, int x, int y) {
		return this.getNumberOf3InRow(currentField, x, y, this.playerMark);
	}

	protected List<Cell> getDiagonalRow(int sx, int sy, Field f) {
		int fx = (sx > 0) ? 0 : f.getSize() - 1;
		int fy = (sy > 0) ? 0 : f.getSize() - 1;

		if (fx < sx) {
			int tmp = fx;
			fx = sx;
			sx = tmp;
			tmp = sy;
			sy = fy;
			fy = tmp;
		}

		List<Cell> result = new ArrayList<>();
		for (int x = sx; x <= fx; ++x) {
			int y = sy + (int) Math.signum(fy - sy) * (x - sx);
			result.add(f.getCellAt(x, y));
		}
		return result;
	}

	protected List<Cell> getNumberOf3InDiagonal(int sx, int sy, int fx, int fy, Field f, char mark) {
		List<Cell> list = this.getDiagonalRow(sx, sy, f).stream()
				.filter(cell -> cell.getMark() == mark)
				.collect(Collectors.toList());
		if (list.size() < 3)
			return null;
		return list;
	}

	private void addRow(List result, List<Cell> row) {
		if (row != null)
			result.add(row);
	}

	private List<Cell> getPlayerRow(int sx, int sy, int fx, int fy, Field f) {
		return this.getRowByMark(sx, sy, fx, fy, f, this.playerMark);
	}

	protected List<Cell> getRow(int sx, int sy, int fx, int fy, Field f) {
		List<Cell> result = new ArrayList<>();
		for (int x = sx; (sx <= fx ? x <= fx : x >= fx); x = (fx >= sx ? x + 1 : x - 1)) {
			for (int y = sy; (sy <= fy ? y <= fy : y >= fy); y = (fy >= sy ? y + 1 : y - 1)) {
				result.add(f.getCellAt(x, y));
			}
		}
		return result;
	}

	private List<Cell> getRowByMark(int sx, int sy, int fx, int fy, Field f, char mark) {
		List<Cell> result = this.getRow(sx, sy, fx, fy, f).stream()
				.filter(cell -> cell.getMark() == mark)
				.collect(Collectors.toList());
		if (result.size() < 3)
			return null;
		return result;
	}

	public char getSymbol() {
		return playerMark;
	}
}
