package com.group4.main.model;

/**
 * @author darkwizard
 */
public class Field {
	Cell[][] field;
	private int numOfMarked = 0;
	public Cell getCellAt(int x, int y) {
		return field[x][y];
	}
	public void markCell(int x, int y, char mark) {
		numOfMarked++;
		field[x][y].setMark(mark);
	}
	public void unmarkCell(int x, int y) {
		numOfMarked--;
		field[x][y].setMark(Cell.DEFAULT_MARK);
	}

	public void printField() {
		for (int i = 0; i < field.length; ++i) {
			for (int j = 0; j < field[i].length; ++j)
				System.out.print("| " + this.getCellAt(i, j).getMark() + " ");
			System.out.println("|");
		}
	}

	public int getSize() {
		return field.length;
	}

	public Field(int size) {
		this.field = new Cell[size][size];
		for (int i = 0; i < size; ++i)
			for (int j = 0; j < size; ++j)
				this.field[i][j] = new Cell(i, j);
	}

	public int getNumOfMarked() {
		return numOfMarked;
	}

	public boolean isFilledCompletely() {
		return numOfMarked == getSize() * getSize();
	}
}
