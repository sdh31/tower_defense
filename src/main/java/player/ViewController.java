package main.java.player;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import main.java.player.dlc.RepositoryViewer;
import main.java.player.panels.FileChooserActionListener;
import main.java.player.panels.GameInfoPanel;
import main.java.player.panels.HelpTextPanel;
import main.java.player.panels.HighScoreCard;
import main.java.player.panels.ObjectChooser;
import main.java.player.panels.ObservingPanel;
import main.java.player.panels.UnitInfoPanel;
import main.java.player.panels.WelcomeButtonPanelListener;
import main.java.player.util.Observing;
import main.java.player.util.Sound;
import main.java.player.util.Subject;
import main.java.reflection.MethodAction;

/**
 * The Swing wrapper that contains all the buttons,
 * and the TDPlayerEngine
 * @author Kevin
 *
 */

@SuppressWarnings("serial")
public class ViewController implements Serializable {

	public static final String LANGUAGES_LIST = "LanguageList";
	public static final String DEFAULT_RESOURCE_PACKAGE = "main.resources.";
	public static final String ENGLISH = "English";
	public static final String LANGUAGES = "Languages";
	public static final int BUTTON_PADDING = 10;
	public static final String USER_DIR = "user.dir";
	public static final String DEFAULT_MUSIC_PATH = "src/main/resources/backgroundmusic.wav";

	public static final String WELCOME_CARD = "welcomeCard";
	public static final String GAME_CARD = "gameCard";
	public static final String OPTION_CARD = "optionCard";
	public static final String HELP_CARD = "helpCard";	
	public static final String CREDITS_CARD = "creditsCard";
	public static final String HIGH_SCORE_CARD = "highScoreCard";

	//public static final String DIFFICULTY = "Difficulty";
	//	public static final String EASY = "Easy Mode";
	//	public static final String MEDIUM = "Medium Mode";
	//	public static final String HARD = "Hard Mode";
	/*public static final String SOUND = "Sound";
	public static final String ON = "On";
	public static final String OFF = "Off";
	public static final String WELCOME_LABEL_TEXT = "Ooga Loompas Tower Defense";
	public static final String LOAD_GAME_TEXT = "Load Game Data";
	public static final String LOAD_LIBRARY_TEXT = "Browse library";
	public static final String FILE_LABEL = "File";
	public static final String PLAY_PAUSE_TEXT = "Play/Pause";
	public static final String SAVE_TEXT = "Save game state";
	public static final String LOAD_TEXT = "Load game state";
	public static final String SPEED_UP_TEXT = "Speed up";
	public static final String SLOW_DOWN_TEXT = "Slow down";
	public static final String ADD_TOWER_TEXT = "Add Tower";
	public static final String SOUND_ONOFF_TEXT = "Sound On/Off";
	public static final String MUSIC_TEXT = "Music";
	public static final String MAIN_MENU_TEXT = "Main Menu";
	public static final String QUIT_TEXT = "Quit";

	public static final String HELP = "Click on Play/Pause to begin game. Click to add towers. \n"
			+ "Adding towers uses up money. Right click on towers to sell. \n"
			+ "A proportion of the tower's original cost will be added to money\n"
			+ "N-click: Annihilator\n"
			+ "I-click: InstantFreeze\n"
			+ "L-click: LifeSaver\n"
			+ "shift-click: Upgrade towers\n"
			+ "R-click: Row-bomb";
	public static final String CREDITS = "Game Authoring Environment\nGary Sheng, Cody Lieu, Stephen Hughes, Dennis Park"
			+ "\n\nGame Data\nIn-Young Jo, Jimmy Fang\n\nGame Engine\n"
			+ "Dianwen Li, Austin Lu, Lawrence Lin, Jordan Ly\n\nGame Player\nMichael Han, Kevin Do";
*/

