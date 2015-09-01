package com.group4.main.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author darkwizard
 */
public class BotPlayer extends Player {
	private Field game = null;
	boolean secretOperationOfSettingX = false;

	@Override
	public Cell makeTurn(Field currentField) {
		this.game = currentField;
		if (this.playerMark == 'X')
			return makeXTurn();
		return makeOTurn();
	}

	private Cell makeXTurn() {
		char opponent = this.getOpponentMark();
		if (game.getCellAt(1, 1).getMark() == Cell.DEFAULT_MARK) {
			// middle is free, place our mark there
			log("Choosing middle");
			game.markCell(1, 1, this.playerMark);
			return game.getCellAt(1, 1);
		} else {
			// okay, it is not the first turn;
			// let's find opponent place
			List<Cell> cells = this.getPlayerCornerCells(opponent);
			if (cells.size() == 0 || secretOperationOfSettingX) {
				// wow, stupid human didn't put his O in the corner!
				secretOperationOfSettingX = true;
				cells = this.getPlayerCells(opponent);
				if (cells.size() == 1) {
					log("Haha, you've already lost!");
					return this.addToCorner(cells.get(0));
				} else {
					// need to find another corner, which has two free neighbours
					log("Searching for corner with free neighbours...");
					cells = this.getFreeCornerCells();
					for (Cell cell : cells) {
						int x = cell.getX(), y = cell.getY();
						List<Cell> nbrs = this.getNeighbours(x, y);
						boolean suits = true;
						for (Cell c: nbrs)
							if (c.getMark() != Cell.DEFAULT_MARK)
								suits = false;
						if (suits) {
							log("Found!", cell);
							this.game.markCell(x, y, this.playerMark);
							secretOperationOfSettingX = false;
							return cell;
						}
					}
				}
			} else {
				// okay, O is in the corner; let's count number of turns to make decision
				cells = this.getPlayerCells(opponent);
				if (cells.size() > 1) {
					// the only option to win is hope :)
					// human couldn't put his O at the dangerous corner
					// or made some other mistake
					// then we take it and win
					Cell attack = this.aggressiveAttack();
					if (attack != null)
						return attack;

					// if we're here, human is not so stupid.
					// it's probably draw
					log("Wow, looks like draw...");
					// just finding dangerous cells, and blocking them
					// if we haven't got them, placing randomly, we can't win here
					Cell defense = this.defendYourLands(opponent);
					if (defense != null)
						return defense;
					// finally, we're here. So we can't win in one turn, and can't block opponent
					// just going randomly :)
					Cell lastHope = this.getFreeCells().stream().findAny().get();
					this.game.markCell(lastHope.getX(), lastHope.getY(), this.playerMark);
					return lastHope;

				} else {
					// one turn passed, working out the strategy
					// placing our mark in the corner with same X or Y
					Cell oppCell = cells.get(0);
					return addToCorner(oppCell);
				}
			}
		}
		return null;
	}

	private Cell addToCorner(Cell oppCell) {
		List<Cell> cells = this.getFreeCornerCells();
		log("We have these free corners: ", cells);
		Cell c = cells.stream()
				.filter(cell -> cell.getX() == oppCell.getX() || cell.getY() == oppCell.getY())
				.findAny().get();
		log("Marking", c);
		this.game.markCell(c.getX(), c.getY(), this.playerMark);
		return c;
	}

	private Cell aggressiveAttack() {
		List<Cell> cells = this.getFreeCells();
		for (Cell cell : cells) {
			int x = cell.getX(), y = cell.getY();
			this.game.markCell(x, y, this.playerMark);
			boolean result = this.isPlayerWins(this.playerMark);
			log("Trying to put mark in", cell, "result: ", result);
			if (result) {
				log("Stupid human! Suffer because of my win!");
				return cell;
			}
			this.game.unmarkCell(x, y);
		}
		return null;
	}

	private Cell defendYourLands(char opponent) {
		List<Cell> cells = this.getFreeCells();
		for (Cell cell : cells) {
			int x = cell.getX(), y = cell.getY();
			this.game.markCell(x, y, opponent);
			if (this.isPlayerWins(opponent)) {
				this.game.unmarkCell(x, y);
				this.game.markCell(x, y, this.playerMark);
				log("Found dangerous cell, closing...");
				return cell;
			}
			this.game.unmarkCell(x, y);
		}
		return null;
	}

	private List<Cell> getNeighbours(int x, int y) {
		List<Cell> result = new ArrayList<>();
		int[] dx = {-1, 0, 1, 0};
		int[] dy = {0, 1, 0, -1};
		for (int l = 0; l < dx.length; ++l) {
			int nx = x + dx[l], ny = y + dy[l];
			if (nx >= 0 && nx < this.game.getSize() && ny >= 0 && ny < this.game.getSize())
				result.add(this.game.getCellAt(nx, ny));
		}
		return result;
	}

