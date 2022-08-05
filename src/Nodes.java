import java.awt.image.BufferedImage;

/**
 * Nodes class for each square on the board
 * 
 * @date June 2022
 * @author Jonathan Zhu
 */
public class Nodes {
	// Initiate variables
	private int x, y, w, h;
	private BufferedImage image, discoveredImage;
	private boolean isBomb, visited, isBlank, isFlagged;

	public Nodes(int x, int y, int w, int h, BufferedImage image) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.image = image;
		isBomb = false;
		visited = false;
		isBlank = true;
		isFlagged = false;
	}

	/**
	 * Method to set value for isBomb
	 */
	public void setIsBomb(boolean bool) {
		this.isBomb = bool;
	}

	/**
	 * Method to get the boolean value of isBomb
	 */
	public boolean getBombStatus() {
		return this.isBomb;
	}

	/**
	 * Method to set the image displayed when the node is clicked
	 */
	public void setDiscoveredImage(BufferedImage image) {
		this.discoveredImage = image;
	}

	/**
	 * Method to set the image displayed when the node is clicked
	 */
	public BufferedImage getDiscoveredImage() {
		return this.discoveredImage;
	}

	/**
	 * Method to set the value for isFlagged
	 */
	public void setIsFlagged(boolean bool) {
		this.isFlagged = bool;
	}

	/**
	 * Method to get the value of isFlagged
	 */
	public boolean getFlaggedStatus() {
		return this.isFlagged;
	}

	/**
	 * Method to set the value for isBlank
	 */
	public void setIsBlank(boolean bool) {
		this.isBlank = bool;
	}

	/**
	 * Method to get the value of isBlank
	 */
	public boolean getBlankStatus() {
		return this.isBlank;
	}

	/**
	 * Method to set the value for isVisited
	 */
	public void setVisited(boolean bool) {
		this.visited = bool;
	}

	/**
	 * Method to get the value of isVisited
	 */
	public boolean getVisitedStatus() {
		return this.visited;
	}

	/**
	 * Method to get the x position of the node when painted onto the GamePanel
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Method to get the y position of the node when painted onto the GamePanel
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Method to get the width of the node when painted onto the GamePanel
	 */
	public int getW() {
		return this.w;
	}

	/**
	 * Method to get the height of the node when painted onto the GamePanel
	 */
	public int getH() {
		return this.h;
	}

	/**
	 * Method to set the x position of the node
	 */
	public void setX(int newX) {
		this.x = newX;
	}

	/**
	 * Method to set the y position of the node
	 */
	public void setY(int newY) {
		this.y = newY;
	}

	/**
	 * Method to set the width of the node when pained
	 */
	public void setW(int newW) {
		this.w = newW;
	}

	/**
	 * Method to set the height of the node when pained
	 */
	public void setH(int newH) {
		this.h = newH;
	}

	/**
	 * Method to get the image that the node is currently displaying
	 */
	public BufferedImage getImage() {
		return this.image;
	}

	/**
	 * Method to set the image that the node is currently displaying
	 */
	public void setImage(BufferedImage newImage) {
		this.image = newImage;
	}

	/**
	 * Method to check if the mouse is inside of a node
	 */
	public boolean contains(int mouseX, int mouseY) {
		int nw = this.w;
		int nh = this.h;
		int mw = 1;
		int mh = 1;
		if (mw <= 0 || mh <= 0 || nw <= 0 || nh <= 0) {
			return false;
		}
		int nx = this.x;
		int ny = this.y;
		int mx = mouseX;
		int my = mouseY;
		mw += mx;
		mh += my;
		nw += nx;
		nh += ny;
		return ((mw < mx || mw > nx) && (mh < my || mh > ny) && (nw < nx || nw > mx) && (nh < ny || nh > my));
	}
	
}
