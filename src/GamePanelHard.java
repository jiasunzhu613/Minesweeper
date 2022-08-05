import java.awt.*;
import javax.sound.sampled.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * GamePanel displaying the hard difficulty for MINEWSWEEPER
 * 
 * @date June 2022
 * @author Jonathan Zhu
 */
public class GamePanelHard extends GamePanel implements Runnable {
	// Initiate variables
	private Nodes[][] nodes;
	private ArrayList<ArrayList<Integer>> bombLocation;
	private int[] rowMatrix, colMatrix;
	private BufferedImage facingDown, facingDownHover, blank, oneNear, twoNear, threeNear, fourNear, fiveNear, sixNear,
			sevenNear, eightNear, bomb, flagged, stopwatch, trophy, newGame, levels, board;
	private BufferedImage[] numberImages;
	private boolean running, firstClick, gameLost, gameWon, viewingBoard;
	private Thread panelThread;
	private File initialDiscoverFile, discoverFile, flagFile, unflagFile, loseFile, winFile;
	private AudioInputStream initialDiscoverStream, discoverStream, flagStream, unflagStream, loseStream, winStream;
	private Clip initialDiscover, discover, flag, unflag, lose, win;
	private final int NODEWIDTH, NODEHEIGHT, NUMROWS, NUMCOLS, NUMBOMBS;

	public GamePanelHard() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		NODEWIDTH = 30;

		NODEHEIGHT = 28;

		NUMROWS = 20;

		NUMCOLS = 24;

		NUMBOMBS = 99;

		/**
		 * 2D Nodes array containing each node on the board
		 */
		nodes = new Nodes[NUMROWS][NUMCOLS];

		rowMatrix = new int[] { -1, -1, -1, 0, 0, 1, 1, 1 };

		colMatrix = new int[] { -1, 0, 1, -1, 1, -1, 0, 1 };

		bombLocation = new ArrayList<>();

		flagCounter = 99;

		numberImages = new BufferedImage[9];

		facingDown = ImageIO.read(new File("Images/facingDown.png"));

		facingDownHover = ImageIO.read(new File("Images/facingDownHover.png"));

		blank = ImageIO.read(new File("Images/0.png"));
		numberImages[0] = blank;

		oneNear = ImageIO.read(new File("Images/1.png"));
		numberImages[1] = oneNear;

		twoNear = ImageIO.read(new File("Images/2.png"));
		numberImages[2] = twoNear;

		threeNear = ImageIO.read(new File("Images/3.png"));
		numberImages[3] = threeNear;
		
		fourNear = ImageIO.read(new File("Images/4.png"));
		numberImages[4] = fourNear;

		fiveNear = ImageIO.read(new File("Images/5.png"));
		numberImages[5] = fiveNear;

		sixNear = ImageIO.read(new File("Images/6.png"));
		numberImages[6] = sixNear;

		sevenNear = ImageIO.read(new File("Images/7.png"));
		numberImages[7] = sevenNear;

		eightNear = ImageIO.read(new File("Images/8.png"));
		numberImages[8] = eightNear;

		bomb = ImageIO.read(new File("Images/bomb.png"));

		flagged = ImageIO.read(new File("Images/flagged.png"));

		stopwatch = ImageIO.read(new File("Images/stopwatch.png"));

		trophy = ImageIO.read(new File("Images/trophy.png"));

		newGame = ImageIO.read(new File("Images/newGame.png"));

		levels = ImageIO.read(new File("Images/levels.png"));

		board = ImageIO.read(new File("Images/board.png"));

		running = true;

		firstClick = true;

		gameWon = false;

		gameLost = false;

		viewingBoard = false;

		for (int row = 0; row < NUMROWS; row++) {
			for (int col = 0; col < NUMCOLS; col++) {
				nodes[row][col] = new Nodes(col * NODEWIDTH, row * NODEHEIGHT, NODEWIDTH, NODEHEIGHT, facingDown);
			}
		}

		/**
		 * All audio retrieved from Google's Minesweeper 
		 */
		initialDiscoverFile = new File("Audio/initialDiscover.wav");

		discoverFile = new File("Audio/discover.wav");

		flagFile = new File("Audio/flag.wav");

		unflagFile = new File("Audio/unflag.wav");

		loseFile = new File("Audio/loseAudio.wav");

