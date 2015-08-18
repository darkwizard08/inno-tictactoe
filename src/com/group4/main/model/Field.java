package com.group4.main.model;

/**
 * @author darkwizard
 */
public class Field {
	Cell[][] field;
	public Cell getCellAt(int x, int y) {
		return field[x][y];
	}
}
