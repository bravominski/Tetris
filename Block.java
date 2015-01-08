package Tetris_ver2_storage;

/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */


// Class Block for Tetris

public class Block {

	// Possible shapes of Block
	enum shape {Empty, ZBlock, SBlock, LineBlock,
		TBlock, SquareBlock, LBlock, ReverseLBlock};
	private shape currentBlock;
	private int[][] blockInCoord;
	 
	
	// Constructor
	public Block() {
		this.blockInCoord = new int[4][2];
		setShape(shape.Empty);
	}

	// Setting a shape for a given block
	public void setShape(shape blockShape) {
		int[][] fourByTwo;
		switch(blockShape) {
		case Empty:
			fourByTwo = new int[][] {{0,0}, {0,0}, {0,0}, {0,0}}; // Empty
			break;
		case ZBlock:
			fourByTwo = new int[][] {{0,-1}, {0,0}, {1,0}, {1,1}}; // Z
			break;
		case SBlock:
			fourByTwo = new int[][] {{0,-1}, {0,0}, {-1,0}, {-1,1}}; // S
			break;
		case LineBlock:
			fourByTwo = new int[][] {{0,-1}, {0,0}, {0,1}, {0,2}}; // Line
			break;
		case TBlock:
			fourByTwo = new int[][] {{0,-1}, {0,0}, {-1,0}, {1,0}}; // T
			break;
		case SquareBlock:
			fourByTwo = new int[][] {{0,0}, {0,1}, {1,0}, {1,1}}; // Square
			break;
		case LBlock:
			fourByTwo = new int[][] {{1,-1}, {0,-1}, {0,0}, {0,1}}; // L
			break;
		case ReverseLBlock:
			fourByTwo = new int[][] {{-1,-1}, {0,-1}, {0,0}, {0,1}};  // Reverse L
			break;
		default:
			fourByTwo = new int[][] {{0,0}, {0,0}, {0,0}, {0,0}}; // Empty
			break;
		}
		
		this.blockInCoord = fourByTwo;
		this.currentBlock = blockShape;
	}
	
	// Obtain an x value of the block in 4 x 2 array representation of it
	public int getX(int index) {
		return this.blockInCoord[index][0];
	}
	
	// Obtain an y value of the block in 4 x 2 array representation of it
	public int getY(int index) {
		return this.blockInCoord[index][1];
	}
	
	// Obtain the smallest y value of the block in 4 x 2 array representation of it
	public int getYMin(){
		int min = this.blockInCoord[0][1];
		for (int i = 1; i < 4; i++) {
			min = Math.min(min, this.blockInCoord[i][1]);
		}
		return min;
	}

	// return the shape of the given block
    public shape getShape() {
    	return currentBlock;
    }
    
    // set the shape of the given block at random
    public void setRandomShape() {
    	int i = (int)(Math.random() * 7) % 7 + 1;
    	setShape(shape.values()[i]); // Set currentBlock to random shape,
    	                             // index 0, Empty is not an option
    }
    
    // Left-rotate the given block
    public Block rotateLeft() {
    	if (currentBlock == shape.SquareBlock) {
    		return this;
    	}
    	Block rotated = new Block();
    	rotated.currentBlock = this.currentBlock;
    	for (int i = 0; i < 4; i++) {
    		rotated.blockInCoord[i][0] = -this.blockInCoord[i][1];
    		rotated.blockInCoord[i][1] = this.blockInCoord[i][0];
    	}
    	return rotated;
    }
  
    // Right-rotate the given block
    public Block rotateRight() { 
    	if (currentBlock == shape.SquareBlock) {
    		return this;
    	}
    	Block rotated = new Block();
    	rotated.currentBlock = this.currentBlock;
    	for (int i = 0; i < 4; i++) {
    		rotated.blockInCoord[i][0] = this.blockInCoord[i][1];
    		rotated.blockInCoord[i][1] = -this.blockInCoord[i][0];
    	}
    	return rotated;
    }
}