		winFile = new File("Audio/winAudio.wav");

		// Retrieved from https://www.youtube.com/watch?v=oLirZqJFKPE at 28:06
		panelThread = new Thread(this);
		panelThread.start();

		this.setBounds(90, 150, 720, 700);
		this.setBackground(null);
		ClickListener clickListener = new ClickListener();
		MoveListener moveListener = new MoveListener();
		this.addMouseMotionListener(moveListener);
		this.addMouseListener(clickListener);

	}

	/**
	 * Method that sets up the images each node will display if they are clicked
	 */
	public void setDiscoveredImage() {
		for (int row = 0; row < NUMROWS; row++) {
			for (int col = 0; col < NUMCOLS; col++) {
				int bombsAdj = 0;
				for (int i = 0; i < 8; i++) {
					int newRow = row + rowMatrix[i];
					int newCol = col + colMatrix[i];
					if (newRow < 0 || newRow > NUMROWS - 1 || newCol < 0 || newCol > NUMCOLS - 1) {
						continue;
					}
					if (nodes[newRow][newCol].getBombStatus()) {
						bombsAdj++;
					}
				}
				nodes[row][col].setDiscoveredImage(numberImages[bombsAdj]);
			}
		}
	}

	/**
	 * Method that randomly chooses positions to place bombs around the node the
	 * user begun on
	 * 
	 * @param r (row coordinate of the node that the user begun on)
	 * @param c (column coordinate of the node that the user begun on)
	 */
	public void setBombs(int r, int c) {
		while (bombLocation.size() < NUMBOMBS) {
			int row = (int) Math.floor((NUMROWS - 1) * Math.random());
			int col = (int) Math.floor((NUMCOLS - 1) * Math.random());
			ArrayList<Integer> temp = new ArrayList<>();
			if (row == r && col == c) {
				continue;
			} else if (row == r - 1 && col == c - 1) {
				continue;
			} else if (row == r - 1 && col == c) {
				continue;
			} else if (row == r - 1 && col == c + 1) {
				continue;
			} else if (row == r && col == c - 1) {
				continue;
			} else if (row == r && col == c + 1) {
				continue;
			} else if (row == r + 1 && col == c - 1) {
				continue;
			} else if (row == r + 1 && col == c) {
				continue;
			} else if (row == r + 1 && col == c + 1) {
				continue;
			}
			temp.add(row);
			temp.add(col);
			if (!bombLocation.contains(temp)) {
				bombLocation.add(temp);
				nodes[row][col].setIsBomb(true);
				nodes[row][col].setIsBlank(false);
				for (int j = 0; j < 8; j++) {
					int newRow = row + rowMatrix[j];
					int newCol = col + colMatrix[j];
					if (newRow < 0 || newRow > NUMROWS - 1 || newCol < 0 || newCol > NUMCOLS - 1) {
						continue;
					}
					nodes[newRow][newCol].setIsBlank(false);
				}
			}
		}
	}

	/**
	 * Method that discovers the reachable nodes of a node that was clicked (using
	 * DFS/depth-first search)
	 * 
	 * @param node, node that was clicked
	 * @param row   (row coordinate of the node that the user clicked)
	 * @param col   (column coordinate of the node that the user clicked)
	 */
	public void discoverNode(Nodes node, int row, int col) {
		nodes[row][col].setVisited(true);
		nodes[row][col].setImage(nodes[row][col].getDiscoveredImage());
		for (int i = 0; i < 8; i++) {
			int newRow = row + rowMatrix[i];
			int newCol = col + colMatrix[i];
			if (newRow < 0 || newRow > NUMROWS - 1 || newCol < 0 || newCol > NUMCOLS - 1) {
				continue;
			}
			if (nodes[newRow][newCol].getVisitedStatus()) {
				continue;
			}
			if (nodes[row][col].getBlankStatus()) {
				discoverNode(nodes[newRow][newCol], newRow, newCol);
			}
		}
	}

	/**
	 * Method that checks if all nodes have been uncovered
	 * 
	 * @return true if all nodes have been visited
	 */
	public boolean checkWin() {
		for (int row = 0; row < NUMROWS; row++) {
			for (int col = 0; col < NUMCOLS; col++) {
				ArrayList<Integer> temp = new ArrayList<>();
				temp.add(row);
				temp.add(col);
				if (!bombLocation.contains(temp) && !nodes[row][col].getVisitedStatus()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Method to set up variables if the game has been won
	 */
	public void gameWon() {
		GameFrame.timer.stop();
		gameWon = true;
		GameFrame.updateBestTimes(2);
		try {
			winStream = AudioSystem.getAudioInputStream(winFile);
			win = AudioSystem.getClip();
			win.open(winStream);
			win.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Method to set variables if the game has been lost
	 */
	public void gameLost() {
		for (int i = 0; i < bombLocation.size(); i++) {
			displayBomb(bombLocation.get(i).get(0), bombLocation.get(i).get(1));
		}
		GameFrame.timer.stop();
		GameFrame.timerReset();
		gameLost = true;
		try {
			loseStream = AudioSystem.getAudioInputStream(loseFile);
			lose = AudioSystem.getClip();
			lose.open(loseStream);
			lose.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Method to reset the flagCounter
	 */
	public void resetFlagCounter() {
		flagCounter = 99;
	}

	/**
	 * Method to reset the board
	 */
	public void resetBoard() {
		for (int row = 0; row < NUMROWS; row++) {
			for (int col = 0; col < NUMCOLS; col++) {
				nodes[row][col].setImage(facingDown);
				nodes[row][col].setIsBomb(false);
				nodes[row][col].setVisited(false);
				nodes[row][col].setIsBlank(true);
				nodes[row][col].setIsFlagged(false);
				firstClick = true;
				bombLocation = new ArrayList<>();
			}
		}
		if (gameLost) {
			lose.close();
			try {
				loseStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (gameWon) {
			win.close();
			try {
				winStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		gameWon = false;
		gameLost = false;
		resetFlagCounter();
		GameFrame.updateFlaggedCounter();
		GameFrame.timer.stop();
		GameFrame.timerReset();

	}

	/**
	 * Helper method to set a node as flagged
	 * 
	 * @param row (row coordinate of the node that has been right-clicked)
	 * @param col (column coordinate of the node that has been right-clicked)
	 */
	public void setFlagged(int row, int col) {
		nodes[row][col].setImage(flagged);
		nodes[row][col].setIsFlagged(true);
	}

	/**
	 * Helper method to set a node as facing down when unflagged
	 * 
	 * @param row (row coordinate of the node that has been right-clicked)
	 * @param col (column coordinate of the node that has been right-clicked)
	 */
	public void setFacingDown(int row, int col) {
		nodes[row][col].setImage(facingDown);
		nodes[row][col].setIsFlagged(false);
		nodes[row][col].setVisited(false);
	}

	/**
	 * Helper method to display a bomb
	 * 
	 * @param row (row coordinate of the bomb that has been left-clicked)
	 * @param col (column coordinate of the bomb that has been left-clicked)
	 */
	public void displayBomb(int row, int col) {
		nodes[row][col].setImage(bomb);
		nodes[row][col].setIsFlagged(false);
		nodes[row][col].setVisited(true);
	}

	/**
	 * Method that checks if the mouse location is within the play again "button"
	 * 
	 * @param mouseX (x location of the mouse)
	 * @param mouseY (y location of the mouse)
	 * @return true if the mouse location is within the play again "button"
	 */
	public boolean containsPlayAgain(int mouseX, int mouseY) {
		int nw = 504;
		int nh = 82;
		int mw = 1;
		int mh = 1;
		if (mw <= 0 || mh <= 0 || nw <= 0 || nh <= 0) {
			return false;
		}
		int nx = 104;
		int ny = 367;
		int mx = mouseX;
		int my = mouseY;
		mw += mx;
		mh += my;
		nw += nx;
		nh += ny;
		return ((mw < mx || mw > nx) && (mh < my || mh > ny) && (nw < nx || nw > mx) && (nh < ny || nh > my));
	}

	/**
	 * Method that checks if the mouse location is within the choose difficulty
	 * "button"
	 * 
	 * @param mouseX (x location of the mouse)
	 * @param mouseY (y location of the mouse)
	 * @return true if the mouse location is within the choose difficulty "button"
	 */
	public boolean containsChooseDifficulty(int mouseX, int mouseY) {
		int nw = 242;
		int nh = 82;
		int mw = 1;
		int mh = 1;
		if (mw <= 0 || mh <= 0 || nw <= 0 || nh <= 0) {
			return false;
		}
		int nx = 104;
		int ny = 459;
		int mx = mouseX;
		int my = mouseY;
		mw += mx;
		mh += my;
		nw += nx;
		nh += ny;
		return ((mw < mx || mw > nx) && (mh < my || mh > ny) && (nw < nx || nw > mx) && (nh < ny || nh > my));
	}

	/**
	 * Method that checks if the mouse location is within the view board "button"
	 * 
	 * @param mouseX (x location of the mouse)
	 * @param mouseY (y location of the mouse)
	 * @return true if the mouse location is within the view board "button"
	 */
	public boolean containsViewBoard(int mouseX, int mouseY) {
		int nw = 242;
		int nh = 82;
		int mw = 1;
		int mh = 1;
		if (mw <= 0 || mh <= 0 || nw <= 0 || nh <= 0) {
			return false;
		}
		int nx = 366;
		int ny = 459;
		int mx = mouseX;
		int my = mouseY;
		mw += mx;
		mh += my;
		nw += nx;
		nh += ny;
		return ((mw < mx || mw > nx) && (mh < my || mh > ny) && (nw < nx || nw > mx) && (nh < ny || nh > my));
	}

	/**
	 * Method that checks if the mouse location is within the stop viewing "button"
	 * 
	 * @param mouseX (x location of the mouse)
	 * @param mouseY (y location of the mouse)
	 * @return true if the mouse location is within the stop viewing "button"
	 */
	public boolean containsStopViewing(int mouseX, int mouseY) {
		int nw = 150;
		int nh = 50;
		int mw = 1;
		int mh = 1;
		if (mw <= 0 || mh <= 0 || nw <= 0 || nh <= 0) {
			return false;
		}
		int nx = 285;
		int ny = 566;
		int mx = mouseX;
		int my = mouseY;
		mw += mx;
		mh += my;
		nw += nx;
		nh += ny;
		return ((mw < mx || mw > nx) && (mh < my || mh > ny) && (nw < nx || nw > mx) && (nh < ny || nh > my));
	}

	/**
	 * Method that continuously runs
	 */
	@Override
	public void run() {
		// Retrieved from https://www.youtube.com/watch?v=oLirZqJFKPE at 28:06
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				delta--;
				repaint();
			}
		}
	}

	/**
	 * Method that paints the panel
	 */
	public void paintComponent(Graphics g) {
		for (int row = 0; row < NUMROWS; row++) {
			for (int col = 0; col < NUMCOLS; col++) {
				g.drawImage(nodes[row][col].getImage(), nodes[row][col].getX(), nodes[row][col].getY(),
						nodes[row][col].getW(), nodes[row][col].getH(), null);
			}
		}

		if ((gameWon || gameLost) && viewingBoard) {
			g.setColor(Color.decode("#282f56"));
			g.drawRoundRect(285, 566, 150, 50, 30, 30);
			g.fillRoundRect(285, 566, 150, 50, 30, 30);

			g.setFont(new Font("Comic Sans", Font.BOLD, 16));
			g.setColor(Color.decode("#ffffff"));
			g.drawString("STOP VIEWING", 300, 595);
		} else if (gameWon) {
			g.setColor(Color.decode("#282f56")); // 536e3e
			g.drawRoundRect(104, 32 - 15, 504, 340, 30, 30);
			g.fillRoundRect(104, 32 - 15, 504, 340, 30, 30);

			g.drawRoundRect(104, 382 - 15, 504, 82, 30, 30);
			g.fillRoundRect(104, 382 - 15, 504, 82, 30, 30);

			g.drawRoundRect(104, 459, 242 + 5, 82, 30, 30);
			g.fillRoundRect(104, 459, 242 + 5, 82, 30, 30);

			g.drawRoundRect(366 - 5, 459, 242 + 5, 82, 30, 30);
			g.fillRoundRect(366 - 5, 459, 242 + 5, 82, 30, 30);

			g.setColor(Color.decode("#ffffff"));

			g.drawImage(stopwatch, 104, 32 - 15, 54 * 5, 52 * 5, null);

			g.drawImage(trophy, 104 + 300, 76 - 15, 768 / 6, 882 / 6, null);

			g.setFont(new Font("Comic Sans", Font.BOLD, 35));
			g.drawString(GameFrame.getTime(), 189 - 40 + 20 - 10 + 2, 240);

			g.setFont(new Font("Comic Sans", Font.BOLD, 35));
			g.drawString(GameFrame.getBestTimesString(2), 189 + 200 + 1, 240);

			g.drawImage(newGame, 200 + 22, 402 - 15, 40, 40, null);

			g.drawImage(levels, 182 - 20 - 30, 492 - 15, 40, 50, null);

			g.drawImage(board, 182 - 20 - 30 + 262 - 5, 492 - 15 + 4, 40, 40, null);

			g.setFont(new Font("Comic Sans", Font.BOLD, 35));
			g.drawString("Play Again", 270 + 22, 435 - 15);

			g.setFont(new Font("Comic Sans", Font.BOLD, 25));
			g.drawString("Choose", 210, 492);
			g.drawString("Difficulty", 197, 527);

			g.drawString("View ", 480, 492);
			g.drawString("Board", 473, 527);
		} else if (gameLost) {
			g.setColor(Color.decode("#282f56")); // 536e3e
			g.drawRoundRect(104, 32 - 15, 504, 340, 30, 30);
			g.fillRoundRect(104, 32 - 15, 504, 340, 30, 30);

			g.drawRoundRect(104, 382 - 15, 504, 82, 30, 30);
			g.fillRoundRect(104, 382 - 15, 504, 82, 30, 30);

			g.drawRoundRect(104, 459, 242 + 5, 82, 30, 30);
			g.fillRoundRect(104, 459, 242 + 5, 82, 30, 30);

			g.drawRoundRect(366 - 5, 459, 242 + 5, 82, 30, 30);
			g.fillRoundRect(366 - 5, 459, 242 + 5, 82, 30, 30);

			g.setColor(Color.decode("#ffffff"));

			g.drawImage(stopwatch, 104, 32 - 15, 54 * 5, 52 * 5, null);

			g.drawImage(trophy, 104 + 300, 76 - 15, 768 / 6, 882 / 6, null);

			g.fillRect(189, 240, 100, 3);

			if (GameFrame.getBestTimesString(2).equals(" ")) {
				g.fillRect(189 + 225 + 3, 240, 100, 3);
			} else {
				g.setFont(new Font("Comic Sans", Font.BOLD, 35));
				g.drawString(GameFrame.getBestTimesString(2), 189 + 200 + 1, 240);
			}

			g.drawImage(newGame, 200 + 22, 402 - 15, 40, 40, null);

			g.drawImage(levels, 182 - 20 - 30, 492 - 15, 40, 50, null);

			g.drawImage(board, 182 - 20 - 30 + 262 - 5, 492 - 15 + 4, 40, 40, null);

			g.setFont(new Font("Comic Sans", Font.BOLD, 35));
			g.drawString("Try Again", 270 + 30, 435 - 15);

			g.setFont(new Font("Comic Sans", Font.BOLD, 25));
			g.drawString("Choose", 210, 492);
			g.drawString("Difficulty", 197, 527);

			g.drawString("View ", 480, 492);
			g.drawString("Board", 473, 527);
		}
	}

	/**
	 * Class to monitor mouse button activity
	 */
	private class ClickListener extends MouseAdapter {
		/**
		 * Method to check if the mouse has been pressed
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			if ((gameWon || gameLost) && viewingBoard && containsStopViewing(e.getX(), e.getY())) {
				viewingBoard = false;
			} else if ((gameWon || gameLost) && !viewingBoard && containsPlayAgain(e.getX(), e.getY())) {
				resetBoard();
			} else if ((gameWon || gameLost) && !viewingBoard && containsChooseDifficulty(e.getX(), e.getY())) {
				resetBoard();
				GameFrame.displayDifficultyMenu();
			} else if ((gameWon || gameLost) && !viewingBoard && containsViewBoard(e.getX(), e.getY())) {
				viewingBoard = true;
			} else if (!gameWon && !gameLost) {
				if (SwingUtilities.isLeftMouseButton(e) && firstClick) {
					for (int row = 0; row < NUMROWS; row++) {
						for (int col = 0; col < NUMCOLS; col++) {
							if (nodes[row][col].contains(e.getX(), e.getY())) {
								setBombs(row, col);
								setDiscoveredImage();
								firstClick = false;
								GameFrame.timer.restart();
								try {
									initialDiscoverStream = AudioSystem.getAudioInputStream(initialDiscoverFile);
									initialDiscover = AudioSystem.getClip();
									initialDiscover.open(initialDiscoverStream);
									initialDiscover.start();
								} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							if (nodes[row][col].contains(e.getX(), e.getY()) && !nodes[row][col].getVisitedStatus()
									&& nodes[row][col].getBombStatus() && !nodes[row][col].getFlaggedStatus()) {
								displayBomb(row, col);
							} else if (nodes[row][col].contains(e.getX(), e.getY())
									&& !nodes[row][col].getVisitedStatus() && !nodes[row][col].getFlaggedStatus()) {
								discoverNode(nodes[row][col], row, col);
								if (checkWin()) {
									gameWon();
								}
							}
						}
					}
				}
				if (SwingUtilities.isLeftMouseButton(e)) {
					for (int row = 0; row < NUMROWS; row++) {
						for (int col = 0; col < NUMCOLS; col++) {
							if (nodes[row][col].contains(e.getX(), e.getY()) && !nodes[row][col].getVisitedStatus()
									&& nodes[row][col].getBombStatus() && !nodes[row][col].getFlaggedStatus()) {
								displayBomb(row, col);
								gameLost();
							} else if (nodes[row][col].contains(e.getX(), e.getY())
									&& !nodes[row][col].getVisitedStatus() && !nodes[row][col].getFlaggedStatus()) {
								discoverNode(nodes[row][col], row, col);
								try {
									discoverStream = AudioSystem.getAudioInputStream(discoverFile);
									discover = AudioSystem.getClip();
									discover.open(discoverStream);
									discover.start();
								} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								if (checkWin()) {
									gameWon();
								}
							}
						}
					}
				} else if (SwingUtilities.isRightMouseButton(e)) {
					for (int row = 0; row < NUMROWS; row++) {
						for (int col = 0; col < NUMCOLS; col++) {
							if (nodes[row][col].contains(e.getX(), e.getY()) && !nodes[row][col].getVisitedStatus()
									&& !nodes[row][col].getFlaggedStatus() && flagCounter > 0) {
								setFlagged(row, col);
								flagCounter--;
								GameFrame.updateFlaggedCounter();
								try {
									flagStream = AudioSystem.getAudioInputStream(flagFile);
									flag = AudioSystem.getClip();
									flag.open(flagStream);
									flag.start();
								} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							} else if (nodes[row][col].contains(e.getX(), e.getY())
									&& nodes[row][col].getFlaggedStatus()) {
								setFacingDown(row, col);
								flagCounter++;
								GameFrame.updateFlaggedCounter();
								try {
									unflagStream = AudioSystem.getAudioInputStream(unflagFile);
									unflag = AudioSystem.getClip();
									unflag.open(unflagStream);
									unflag.start();
								} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
					}
				}

			}

		}

		/**
		 * Method to check if the mouse has exited the panel
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			for (int row = 0; row < NUMROWS; row++) {
				for (int col = 0; col < NUMCOLS; col++) {
					if (!nodes[row][col].getVisitedStatus() && !nodes[row][col].getFlaggedStatus()) {
						nodes[row][col].setImage(facingDown);
					}
				}
			}
		}

	}

	/**
	 * Class to monitor mouse motion activity
	 */
	private class MoveListener extends MouseMotionAdapter {
		/**
		 * Method to check if the mouse have moved
		 */
		@Override
		public void mouseMoved(MouseEvent e) {
			if (!gameWon && !gameLost) {
				for (int row = 0; row < NUMROWS; row++) {
					for (int col = 0; col < NUMCOLS; col++) {
						if (nodes[row][col].contains(e.getX(), e.getY()) && !nodes[row][col].getVisitedStatus()
								&& !nodes[row][col].getFlaggedStatus()) {
							nodes[row][col].setImage(facingDownHover);
						} else if (!nodes[row][col].getVisitedStatus() && !nodes[row][col].getFlaggedStatus()) {
							nodes[row][col].setImage(facingDown);
						}

					}
				}
			}
		}
	}

}
