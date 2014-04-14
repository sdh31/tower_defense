package main.java.author.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import main.java.author.controller.MainController;
import main.java.author.controller.TabController;
import main.java.author.controller.tabbed_controllers.EnemyController;
import main.java.author.controller.tabbed_controllers.GameSettingsController;
import main.java.author.controller.tabbed_controllers.TerrainController;
import main.java.author.controller.tabbed_controllers.TowerController;
import main.java.author.controller.tabbed_controllers.WaveController;
import main.java.author.view.menubar.BasicMenuBar;
import main.java.author.view.tabs.GameSettingsEditorTab;
import main.java.author.view.tabs.enemy.EnemyEditorTab;
import main.java.author.view.tabs.terrain.TerrainEditorTab;
import main.java.author.view.tabs.tower.TowerEditorTab;
import main.java.author.view.tabs.wave.WaveEditorTab;
import main.java.exceptions.data.InvalidGameBlueprintException;

/**
 * Frame that represents the GUI for the Authoring environment.
 * 
 */
public class AuthoringView extends JFrame {
	private MainController myController;
	private JButton finalizeGameButton;

	private JTabbedPane tabbedPane = new JTabbedPane();

	private static final String GAME_SETTINGS_EDITOR_STRING = "Game Settings Editor";
	private static final String TOWER_EDITOR_STRING = "Tower Editor";
	private static final String ENEMY_EDITOR_STRING = "Enemy Editor";
	private static final String TERRAIN_EDITOR_STRING = "Terrain Editor";
	private static final String WAVE_EDITOR_STRING = "Wave Editor";

	public AuthoringView(MainController mainController) {
		myController = mainController;
		myController.setView(this);

	}

	/**
	 * Creates the Editor Tabs for the tower, enemy, wave, terrain, etc.
	 */
	public void createEditorTabs() {
		TabController enemyController = new EnemyController(myController);
		TabController towerController = new TowerController(myController);
		TabController waveController = new WaveController(myController);
		TabController gameSettingsController = new GameSettingsController(
				myController);
		TabController terrainController = new TerrainController(myController);

		tabbedPane
				.add(GAME_SETTINGS_EDITOR_STRING,
						new GameSettingsEditorTab(gameSettingsController));
		tabbedPane
				.add(TOWER_EDITOR_STRING,
						new TowerEditorTab(towerController));
		tabbedPane
				.add(ENEMY_EDITOR_STRING,
						new EnemyEditorTab(enemyController));
		tabbedPane
				.add(TERRAIN_EDITOR_STRING,
						new TerrainEditorTab(terrainController));
		tabbedPane
				.add(WAVE_EDITOR_STRING,
						new WaveEditorTab(waveController));
	}

	public void createAndShowGUI() {
		createEditorTabs();
		add(tabbedPane, BorderLayout.CENTER);
		add(createFinalizeGameButton(), BorderLayout.SOUTH);
		setJMenuBar(new BasicMenuBar());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		pack();
		setVisible(true);
		addActionListeners();
	}

	/**
	 * Adds an action listener to the finalize game button which, when clicked,
	 * saves the game blueprint so the data team can begin serializing information
	 */
	private void addActionListeners() {
		finalizeGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (myController.isGameValid()) {
					try {
						myController.saveBlueprint();
					} catch (InvalidGameBlueprintException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
	}
	
	

	/**
	 * Constructs a JButton which allows the user to finalize the game
	 * that they made
	 */
	private JButton createFinalizeGameButton() {
		finalizeGameButton = new JButton("Finalize Game");
		return finalizeGameButton;
	}

}
