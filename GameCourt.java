package Tetris_ver2_storage;

/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 * 
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {
	
	private Block fallingBlock; // currently falling block
	private boolean blockSettled = false; // whether the block is settled and need a new block 
	private boolean gamePaused = false; // whether the game is paused
	private boolean gamePlaying = false; // whether the game is running
	private boolean previewOn = true;
	private JLabel score; // Current score text
	private JLabel status; // Current status text (i.e. Running...)
	private JLabel warn; // Current warning text
	private int currentScore; // the number of line deleted
	// Game constants
	private static final int gridColumn = 10;
	private static final int gridRow = 22;
	private static final int INTERVAL = 10;
	private int Xcoord;
	private int Ycoord;
	private Timer timer;
	
	private Block.shape[] grid;
	private Block.shape[] storage;
	
	public GameCourt(JLabel score, JLabel status, JLabel warning) {
		// creates border around the court area, JComponent method
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		grid = new Block.shape[gridColumn * gridRow];
		fallingBlock = new Block();
		currentScore = 0;
		Xcoord = gridColumn / 2;
		Ycoord = 0;
		gamePlaying = true;
		blockSettled = false;
	    storage = new Block.shape[1]; 
		
		// Timer, at each "tick", either drop one line or generate a new block
		timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (blockSettled) {
					blockSettled = false;
					newBlock();
				}
				else {
					dropOneLine();
				}
			}
		});
		timer.start(); // MAKE SURE TO START THE TIMER!

		setFocusable(true);

		// taking care of keyboard inputs
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyInput = e.getKeyCode();
				boolean moved = false;
				
				if (keyInput == 'r' || keyInput == 'R') {
					start();
					return;
				}
				
				if (!gamePlaying ||
						fallingBlock.getShape() == Block.shape.Empty) {
					return;
				}
				
				if (keyInput == 'p' || keyInput == 'P') {
					pause();
					return;
				}
				
				if(gamePaused) {
					return;
				}
				
				switch(keyInput) {
				case KeyEvent.VK_LEFT:
					moved = Move(fallingBlock, Xcoord - 1, Ycoord);
					if (!moved) {
						leftShiftWarning();
					}
					break;
				case KeyEvent.VK_RIGHT:
					moved = Move(fallingBlock, Xcoord + 1, Ycoord);
					if (!moved) {
						rightShiftWarning();
					}
					break;
				case KeyEvent.VK_DOWN:
					moved = Move(fallingBlock.rotateLeft(), Xcoord, Ycoord);
					if (!moved) {
						leftRotationWarning();
					}
					break;
				case KeyEvent.VK_UP:
					moved = Move(fallingBlock.rotateRight(), Xcoord, Ycoord);
					if (!moved) {
						rightRotationWarning();
					}
					break;
				case KeyEvent.VK_SPACE:
					allTheWayDown();
					break;
				case KeyEvent.VK_SHIFT:
					storage();
					break;	
				case 'd':
					dropOneLine();
					break;
				case 'D':
					dropOneLine();
					break;
				case 'v':
					previewOn = !previewOn;
					break;
				case 'V':
					previewOn = !previewOn;
					break;
				}
			}
		});
		
		this.warn = warning;
		this.status = status;
		this.score = score;
		newBlock();
		cleanUp();
	}
	
	public void storage() {
		if (storage[0] == null) {
			storage[0] = fallingBlock.getShape();
			fallingBlock.setRandomShape();
			return;
		}
		Block.shape temp = fallingBlock.getShape();
		fallingBlock.setShape(storage[0]);
		storage[0] = temp;
		
		return;
	}
	
	public void leftRotationWarning() {
		this.warn.setText("Can't turn left!");
	}
	
	public void rightRotationWarning() {
		this.warn.setText("No right rotation!");
	}
	
	public void leftShiftWarning() {
		this.warn.setText("No more left!");
	}
	
	public void rightShiftWarning() {
		this.warn.setText("Nowhere to go on right!");
	}
	
	// start the game
	public void start() {
		if (gamePaused) {
			return;
		}
		Xcoord = gridColumn / 2;
		Ycoord = (int)getSize().getHeight() - gridRow * gridHeight() + 1 - this.fallingBlock.getYMin();
		gamePlaying = true;
		blockSettled = false;
		this.currentScore = 0;
		this.score.setText(String.valueOf(currentScore));
		cleanUp();
		newBlock();
		status.setText("Playing...");
		timer.setDelay(1000);
		timer.start();
		requestFocusInWindow();
		storage[0] = null;
	}
	
	// width of each grid
	public int gridWidth() {
		return (int) this.getSize().getWidth() / gridColumn;
	}
	
	// height of each grid
	public int gridHeight() {
		return (int) this.getSize().getHeight() / gridRow;
	}
	
	// shape of block stored in each grid -> used to color each grid
	public Block.shape shapeOfGrid(int x, int y) {
		return grid[y * gridColumn + x];
	}
	
	// pause the game
	public void pause() {
		
		if (!gamePlaying) {
			return;
		}
		
		gamePaused = !gamePaused;
		if (gamePaused) {
			timer.stop();
			this.status.setText("Paused...");
		}
		else {
			timer.start();
			this.status.setText("Playing...");
		}

		repaint();
	}
	
	// generate a new block
	public void newBlock() {
		this.fallingBlock.setRandomShape();
		Xcoord = gridColumn / 2;
		Ycoord = 1 - this.fallingBlock.getYMin();
		
		if(!Move(fallingBlock, Xcoord, Ycoord) && (Ycoord < 2)) {
			gamePlaying = false;
			this.fallingBlock.setShape(Block.shape.Empty);
			status.setText("Game Over");
			timer.stop();
		}
	}
	
	// drop - move a block down to Ycoord
	public void drop() {
		for (int i = 0; i < 4; i++) {
			int x = this.Xcoord + this.fallingBlock.getX(i);
			int y = this.Ycoord - this.fallingBlock.getY(i);
			grid[x + y * gridColumn] = this.fallingBlock.getShape();
		}
		lineCheck();
		if(!blockSettled) {
			newBlock();
		}
	}
	
	// Adjust Ycoord to Ycoord - 1
	public void dropOneLine() {
		if (!Move(fallingBlock, Xcoord, Ycoord + 1)) {
			drop();
		}
	}
	
	// Adjust Ycoord to the very bottom it can possibly go down
	public void allTheWayDown() {
		int y = Ycoord;
		
		while(y < gridRow) {
			if(!Move(fallingBlock, Xcoord, y + 1)) {
				break;
			}	
			y++;
		}
		drop();
	}
	
	// Check for full lines, clear them and add up to score
	public void lineCheck() {
		int fullLine = 0;
		for (int i = 0; i < gridRow; i++) {
			boolean isLineFull = true;
			for (int j = 0; j < gridColumn; j++) {
				if (shapeOfGrid(j, i) == Block.shape.Empty) {
					isLineFull = false;
					break;
				}
			}
			if (isLineFull) {
				fullLine++;
				for (int k = i; k > 0; k--) {
					for (int l = 0; l < gridColumn; l++) {
						grid[gridColumn * k + l] = shapeOfGrid(l, k - 1);
					}
				}
			}		
		}
		if (fullLine > 0) {
			int multiplier = 0;
			switch(fullLine) {
			case 1:
				multiplier = 1000;
				break;
			case 2:
				multiplier = 1100;
				break;
			case 3:
				multiplier = 1200;
				break;
			case 4:
				multiplier = 1300;
				break;
			}
			this.currentScore += fullLine * multiplier;
			if (currentScore > 10000) { 
				this.timer.setDelay(100);
			}
			else if (currentScore > 7500) {
				this.timer.setDelay(400);
			}
			else if (currentScore > 5000) {
				this.timer.setDelay(500);
			}
			else if (currentScore > 2500) {
				this.timer.setDelay(750);
			}
			else if (currentScore > 1000) {
				this.timer.setDelay(900);
			}
			this.score.setText(String.valueOf(currentScore));
			blockSettled = true;
			this.fallingBlock.setShape(Block.shape.Empty);
			repaint();
		}	
	}
	
	// Check whether the block can move at given x, y coordinates and
	// if true, adjust Xcoord and Ycoord
	public boolean Move(Block b, int x, int y) {
		
		for (int i = 0; i < 4; i++) {
			int XSpace = x + b.getX(i);
			int YSpace = y - b.getY(i);
			
			if (XSpace < 0 || XSpace >= gridColumn
					|| YSpace < 0 || YSpace >= gridRow) {
				return false;
			}
			
			if (shapeOfGrid(XSpace, YSpace) != Block.shape.Empty) {
				return false;
			}
		}
		
		this.fallingBlock = b;
		this.Xcoord = x;
		this.Ycoord = y;
		
		repaint();
		return true;
	}
	
	public int yForPreview(Block b, int x, int y) {
		int minY = 1000;
		for (int i = 0; i < 4; i++) {
			int XSpace = x + b.getX(i);
			int YSpace = y - b.getY(i);
			
			while (shapeOfGrid(XSpace, YSpace) == Block.shape.Empty && YSpace <= gridRow) {
				YSpace++;
			}
			minY = Math.min(minY, YSpace);
		}
		
		return minY;
	}
	
	// To update the entire picture of game each time, cleanup
	public void cleanUp() {
		for (int i = 0; i < gridColumn * gridRow; i++) {
			grid[i] = Block.shape.Empty;
		}
	}
	
	// Draw blocks with square unit
	public void draw(Graphics g, int x, int y, Block.shape s) {
		Color[] colors = new Color[]{
			Color.white, Color.red, Color.yellow, Color.magenta, Color.gray, Color.green,
			Color.orange, Color.cyan};
		
		Color blockColor = colors[s.ordinal()];
		g.setColor(blockColor);
		g.fillRect(x, y, gridWidth(), gridHeight());
		g.setColor(Color.black);
		g.drawRect(x, y, gridWidth(), gridHeight());
	}
	
	public void drawPreview(Graphics g, int x, int y, Block.shape s) {
		Color[] colors = new Color[]{
			Color.white, Color.red, Color.yellow, Color.magenta, Color.gray, Color.green,
			Color.orange, Color.cyan};
		
		Color blockColor = colors[s.ordinal()];
		g.setColor(blockColor);
		g.setColor(blockColor.darker());
		g.drawRect(x, y, gridWidth(), gridHeight());
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Dimension size = this.getSize();
		int gridTop = (int) size.getHeight() - gridRow * gridHeight();
		g.setColor(Color.red);
		g.drawLine(0, 2 * gridHeight(), (int) size.getWidth(), 2 * gridHeight());
		
		for (int i = 0; i < gridRow; i++) {
			for (int j = 0; j < gridColumn; j++) {
				Block.shape s = shapeOfGrid(j, gridRow - i - 1);
				if(s != Block.shape.Empty) {
					draw(g, j * gridWidth(), gridTop + (gridRow - i - 1) * gridHeight(), s);
				}
			}
		}
		
		if (this.fallingBlock.getShape() != Block.shape.Empty) {
			int yLimit = 1000;  // Just a big number
			for (int i = 0; i < 4; i++) {
				int x = Xcoord + fallingBlock.getX(i);
				int y = Ycoord - fallingBlock.getY(i);
				int measureY = y;
				while (measureY < gridRow) {
					if(shapeOfGrid(x, measureY) != Block.shape.Empty) break;
					measureY++;
				}
				if (yLimit > measureY - 1 - y) {
					yLimit = measureY - 1 - y;
				}
			}
			for (int i = 0; i < 4; i++) {
				int x = Xcoord + fallingBlock.getX(i);
				int y = Ycoord - fallingBlock.getY(i);
				int yPreview = y + yLimit;
				draw(g, x * gridWidth(), y *
						gridHeight(), fallingBlock.getShape());
				if (previewOn) {
					drawPreview(g, x * gridWidth(), (int)size.getHeight() - (gridRow - yPreview) * gridHeight()
							, fallingBlock.getShape());
				}
			}
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(gridColumn, gridRow);
	}
}
