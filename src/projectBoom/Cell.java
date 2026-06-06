package projectBoom;

public class Cell{
	private boolean isBoom;
	private boolean isRevealed;
	private boolean isFlagged;
	private int adjacentBoom;
	
	public Cell(){
		this.isBoom=false;
		this.isRevealed=false;
		this.isFlagged=false;
		this.adjacentBoom=0;
	}

	public boolean isBoom() {
		return isBoom;
	}

	public void setBoom(boolean isBoom) {
		this.isBoom = isBoom;
	}

	public boolean isRevealed() {
		return isRevealed;
	}

	public void setRevealed(boolean isRevealed) {
		this.isRevealed = isRevealed;
	}

	public boolean isFlagged() {
		return isFlagged;
	}

	public void setFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
	}

	public int getAdjacentBoom() {
		return adjacentBoom;
	}

	public void setAdjacentBoom(int adjacentBoom) {
		this.adjacentBoom = adjacentBoom;
	}

}