	private JFrame frame;
	private JPanel cards;
	private CardLayout cardLayout;
	private static final JFileChooser fileChooser = new JFileChooser(System.getProperties().getProperty(USER_DIR));
	private ResourceBundle myResources = ResourceBundle.getBundle("main.resources.GUI");
	private ResourceBundle myLanguageResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + ENGLISH);
	private ResourceBundle myLanguagesList = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + LANGUAGES_LIST);
	private ITDPlayerEngine engine;
	private Sound song;
	private boolean soundOn;
	private ObjectChooser towerChooser;
	private JFrame languageFrame;
	//private ObjectChooser powerUpChooser;

	/**
	 * initializeEngine() must be called first
	 * Many other modules require the engine reference to exist
	 */
	public ViewController(){
		initializeEngine(showBlueprintPrompt());
		showLanguagePrompt();
		initSong();
		/*makeFrame();
		makeCards();
		addWelcomeCard();
		addGameCard();
		addHelpCard();
		addOptionsCard();
		addCreditsCard();
		addHighScoreCard();
		show();*/
	}

	private void makeAndAddCards(){
		makeCards();
		addWelcomeCard();
		addGameCard();
		addHelpCard();
		addOptionsCard();
		addCreditsCard();
		addHighScoreCard();
	}
	
	private String showBlueprintPrompt() {
		int response = fileChooser.showOpenDialog(null);
		if(response == JFileChooser.APPROVE_OPTION){
			File file = fileChooser.getSelectedFile();
			return file.getAbsolutePath();
		}
		else {
			System.exit(0);
			return "";
		}
	}	

	private void showLanguagePrompt(){
		languageFrame = new JFrame();
		languageFrame.setLocationRelativeTo(null);
		DefaultComboBoxModel<String> listOfLanguages = new DefaultComboBoxModel<String>(new Vector<String>());
		final JComboBox<String> languageComboBox = new JComboBox<String>(listOfLanguages);
		for(String s: myLanguagesList.keySet()){
			listOfLanguages.addElement(s);
		}
		languageComboBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				myLanguageResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + (String)languageComboBox.getSelectedItem());
				makeFrame();
				makeAndAddCards();
				show();
				languageFrame.dispose();
		
			}			
		});
		languageFrame.add(languageComboBox);
		languageFrame.pack();
		languageFrame.setLocationRelativeTo(null);
		languageFrame.setVisible(true);
		
	}

	private void initSong(){
		try {
			song = new Sound(DEFAULT_MUSIC_PATH);
		} catch (LineUnavailableException | IOException
				| UnsupportedAudioFileException e) {
			//tell user song not found
		}
		soundOn = false;
	}

	public void showCard(String cardName){
		cardLayout.show(cards,  cardName);
	}

	private void makeFrame() {
		frame = new JFrame();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(makeMenuBar());
	}

	private JMenu makeFileMenu(){
		JMenu files = new JMenu(myLanguageResources.getString("FILE_LABEL"));
		files.add(new FileChooserActionListener(engine, "loadBlueprintFile", fileChooser, myLanguageResources.getString("LOAD_GAME_TEXT")));
		files.add(new RepositoryViewer(myLanguageResources.getString("LOAD_LIBRARY_TEXT"), engine));
		return files;
	}

	private JMenuBar makeMenuBar(){
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(makeFileMenu());
		//menuBar.add(makeLanguagesMenu());
		return menuBar;
	}

	private void makeCards() {
		cards = new JPanel(cardLayout = new CardLayout());
	}

	private void addWelcomeCard() {
		JPanel welcomeCard = new JPanel();
		welcomeCard.setLayout(new BoxLayout(welcomeCard, BoxLayout.Y_AXIS));
		welcomeCard.add(makeWelcomeLabel());
		welcomeCard.add(makeWelcomeButtonPanel());
		cards.add(welcomeCard, WELCOME_CARD);
	}

	private JLabel makeWelcomeLabel() {
		JLabel welcomeLabel = new JLabel(myLanguageResources.getString("WELCOME_LABEL_TEXT"));
		welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomeLabel.setFont(new Font("SansSerif", Font.PLAIN, 32));
		return welcomeLabel;
	}

	private JPanel makeWelcomeButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));	

		WelcomeButtonPanelListener listener = new WelcomeButtonPanelListener(myResources, this, myLanguageResources);
		Set<String> keys = myResources.keySet();
		for(String s: keys){
			JButton temp = new JButton(myLanguageResources.getString(s));
			temp.setAlignmentX(Component.CENTER_ALIGNMENT);
			temp.addActionListener(listener);
			buttonPanel.add(temp);
			buttonPanel.add(Box.createRigidArea(new Dimension(0, BUTTON_PADDING)));
		}

		JButton exitButton = makeQuitButton();
		buttonPanel.add(exitButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, BUTTON_PADDING)));
		return buttonPanel;
	}

	private void addGameCard() {
		JPanel gameCard = new JPanel();
		gameCard.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		gameCard.add((Component) engine, constraints);

		//constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 1;
		constraints.gridy = 0;
		gameCard.add(makeGameActionPanel(), constraints);
		//constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 1;
		gameCard.add(makeGameInfoPanel(), constraints);

		//constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 1;
		constraints.gridy = 1;
		gameCard.add(makeUnitInfoPanel(), constraints);


		cards.add(gameCard, GAME_CARD);
	}

	private ITDPlayerEngine initializeEngine(String pathToBlueprint) {
		engine = new TDPlayerEngine(pathToBlueprint);
		engine.initModel();
		engine.stop();
		engine.toggleRunning();
		return engine;
	}

	private JPanel makeGameActionPanel() {
		JPanel gameButtonPanel = new JPanel();
		gameButtonPanel.setLayout(new GridLayout(10, 1));

		JButton mainMenuButton = makeMainMenuButton();

		JButton playResumeButton = new JButton(myLanguageResources.getString("PLAY_PAUSE_TEXT"));
		playResumeButton.addActionListener(new MethodAction (engine, "toggleRunning"));

		JButton saveButton = new JButton(myLanguageResources.getString("SAVE_TEXT"));
		saveButton.addActionListener(new FileChooserActionListener(engine, "saveGameState", fileChooser, null));

		JButton loadButton = new JButton(myLanguageResources.getString("LOAD_TEXT"));
		loadButton.addActionListener(new FileChooserActionListener(engine, "loadGameState", fileChooser, null));
		JButton speedUpButton = new JButton(myLanguageResources.getString("SPEED_UP_TEXT"));
		speedUpButton.addActionListener(new MethodAction (engine, "speedUp"));

		JButton slowDownButton = new JButton(myLanguageResources.getString("SLOW_DOWN_TEXT"));
		slowDownButton.addActionListener(new MethodAction (engine, "slowDown"));

		JButton quitButton = makeQuitButton();

		JButton addTowerButton = new JButton(myLanguageResources.getString("ADD_TOWER_TEXT"));
		addTowerButton.addActionListener(new MethodAction (engine, "toggleAddTower"));

		JButton soundButton = new JButton(myLanguageResources.getString("SOUND_ONOFF_TEXT"));
		soundButton.addActionListener(new MethodAction (this, "toggleSound"));

		towerChooser = new ObjectChooser(engine.getPossibleTowers());
		towerChooser.register((Observing) engine);
		//powerUpChooser = new ObjectChooser(engine.getPossibleItems());

		List<Subject> engineSubjectList = new ArrayList<Subject>();
		engineSubjectList.add(towerChooser);
		//engineSubjectList.add(powerUpChooser);
		engine.setSubject(engineSubjectList);//This probably does not belong here


		gameButtonPanel.add(mainMenuButton);
		gameButtonPanel.add(playResumeButton);
		gameButtonPanel.add(saveButton);
		gameButtonPanel.add(loadButton);
		gameButtonPanel.add(speedUpButton);
		gameButtonPanel.add(slowDownButton);
		gameButtonPanel.add(quitButton);
		gameButtonPanel.add(soundButton);
		gameButtonPanel.add(addTowerButton);
		gameButtonPanel.add(towerChooser);
		//gameButtonPanel.add(itemChooser);
		return gameButtonPanel;
	}

	public void toggleSound(){
		soundOn = !soundOn;
		if(soundOn) {
			song.loop();
		}
		else {
			song.stop();
		}
	}

	private JPanel makeGameInfoPanel() {
		ObservingPanel gameInfoPanel = new GameInfoPanel();
		gameInfoPanel.setSubject((Subject) engine);
		engine.register(gameInfoPanel);
		return gameInfoPanel;
	}

	private JPanel makeUnitInfoPanel() {
		ObservingPanel unitInfoPanel = new UnitInfoPanel();
		unitInfoPanel.setSubject((Subject) engine);
		engine.register(unitInfoPanel);
		return unitInfoPanel;
	}

	/*	private JPanel makeInfoPanel(String className){
		Object infoPanel = Class.forName(className).newInstance();

		//return infoPanel;
	}
	 */
	//TODO: need to add when game ends to route to here, also need to work on saving the scores 
	private void addHighScoreCard(){
		HighScoreCard highScoreCard = new HighScoreCard();
		highScoreCard.setSubject((Subject) engine);
		engine.register(highScoreCard);
		cards.add(highScoreCard, HIGH_SCORE_CARD);
	}

	private void addOptionsCard() {
		JPanel optionCard = new JPanel();
		optionCard.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		optionCard.add(makeMainMenuButton(), constraints);
		/*
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 1;
		optionCard.add(new InfoPanel(DIFFICULTY), constraints);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 2;
		optionCard.add(new DifficultyPanel(engine), constraints);
		 */
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 1;
		// need to make sound label be centered
		JLabel soundLabel = new JLabel(myLanguageResources.getString("SOUND"));
		soundLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		optionCard.add(soundLabel, constraints);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 2;
		optionCard.add(makeSoundRadioButtonPanel(), constraints);

		cards.add(optionCard, OPTION_CARD);
	}


	private JPanel makeSoundRadioButtonPanel(){
		JPanel soundRadioButtonPanel = new JPanel();
		JCheckBox soundCheckBox = new JCheckBox(myLanguageResources.getString("MUSIC_TEXT"));
		soundCheckBox.addActionListener(new MethodAction(this, "toggleSound"));
		soundRadioButtonPanel.add(soundCheckBox);

		return soundRadioButtonPanel;
	}


	private void addHelpCard() {
		JPanel helpCard = new JPanel();
		helpCard.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 1;
		helpCard.add(makeMainMenuButton(), constraints);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		helpCard.add(new HelpTextPanel(myLanguageResources.getString("HELP")), constraints);

		cards.add(helpCard, HELP_CARD);
	}

	private void addCreditsCard() {
		JTextArea creditsArea = new JTextArea(10,40);
		creditsArea.setEditable(false);
		creditsArea.append(myLanguageResources.getString("CREDITS"));

		JPanel creditsCard = new JPanel();
		creditsCard.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 1;
		creditsCard.add(makeMainMenuButton(), constraints);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		creditsCard.add(creditsArea, constraints);

		cards.add(creditsCard, CREDITS_CARD);
	}

	private JButton makeMainMenuButton() {
		JButton mainMenuButton = new JButton(myLanguageResources.getString("MAIN_MENU_TEXT"));
		mainMenuButton.addActionListener(new MethodAction(engine, "toggleRunning"));
		mainMenuButton.addActionListener(new MethodAction(this, "showCard", WELCOME_CARD));
		return mainMenuButton;
	}

	private JButton makeQuitButton(){
		JButton exitButton = new JButton(myLanguageResources.getString("QUIT_TEXT"));
		exitButton.addActionListener(new MethodAction(this,"quit"));
		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		return exitButton;
	}

	public void quit(){
		System.exit(0);
	}

	private void show() {
		cardLayout.show(cards, WELCOME_CARD);
		frame.getContentPane().add(cards, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}