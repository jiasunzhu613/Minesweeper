import javax.imageio.ImageIO; 
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * GameFrame for MINEWSWEEPER
 * 
 * @date June 2022
 * @author Jonathan Zhu
 */
public class GameFrame extends JFrame implements ActionListener {
	// Initiate variables
	private static int userPlaying;
	private static long[] bestTimes;
	private static String[] bestTimesString;
	private static GamePanel[] gamePanels;
	private static int panelSelected;
	private static GamePanelEasy gamePanelEasy;
	private static GamePanelMedium gamePanelMedium;
	private static GamePanelHard gamePanelHard;
	private BufferedImage flagged, stopwatch;
	private static JTextArea flaggedCounter, selectMode;
	private static JLabel flaggedImage, stopwatchImage;
	private static JLabel stopwatchCounter;
	private static JButton easy, medium, hard, exit;
	private static int elapsedTime, seconds, minutes, hours;
	public static boolean started;
	private static String secondsString, minutesString, hoursString;
	public static Timer timer;
	private PrintWriter fileWriter;
	private BufferedReader fileReader;

	public GameFrame() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		// Sets title at the top of the created JFrame
		super("MINESWEEPER");

		// Sets JFrame size
		setSize(900, 800);

		/**
		 * Setting up swing components and variables
		 */
		userPlaying = 0;

		gamePanels = new GamePanel[3];

		gamePanelEasy = new GamePanelEasy();
		gamePanelEasy.setVisible(false);
		gamePanels[0] = gamePanelEasy;

		gamePanelMedium = new GamePanelMedium();
		gamePanelMedium.setVisible(false);
		gamePanels[1] = gamePanelMedium;

		gamePanelHard = new GamePanelHard();
		gamePanelHard.setVisible(false);
		gamePanels[2] = gamePanelHard;

		bestTimes = new long[3];

		bestTimesString = new String[] { " ", " ", " " };

		panelSelected = 0;

		elapsedTime = 0;

		seconds = 0;

		minutes = 0;

		hours = 0;

		started = false;

		secondsString = String.format("%02d", seconds);

		minutesString = String.format("%02d", minutes);

