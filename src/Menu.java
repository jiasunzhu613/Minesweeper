import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.io.FileWriter;
import java.io.FileReader;

/**
 * Beginning menu for MINEWSWEEPER
 * 
 * @date June 2022
 * @author Jonathan Zhu
 */
public class Menu extends JFrame implements ActionListener {
	// Initiate variables
	private JLabel logo, ruleLabel1, ruleLabel2;
	private JTextArea title, invalid, taken, inputUsernameTitle, inputPasswordTitle, inputUsername, inputPassword,
			createUsernameTitle, createUsername, createPasswordTitle, createPassword, howToPlayTitle, rulesText1,
			rulesText2, rulesText3;
	private JButton start, howToPlay, home, login, enterLogin, playAsGuest, createUser, enterCreate, back,
			howToPlayBack;
	private GameFrame gameFrame;
	private PasswordAuthentication authenticator;
	private PrintWriter fileWriter;
	private BufferedReader fileReader;

	public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		// Creates new menu
		new Menu();
	}

	public Menu() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		// Sets title at the top of the created JFrame
		super("MINESWEEPER");

		// Sets JFrame size
		setSize(900, 800);

		/**
		 * Setting up swing components
		 */
		gameFrame = new GameFrame();
		gameFrame.setVisible(false);

		BufferedImage myPicture = ImageIO.read(new File("Images/minesweeperIcon.png"));
		logo = new JLabel(new ImageIcon(myPicture.getScaledInstance(400, 400, BufferedImage.SCALE_SMOOTH)));
		logo.setBounds(250, 110, 400, 400);
		logo.setVisible(true);

		title = new JTextArea("MINESWEEPER");
		title.setBounds(253, 510, 200, 30);
		title.setBackground(Color.decode("#3d4782"));
		title.setForeground(Color.decode("#ffffff"));
		title.setFont(new Font("Comic Sans", Font.BOLD, 20));
		title.setVisible(true);
		title.setEditable(false);

		start = new JButton("Start");
		start.setBounds(400 + 50 + 65, 530 + 40, 200, 60);
		start.setForeground(Color.decode("#3d4782"));
		start.setFocusPainted(false);
		start.setFont(new Font("Comic Sans", Font.PLAIN, 25));
		start.setVisible(false);
		start.addActionListener(this);

		playAsGuest = new JButton("Play As Guest");
		playAsGuest.setBounds(400 + 50 + 65, 530 + 40, 200, 60);
		playAsGuest.setForeground(Color.decode("#3d4782"));
		playAsGuest.setFocusPainted(false);
		playAsGuest.setFont(new Font("Comic Sans", Font.PLAIN, 25));
		playAsGuest.setVisible(true);
		playAsGuest.addActionListener(this);

		home = new JButton("Home");
		home.setBounds(10, 10, 180, 60);
		home.setForeground(Color.decode("#3d4782"));
		home.setFocusPainted(false);
		home.setFont(new Font("Comic Sans", Font.PLAIN, 20));
		home.setVisible(false);
		home.addActionListener(this);

		howToPlay = new JButton("How to Play");
		howToPlay.setBounds(50 + 50 + 75, 530 + 40, 200, 60);
		howToPlay.setForeground(Color.decode("#3d4782"));
		howToPlay.setFocusPainted(false);
		howToPlay.setFont(new Font("Comic Sans", Font.PLAIN, 25));
		howToPlay.setVisible(false);
		howToPlay.addActionListener(this);

		login = new JButton("Log In");
		login.setBounds(50 + 50 + 75, 530 + 40, 200, 60);
		login.setForeground(Color.decode("#3d4782"));
		login.setFocusPainted(false);
		login.setFont(new Font("Comic Sans", Font.PLAIN, 25));
		login.setVisible(true);
		login.addActionListener(this);

		inputUsernameTitle = new JTextArea("Enter Username:");
		inputUsernameTitle.setBounds(200, 110 + 5 + 50 + 20, 200, 25);
		inputUsernameTitle.setForeground(Color.decode("#ffffff"));
		inputUsernameTitle.setBackground(Color.decode("#3d4782"));
		inputUsernameTitle.setFont(new Font("Comic Sans", Font.PLAIN, 20));
		inputUsernameTitle.setEditable(false);
		inputUsernameTitle.setVisible(false);

		inputUsername = new JTextArea("");
		inputUsername.setBounds(200, 140 + 5 + 50 + 20, 500, 100);
		inputUsername.setBackground(Color.decode("#ffffff"));
		inputUsername.setForeground(Color.decode("#3d4782"));
		inputUsername.setCaretColor(Color.decode("#3d4782"));
		inputUsername.setFont(new Font("Comic Sans", Font.PLAIN, 20));
		inputUsername.setEditable(true);
		inputUsername.setVisible(false);

		inputPasswordTitle = new JTextArea("Enter Password:");
		inputPasswordTitle.setBounds(200, 250 + 5 + 50 + 20, 200, 25);
		inputPasswordTitle.setForeground(Color.decode("#ffffff"));
		inputPasswordTitle.setBackground(Color.decode("#3d4782"));
		inputPasswordTitle.setFont(new Font("Comic Sans", Font.PLAIN, 20));
		inputPasswordTitle.setEditable(false);
		inputPasswordTitle.setVisible(false);

		inputPassword = new JTextArea("");
		inputPassword.setBounds(200, 280 + 5 + 50 + 20, 500, 100);
		inputPassword.setBackground(Color.decode("#ffffff"));
		inputPassword.setForeground(Color.decode("#3d4782"));
		inputPassword.setCaretColor(Color.decode("#3d4782"));
		inputPassword.setFont(new Font("Comic Sans", Font.PLAIN, 20));
		inputPassword.setEditable(true);
		inputPassword.setVisible(false);

		createUser = new JButton("Create User");
		createUser.setBounds(710, 10, 180, 60);
		createUser.setForeground(Color.decode("#3d4782"));
		createUser.setFont(new Font("Comic Sans", Font.PLAIN, 20));
		createUser.addActionListener(this);
		createUser.setFocusable(false);
		createUser.setVisible(false);

		back = new JButton("Back");
		back.setBounds(710, 10, 180, 60);
		back.setForeground(Color.decode("#3d4782"));
		back.setFont(new Font("Comic Sans", Font.PLAIN, 20));
		back.addActionListener(this);
		back.setFocusable(false);
		back.setVisible(false);

		howToPlayBack = new JButton("Back");
		howToPlayBack.setBounds(710, 10, 180, 60);
		howToPlayBack.setForeground(Color.decode("#3d4782"));
		howToPlayBack.setFont(new Font("Comic Sans", Font.PLAIN, 20));
		howToPlayBack.addActionListener(this);
		howToPlayBack.setFocusable(false);
		howToPlayBack.setVisible(false);

		enterLogin = new JButton("Enter");
		enterLogin.setBounds(350, 430 + 5 + 50 + 20, 200, 60);
		enterLogin.setForeground(Color.decode("#3d4782"));
		enterLogin.setFont(new Font("Comic Sans", Font.PLAIN, 25));
		enterLogin.addActionListener(this);
		enterLogin.setFocusable(false);
		enterLogin.setVisible(false);

		invalid = new JTextArea("*Invalid*");
		invalid.setBounds(620, 385 + 5 + 50 + 20, 100, 100);
		invalid.setBackground(Color.decode("#3d4782"));
		invalid.setForeground(Color.decode("#e72e38"));
		invalid.setFont(new Font("Comic Sans", Font.PLAIN, 20));
		invalid.setVisible(false);
		invalid.setEditable(false);

		taken = new JTextArea("*Already Taken*");
		taken.setBounds(547, 300 + 17, 200, 100);
		taken.setBackground(Color.decode("#3d4782"));
		taken.setForeground(Color.decode("#e72e38"));
		taken.setFont(new Font("Comic Sans", Font.PLAIN, 20));
		taken.setVisible(false);
		taken.setEditable(false);

		createUsernameTitle = new JTextArea("Create Username:");
		createUsernameTitle.setBounds(200, 110 + 5 + 50 + 20, 200, 25);
		createUsernameTitle.setForeground(Color.decode("#ffffff"));
		createUsernameTitle.setBackground(Color.decode("#3d4782"));
		createUsernameTitle.setFont(new Font("Comic Sans", Font.PLAIN, 20));
		createUsernameTitle.setEditable(false);
		createUsernameTitle.setVisible(false);

		createUsername = new JTextArea("");
		createUsername.setBounds(200, 140 + 5 + 50 + 20, 500, 100);
		createUsername.setBackground(Color.decode("#ffffff"));
		createUsername.setForeground(Color.decode("#3d4782"));
		createUsername.setCaretColor(Color.decode("#3d4782"));
		createUsername.setFont(new Font("Comic Sans", Font.PLAIN, 20));
		createUsername.setEditable(true);
		createUsername.setVisible(false);

		createPasswordTitle = new JTextArea("Create Password:");
		createPasswordTitle.setBounds(200, 250 + 5 + 50 + 20, 200, 25);
		createPasswordTitle.setForeground(Color.decode("#ffffff"));
		createPasswordTitle.setBackground(Color.decode("#3d4782"));
		createPasswordTitle.setFont(new Font("Comic Sans", Font.PLAIN, 20));
		createPasswordTitle.setEditable(false);
		createPasswordTitle.setVisible(false);

		createPassword = new JTextArea("");
		createPassword.setBounds(200, 280 + 5 + 50 + 20, 500, 100);
		createPassword.setBackground(Color.decode("#ffffff"));
		createPassword.setForeground(Color.decode("#3d4782"));
		createPassword.setCaretColor(Color.decode("#3d4782"));
		createPassword.setFont(new Font("Comic Sans", Font.PLAIN, 20));
		createPassword.setEditable(true);
		createPassword.setVisible(false);

		enterCreate = new JButton("Create");
		enterCreate.setBounds(350, 430 + 5 + 50 + 20, 200, 60);
		enterCreate.setForeground(Color.decode("#3d4782"));
		enterCreate.setFont(new Font("Comic Sans", Font.PLAIN, 25));
		enterCreate.addActionListener(this);
		enterCreate.setFocusable(false);
		enterCreate.setVisible(false);

		BufferedImage rules1 = ImageIO.read(new File("Images/minesweeperRules1.png"));
		ruleLabel1 = new JLabel(new ImageIcon(rules1.getScaledInstance(288, 224, BufferedImage.SCALE_SMOOTH))); // 1440
																												// ×
																												// 1120
		ruleLabel1.setBounds(108 + 100 + 100, 170 + 50 + 30 + 10, 288, 224);
		ruleLabel1.setVisible(false);

		BufferedImage rules2 = ImageIO.read(new File("Images/minesweeperRules2.png"));
		ruleLabel2 = new JLabel(new ImageIcon(rules2.getScaledInstance(288, 224, BufferedImage.SCALE_SMOOTH))); // 1440
																												// ×
																												// 1120
		ruleLabel2.setBounds(108 + 100 + 100, 120 + 50 + 300 + 20 + 20 + 10, 288, 224);
		ruleLabel2.setVisible(false);

		howToPlayTitle = new JTextArea("How To Play");
		howToPlayTitle.setBounds(350 + 20, 50, 200, 30);
		howToPlayTitle.setBackground(Color.decode("#3d4782"));
		howToPlayTitle.setForeground(Color.decode("#ffffff"));
		howToPlayTitle.setFont(new Font("Comic Sans", Font.BOLD, 25));
		howToPlayTitle.setVisible(false);
		howToPlayTitle.setEditable(false);

		rulesText1 = new JTextArea("Uncover the map WITHOUT clicking on a bomb.");
		rulesText1.setBounds(50, 120, 750, 30);
		rulesText1.setBackground(Color.decode("#3d4782"));
		rulesText1.setForeground(Color.decode("#ffffff"));
		rulesText1.setFont(new Font("Comic Sans", Font.BOLD, 15));
		rulesText1.setVisible(false);
		rulesText1.setEditable(false);

		rulesText2 = new JTextArea(
				"To start the game, left-click on any space on the map. Uncovering spaces will provide \n"
						+ "information about the map. A blank square means that there are no bombs in the squares to \n"
						+ "the left, right, top, bottom and the diagonals (adjacent) of it. A numbered square means there \n"
						+ "are that number of bombs adjacent to it.");
		rulesText2.setBounds(50, 120 + 50, 750, 80);
		rulesText2.setBackground(Color.decode("#3d4782"));
		rulesText2.setForeground(Color.decode("#ffffff"));
		rulesText2.setFont(new Font("Comic Sans", Font.BOLD, 15));
		rulesText2.setVisible(false);
		rulesText2.setEditable(false);

		rulesText3 = new JTextArea("You can also right-click to mark squares that are potential bombs.");
		rulesText3.setBounds(50, 120 + 50 + 300 + 20, 750, 30);
		rulesText3.setBackground(Color.decode("#3d4782"));
		rulesText3.setForeground(Color.decode("#ffffff"));
		rulesText3.setFont(new Font("Comic Sans", Font.BOLD, 15));
		rulesText3.setVisible(false);
		rulesText3.setEditable(false);

		// Create instance of PassswordAuthentication
		authenticator = new PasswordAuthentication();

		// Add all elements to the Menu (JFrame)
		Container c = getContentPane();
		c.setLayout(null);
		c.setBackground(Color.decode("#3d4782"));
		c.add(logo);
		c.add(title);
		c.add(start);
		c.add(howToPlay);
		c.add(home);
		c.add(ruleLabel1);
		c.add(ruleLabel2);
		c.add(playAsGuest);
		c.add(login);
		c.add(createPassword);
		c.add(createPasswordTitle);
		c.add(createUser);
		c.add(createUsername);
		c.add(createUsernameTitle);
		c.add(enterCreate);
		c.add(enterLogin);
		c.add(inputPassword);
		c.add(inputPasswordTitle);
		c.add(inputUsername);
		c.add(inputUsernameTitle);
		c.add(invalid);
		c.add(back);
		c.add(howToPlayTitle);
		c.add(rulesText1);
		c.add(rulesText2);
		c.add(rulesText3);
		c.add(howToPlayBack);
		c.add(taken);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	/**
	 * Helper method to display the game
	 */
	public void displayGame() {
		this.setVisible(false);
		gameFrame.setVisible(true);
	}

	/**
	 * Helper method to display the start menu
	 */
	public void displayStartMenu() {
		home.setVisible(false);
		howToPlayTitle.setVisible(false);
		ruleLabel1.setVisible(false);
		ruleLabel2.setVisible(false);
		rulesText1.setVisible(false);
		rulesText2.setVisible(false);
		rulesText3.setVisible(false);
		start.setVisible(true);
		title.setVisible(true);
		logo.setVisible(true);
		login.setVisible(false);
		playAsGuest.setVisible(false);
		howToPlay.setVisible(true);
		enterLogin.setVisible(false);
		inputPassword.setVisible(false);
		inputPasswordTitle.setVisible(false);
		inputUsername.setVisible(false);
		inputUsernameTitle.setVisible(false);
		createUser.setVisible(false);
		enterCreate.setVisible(false);
		createPassword.setVisible(false);
		createPasswordTitle.setVisible(false);
		createUsername.setVisible(false);
		createUsernameTitle.setVisible(false);
		back.setVisible(false);
		howToPlayBack.setVisible(false);
		taken.setVisible(false);
		invalid.setVisible(false);
	}

	/**
	 * Helper method to display the home screens
	 */
	public void displayHome() {
		home.setVisible(false);
		howToPlayTitle.setVisible(false);
		ruleLabel1.setVisible(false);
		ruleLabel2.setVisible(false);
		rulesText1.setVisible(false);
		rulesText2.setVisible(false);
		rulesText3.setVisible(false);
		start.setVisible(false);
		title.setVisible(false);
		logo.setVisible(true);
		login.setVisible(true);
		playAsGuest.setVisible(true);
		howToPlay.setVisible(false);
		enterLogin.setVisible(false);
		inputPassword.setVisible(false);
		inputPasswordTitle.setVisible(false);
		inputUsername.setVisible(false);
		inputUsernameTitle.setVisible(false);
		createUser.setVisible(false);
		enterCreate.setVisible(false);
		createPassword.setVisible(false);
		createPasswordTitle.setVisible(false);
		createUsername.setVisible(false);
		createUsernameTitle.setVisible(false);
		back.setVisible(false);
		taken.setVisible(false);
		invalid.setVisible(false);

		createPassword.setText("");
		createUsername.setText("");
		inputPassword.setText("");
		inputUsername.setText("");
	}

	/**
	 * Helper method to display the rules screen
	 */
	public void displayRules() {
		home.setVisible(false);
		howToPlayBack.setVisible(true);
		howToPlayTitle.setVisible(true);
		ruleLabel1.setVisible(true);
		ruleLabel2.setVisible(true);
		rulesText1.setVisible(true);
		rulesText2.setVisible(true);
		rulesText3.setVisible(true);
		start.setVisible(false);
		title.setVisible(false);
		logo.setVisible(false);
		howToPlay.setVisible(false);
		taken.setVisible(false);
		invalid.setVisible(false);
	}

	/**
	 * Helper method to display the login sscreen
	 */
	public void displayLogin() {
		home.setVisible(true);
		howToPlayTitle.setVisible(false);
		ruleLabel1.setVisible(false);
		ruleLabel2.setVisible(false);
		rulesText1.setVisible(false);
		rulesText2.setVisible(false);
		rulesText3.setVisible(false);
		start.setVisible(false);
		title.setVisible(false);
		logo.setVisible(false);
		login.setVisible(false);
		playAsGuest.setVisible(false);
		howToPlay.setVisible(false);
		enterLogin.setVisible(true);
		inputPassword.setVisible(true);
		inputPasswordTitle.setVisible(true);
		inputUsername.setVisible(true);
		inputUsernameTitle.setVisible(true);
		createUser.setVisible(true);
		enterCreate.setVisible(false);
		createPassword.setVisible(false);
		createPasswordTitle.setVisible(false);
		createUsername.setVisible(false);
		createUsernameTitle.setVisible(false);
		back.setVisible(false);
		taken.setVisible(false);
		invalid.setVisible(false);

		createPassword.setText("");
		createUsername.setText("");
		inputPassword.setText("");
		inputUsername.setText("");
	}

	/**
	 * Helper method to display the create user screen
	 */
	public void displayCreateUser() {
		home.setVisible(false);
		howToPlayTitle.setVisible(false);
		ruleLabel1.setVisible(false);
		ruleLabel2.setVisible(false);
		rulesText1.setVisible(false);
		rulesText2.setVisible(false);
		rulesText3.setVisible(false);
		start.setVisible(false);
		title.setVisible(false);
		logo.setVisible(false);
		login.setVisible(false);
		playAsGuest.setVisible(false);
		howToPlay.setVisible(false);
		enterLogin.setVisible(false);
		inputPassword.setVisible(false);
		inputPasswordTitle.setVisible(false);
		inputUsername.setVisible(false);
		inputUsernameTitle.setVisible(false);
		createUser.setVisible(false);
		enterCreate.setVisible(true);
		createPassword.setVisible(true);
		createPasswordTitle.setVisible(true);
		createUsername.setVisible(true);
		createUsernameTitle.setVisible(true);
		back.setVisible(true);
		taken.setVisible(false);
		invalid.setVisible(false);

		createPassword.setText("");
		createUsername.setText("");
		inputPassword.setText("");
		inputUsername.setText("");
	}

	/**
	 * Checks if a input username and password exists in logins.txt
	 * 
	 * @return true if input password matches with stored password
	 */
	public boolean checkUser(String username, String password) {
		char[] charArrayPassword = password.toCharArray();
		try {
			fileReader = new BufferedReader(new FileReader(new File("data/logins.txt")));
			String line = fileReader.readLine();
			while (line != null) {
				String[] words = line.split(",");
				if (words[0].compareTo(username) == 0 && authenticator.authenticate(charArrayPassword, words[1])) {
					return true;
				}
				line = fileReader.readLine();
			}
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Checks if a username is taken
	 * 
	 * @return true if the input username is found in logins.txt
	 */
	public boolean checkUsername(String username) {
		try {
			fileReader = new BufferedReader(new FileReader(new File("data/logins.txt")));
			String line = fileReader.readLine();
			while (line != null) {
				String[] words = line.split(",");
				if (words[0].compareTo(username) == 0) {
					return true;
				}
				line = fileReader.readLine();
			}
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Helper method to set the value of the userPlaying variable in GameFrame if
	 * someone is playing as a guest
	 */
	public void setUserIndex() {
		GameFrame.setUserPlaying(0);
	}

	/**
	 * Helper method to set the value of the userPlaying variable in GameFrame if
	 * someone is playing with an account
	 */
	public void setUserIndex(String username) {
		try {
			fileReader = new BufferedReader(new FileReader(new File("data/logins.txt")));
			String line = fileReader.readLine();
			int index = 0;
			while (line != null) {
				String[] words = line.split(",");
				if (words[0].compareTo(username) == 0) {
					GameFrame.setUserPlaying(index);
					GameFrame.setBestTimesString(Arrays.copyOfRange(words, 2, 5));
					GameFrame.updateBestTimesLong();
				}
				line = fileReader.readLine();
				index++;
			}
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Helper method to create a new user
	 */
	public void createUser(String username, String password) {
		try {
			fileWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File("data/logins.txt"), true)));
			fileWriter.println(String.format("%s,%s,%s,%s,%s", username, authenticator.hash(password.toCharArray()),
					" ", " ", " "));
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method to check if any JButtons have been pressed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == start) {
			displayGame();
		}

		if (e.getSource() == howToPlay) {
			displayRules();
		}

		if (e.getSource() == home) {
			displayHome();
		}

		if (e.getSource() == createUser) {
			displayCreateUser();
		}

		if (e.getSource() == back) {
			displayLogin();
		}

		if (e.getSource() == login) {
			displayLogin();
		}

		if (e.getSource() == playAsGuest) {
			setUserIndex();
			displayStartMenu();
		}

		if (e.getSource() == howToPlayBack) {
			displayStartMenu();
		}

		if (e.getSource() == enterLogin) {
			if (checkUser(inputUsername.getText(), inputPassword.getText())) {
				displayStartMenu();
				setUserIndex(inputUsername.getText());
				invalid.setVisible(false);
			} else {
				invalid.setVisible(true);
			}
		}

		if (e.getSource() == enterCreate) {
			if (!checkUsername(createUsername.getText())) {
				createUser(createUsername.getText(), createPassword.getText());
				taken.setVisible(false);
				displayHome();
				JOptionPane.showMessageDialog(null, "User Successfully Created!");
			} else {
				taken.setVisible(true);
			}
		}
	}

}
