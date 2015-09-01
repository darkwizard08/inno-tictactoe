package com.group4.main.model;

/**
 * @author darkwizard
 */
public class Cell {
	public static final char DEFAULT_MARK = ' ';
	private char mark = DEFAULT_MARK;
	private int x = 0;
	private int y = 0;

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setMark(char mark) {
		this.mark = mark;
	}

	public char getMark() {
		return mark;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "[" + x + ";" + y + "]";
	}
}