		hoursString = String.format("%02d", hours);

		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				elapsedTime = elapsedTime + 1000;
				hours = (elapsedTime / 3600000);
				minutes = (elapsedTime / 60000) % 60;
				seconds = (elapsedTime / 1000) % 60;
				secondsString = String.format("%02d", seconds);
				minutesString = String.format("%02d", minutes);
				hoursString = String.format("%02d", hours);
				stopwatchCounter.setText(hoursString + ":" + minutesString + ":" + secondsString);
			}

		});

		flagged = ImageIO.read(new File("Images/flagged.png"));
		flaggedImage = new JLabel(new ImageIcon(flagged.getScaledInstance(54, 52, BufferedImage.SCALE_SMOOTH)));
		flaggedImage.setBounds(200 + 50, 50, 54, 52);
		flaggedImage.setVisible(false);

		flaggedCounter = new JTextArea();
		flaggedCounter.setBounds(200 + 54 + 10 + 50, 45, 70, 50);
		flaggedCounter.setBackground(Color.decode("#3d4782"));
		flaggedCounter.setForeground(Color.decode("#ffffff"));
		flaggedCounter.setFont(new Font("Comic Sans", Font.PLAIN, 50));
		flaggedCounter.setVisible(false);
		flaggedCounter.setEditable(false);

		selectMode = new JTextArea("Select A Difficulty To Play!");
		selectMode.setBounds(112, 45, 676, 80);
		selectMode.setBackground(Color.decode("#3d4782"));
		selectMode.setForeground(Color.decode("#ffffff"));
		selectMode.setFont(new Font("Comic Sans", Font.BOLD, 50));
		selectMode.setVisible(true);
		selectMode.setEditable(false);

		stopwatch = ImageIO.read(new File("Images/stopwatch.png"));
		stopwatchImage = new JLabel(
				new ImageIcon(stopwatch.getScaledInstance(54 * 3, 52 * 3, BufferedImage.SCALE_SMOOTH)));
		stopwatchImage.setBounds(200 + 100 + 50, 0, 54 * 3, 52 * 3);
		stopwatchImage.setVisible(false);

		stopwatchCounter = new JLabel();
		stopwatchCounter.setText(hoursString + ":" + minutesString + ":" + secondsString);
		stopwatchCounter.setBounds(200 + 54 + 10 + 50 + 150, 53, 300, 50);
		stopwatchCounter.setBackground(Color.decode("#3d4782"));
		stopwatchCounter.setForeground(Color.decode("#ffffff"));
		stopwatchCounter.setFont(new Font("Comic Sans", Font.PLAIN, 50));
		stopwatchCounter.setVisible(false);

		easy = new JButton("Easy");
		easy.setBounds(350, 200, 200, 60);
		easy.setForeground(Color.decode("#3d4782"));
		easy.setFocusPainted(false);
		easy.setFont(new Font("Comic Sans", Font.PLAIN, 25));
		easy.setVisible(true);
		easy.addActionListener(this);

		medium = new JButton("Medium");
		medium.setBounds(350, 350, 200, 60);
		medium.setForeground(Color.decode("#3d4782"));
		medium.setFocusPainted(false);
		medium.setFont(new Font("Comic Sans", Font.PLAIN, 25));
		medium.setVisible(true);
		medium.addActionListener(this);

		hard = new JButton("Hard");
		hard.setBounds(350, 500, 200, 60);
		hard.setForeground(Color.decode("#3d4782"));
		hard.setFocusPainted(false);
		hard.setFont(new Font("Comic Sans", Font.PLAIN, 25));
		hard.setVisible(true);
		hard.addActionListener(this);

		exit = new JButton("Exit");
		exit.setBounds(10, 705, 200, 60);
		exit.setForeground(Color.decode("#3d4782"));
		exit.setFocusPainted(false);
		exit.setFont(new Font("Comic Sans", Font.PLAIN, 25));
		exit.setVisible(true);
		exit.addActionListener(this);

		// Add all elements to the GameFrame (JFrame)
		Container c = getContentPane();
		c.setLayout(null);
		c.add(gamePanelEasy);
		c.add(gamePanelMedium);
		c.add(gamePanelHard);
		c.add(flaggedCounter);
		c.add(flaggedImage);
		c.add(stopwatchImage);
		c.add(stopwatchCounter);
		c.add(easy);
		c.add(medium);
		c.add(hard);
		c.add(exit);
		c.add(selectMode);
		c.setBackground(Color.decode("#3d4782"));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		WindowListener windowListener = new WindowListener();
		addWindowListener(windowListener);
		setLocationRelativeTo(null);
	}

	/**
	 * Method to reset the timer
	 */
	public static void timerReset() {
		elapsedTime = 0;
		seconds = 0;
		minutes = 0;
		hours = 0;
		secondsString = String.format("%02d", seconds);
		minutesString = String.format("%02d", minutes);
		hoursString = String.format("%02d", hours);
		stopwatchCounter.setText(hoursString + ":" + minutesString + ":" + secondsString);
	}

	/**
	 * Method to update the flagged counter
	 */
	public static void updateFlaggedCounter() {
		flaggedCounter.setText(String.valueOf(gamePanels[panelSelected].getFlagCounter()));
	}

	/**
	 * Helper method to get the time displayed on the stopwatch
	 * 
	 * @return text displayed on the stopwatchCounter
	 */
	public static String getTime() {
		return stopwatchCounter.getText();
	}

	/**
	 * Helper method to the best times for each difficulty
	 * 
	 * @return elements of the bestTimesString array (0 -> easy, 1 -> medium, 2 ->
	 *         hard)
	 */
	public static String getBestTimesString(int difficulty) {
		return bestTimesString[difficulty];
	}

	/**
	 * Method to set the user currently playing
	 * 
	 * @param line the account exists on in logins.txt, 0 if playing as guest
	 */
	public static void setUserPlaying(int n) {
		userPlaying = n;
	}

	/**
	 * Helper method to set the best times for each difficulty
	 * 
	 * @param bestTimes will be the last 3 elements of a line on logins.txt
	 */
	public static void setBestTimesString(String[] bestTimes) {
		bestTimesString = bestTimes;
	}

	/**
	 * Method to update the best times for each difficulty
	 */
	public static void updateBestTimes(int difficulty) {
		if (bestTimes[difficulty] == 0) {
			bestTimes[difficulty] = timeInSeconds();
			bestTimesString[difficulty] = getTime();
		} else if (timeInSeconds() < bestTimes[difficulty]) {
			bestTimes[difficulty] = timeInSeconds();
			bestTimesString[difficulty] = getTime();
		}
	}

	/**
	 * Helper method to get the time displayed on the stopwatch in seconds
	 * 
	 * @return stopwatchCounter time in seconds
	 */
	public static long timeInSeconds() {
		long time = 0L;
		time += seconds;
		time += minutes * 60;
		time += hours * 3600;
		return time;
	}

	/**
	 * Helper method to get the time displayed on the stopwatch in seconds
	 * 
	 * @param integer array with 3 elements (hours, minutes, seconds)
	 * @return time of array t in seconds
	 */
	public static long timeInSeconds(int[] t) {
		long time = 0L;
		time += t[2];
		time += t[1] * 60;
		time += t[0] * 3600;
		return time;
	}

	/**
	 * Method to display the easy difficulty
	 */
	public void displayEasy() {
		easy.setVisible(false);
		medium.setVisible(false);
		hard.setVisible(false);
		selectMode.setVisible(false);
		gamePanelMedium.setVisible(false);
		gamePanelHard.setVisible(false);
		gamePanelEasy.setVisible(true);
		stopwatchCounter.setVisible(true);
		flaggedCounter.setVisible(true);
		flaggedImage.setVisible(true);
		stopwatchImage.setVisible(true);
		exit.setVisible(false);
	}

	/**
	 * Method to display the medium difficulty
	 */
	public void displayMedium() {
		easy.setVisible(false);
		medium.setVisible(false);
		hard.setVisible(false);
		selectMode.setVisible(false);
		gamePanelMedium.setVisible(true);
		gamePanelHard.setVisible(false);
		gamePanelEasy.setVisible(false);
		stopwatchCounter.setVisible(true);
		flaggedCounter.setVisible(true);
		flaggedImage.setVisible(true);
		stopwatchImage.setVisible(true);
		exit.setVisible(false);
	}

	/**
	 * Method to display the hard difficulty
	 */
	public void displayHard() {
		easy.setVisible(false);
		medium.setVisible(false);
		hard.setVisible(false);
		selectMode.setVisible(false);
		gamePanelMedium.setVisible(false);
		gamePanelHard.setVisible(true);
		gamePanelEasy.setVisible(false);
		stopwatchCounter.setVisible(true);
		flaggedCounter.setVisible(true);
		flaggedImage.setVisible(true);
		stopwatchImage.setVisible(true);
		exit.setVisible(false);
	}

	/**
	 * Method to display the menu where you can select the difficulty to play
	 */
	public static void displayDifficultyMenu() {
		easy.setVisible(true);
		medium.setVisible(true);
		hard.setVisible(true);
		selectMode.setVisible(true);
		gamePanelMedium.setVisible(false);
		gamePanelHard.setVisible(false);
		gamePanelEasy.setVisible(false);
		stopwatchCounter.setVisible(false);
		flaggedCounter.setVisible(false);
		flaggedImage.setVisible(false);
		stopwatchImage.setVisible(false);
		exit.setVisible(true);
	}

	/**
	 * Helper method to turn logins.txt into a string ArrayList
	 * 
	 * @return all lines of logins.txt in an ArrayList
	 */
	public ArrayList<String> fileToStringArray() {
		ArrayList<String> arr = new ArrayList<>();
		try {
			fileReader = new BufferedReader(new FileReader(new File("data/logins.txt")));
			String line = fileReader.readLine();
			while (line != null) {
				arr.add(line);
				line = fileReader.readLine();
			}
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arr;
	}

	/**
	 * Method to update the array storing best times for each difficulty in seconds
	 */
	public static void updateBestTimesLong() {
		for (int i = 0; i < 3; i++) {
			if (!bestTimesString[i].equals(" ")) {
				String[] temp = bestTimesString[i].split(":");
				int[] t = new int[3];
				for (int j = 0; j < 3; j++) {
					t[j] = Integer.parseInt(temp[j]);
				}
				bestTimes[i] = timeInSeconds(t);
			}
		}
	}

	/**
	 * Method to update logins.txt
	 */
	public void updateLoginsFile() {
		ArrayList<String> fileStrings = fileToStringArray();
		try {
			fileWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File("data/logins.txt"))));
			fileWriter.write("");
			for (int i = 0; i < fileStrings.size(); i++) {
				if (i == userPlaying) {
					String[] splitString = fileStrings.get(i).split(",");
					String newString = String.format("%s,%s,%s,%s,%s", splitString[0], splitString[1],
							bestTimesString[0], bestTimesString[1], bestTimesString[2]);
					fileWriter.println(newString);
				} else {
					fileWriter.println(fileStrings.get(i));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileWriter.close();
	}

	/**
	 * Method to check if any JButtons have been pressed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == easy) {
			panelSelected = 0;
			displayEasy();
			updateFlaggedCounter();
		}

		if (e.getSource() == medium) {
			panelSelected = 1;
			displayMedium();
			updateFlaggedCounter();
		}

		if (e.getSource() == hard) {
			panelSelected = 2;
			displayHard();
			updateFlaggedCounter();
		}

		if (e.getSource() == exit) {
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}

	}

	/**
	 * Class to monitor window activity
	 */
	private class WindowListener extends WindowAdapter {
		/**
		 * Method to check if a GameFrame window is closing
		 */
		@Override
		public void windowClosing(WindowEvent e) {
			if (userPlaying != 0) {
				updateLoginsFile();
			}
			try {
				new Menu();
			} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