	private Cell makeOTurn() {
		char opponent = this.getOpponentMark();
		List<Cell> cells = this.getPlayerCells(opponent);
		if (this.game.getCellAt(1, 1).getMark() == Cell.DEFAULT_MARK) {
			// oO, somebody very strange plays this game
			log("You disappointed me :(");
			this.game.markCell(1, 1, this.playerMark);
			return this.game.getCellAt(1, 1);

		} else {
			// more default pattern, where 'X' takes middle
			if (cells.size() == 1) {
				Cell corner = this.getFreeCornerCells().stream().findAny().get();
				this.game.markCell(corner.getX(), corner.getY(), this.playerMark);
				log("Taking corner to be a good boy");
				return corner;
				// taking random corner :)
			} else {
				// checking one interesting thing
				cells = this.getPlayerCornerCells(opponent);
				if (cells.size() == this.getPlayerCells(opponent).size()) {
					// checking, if they are in distinct corners
					Cell x0 = cells.get(0);
					Cell x1 = cells.get(1);
					if (x0.getX() != x1.getX() && x0.getY() != x1.getY()) {
						// owow, you wanted to cheat me, nice try :)
						Cell selection = this.getFreeCells().stream()
								.filter(cell -> cell.getX() % 2 != 0 || cell.getY() % 2 != 0)
								.findAny()
								.get();
						log("Wanted to cheat me, nice try :)");
						this.game.markCell(selection.getX(), selection.getY(), this.playerMark);
						return selection;
					}
				}
				// just trying, maybe it helps?
				Cell attack = this.aggressiveAttack();
				if (attack != null)
					return attack;

				// okay, defending is my profession
				// just defending, until the very end.
				Cell defense = this.defendYourLands(opponent);
				if (defense != null)
					return defense;
				else {
					// wow, nothing to defence? Go attacking then :)
					Cell randomAttack = this.getFreeCells().stream()
							.findAny().get();
					this.game.markCell(randomAttack.getX(), randomAttack.getY(), this.playerMark);
					log("I've randomly chosen the", randomAttack);
					return randomAttack;
				}
			}
		}
	}

	private boolean isMarkBlockingLine(char blockingMark, char lineMark, int sx, int sy, int fx, int fy) {
		// count, we're working with diagonal or straight line
		int totalMarks = (Math.abs(sx - fx) + 1) * (Math.abs(sx - fx) + 1);
		int blockingCells = 0;
		int lineCells = 0;
		List<Cell> row;

		if (totalMarks == 3)
			// straight line
			row = this.getRow(sx, sy, fx, fy, this.game);

		else if (totalMarks == 9)
			// diagonal
			row = this.getDiagonalRow(sx, sy, this.game);

		else {
			throw new Error("Unsupported command!");
		}

		for (Cell c : row)
			if (c.getMark() == blockingMark)
				blockingCells++;
			else if (c.getMark() == lineMark)
				lineCells++;

		return blockingCells > 0 && lineCells > 0;
	}

	private boolean isMarkBlockingDiagonal(char blockingMark, char lineMark, int sx, int sy) {
		int fx = (sx > 0) ? 0 : this.game.getSize() - 1;
		int fy = (sy > 0) ? 0 : this.game.getSize() - 1;
		return this.isMarkBlockingLine(blockingMark, lineMark, sx, sy, fx, fy);
	}

	private boolean isDiagonalCompleted(int sx, int sy, char playerMark) {
		List<Cell> res = this.getNumberOf3InDiagonal(sx, sy, -1, -1, this.game, playerMark);
		return res != null;
	}

	private boolean isRowCompleted(int sx, int sy, int fx, int fy, char playerMark) {
		return true;
	}

	private boolean isPlayerWins(char playerMark) {
		List<List<Cell>> tmp;
		boolean result = false;
		for (int i = 0; i < this.game.getSize(); ++i)
			for (int j = 0; j < this.game.getSize(); ++j) {
				tmp = this.getNumberOf3InRow(this.game, i, j, playerMark);
				if (tmp.size() > 0) {
					result = true;
				}
			}
		result = result	|| this.isDiagonalCompleted(0, 0, playerMark)
						|| this.isDiagonalCompleted(0, 2, playerMark);
		return result;
	}

	private List<Cell> getCellsByMark(char mark) {
		List<Cell> result = new ArrayList<>();
		for (int i = 0; i < game.getSize(); ++i)
			for (int j = 0; j < game.getSize(); ++j)
				if (game.getCellAt(i, j).getMark() == mark)
					result.add(game.getCellAt(i, j));
		return result;
	}

	// ===================== FREE ======================================
	private List<Cell> getFreeCells() {
		return this.getCellsByMark(Cell.DEFAULT_MARK);
	}

	private List<Cell> getFreeCornerCells() {
		return this.getFreeCells().stream()
				.filter(c -> c.getX() % 2 == 0 && c.getY() % 2 == 0)
				.collect(Collectors.toList());
	}
	// -----------------------------------------------------------------

	// ====================== OPPONENT =================================
	private List<Cell> getPlayerCornerCells(char playerMark) {
		return this.getPlayerCells(playerMark).stream()
				.filter(cell -> cell.getX() % 2 == 0 && cell.getY() % 2 == 0)
				.collect(Collectors.toList());
	}

	private List<Cell> getPlayerCells(char playerMark) {
		return this.getCellsByMark(playerMark);
	}
	// -----------------------------------------------------------------

	public void log(Object... args) {
		System.out.print("[BOT:] ");
		for (Object s : args)
			System.out.print(s + " ");
		System.out.println();
	}

	public BotPlayer(char playerMark) {
		super(playerMark);
	}

	public char getOpponentMark() {
		return this.playerMark == 'X' ? 'O' : 'X';
	}
}
