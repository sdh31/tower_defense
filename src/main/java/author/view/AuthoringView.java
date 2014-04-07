package main.java.author.view;

import java.awt.BorderLayout;
import java.awt.MenuBar;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import main.java.author.controller.MainController;
import main.java.author.view.menubar.BasicMenuBar;
import main.java.author.view.tabs.GameSettingsEditorTab;
import main.java.author.view.tabs.TowerEditorTab;
import main.java.author.view.tabs.enemy.EnemyEditorTab;
import main.java.author.view.tabs.terrain.TerrainEditorTab;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Frame that represents the GUI for the Authoring environment.
 * 
 */
public class AuthoringView extends JFrame {
	private MainController myController;
	private JTabbedPane tabbedPane = new JTabbedPane();

	private static final String GAME_SETTINGS_EDITOR_STRING = "Game Settings Editor";
	private static final String TOWER_EDITOR_STRING = "Tower Editor";
	private static final String ENEMY_EDITOR_STRING = "Enemy Editor";
	private static final String TERRAIN_EDITOR_STRING = "Terrain Editor";

	public AuthoringView() {
		myController = new MainController();
		
	}

	/**
	 * Creates the Editor Tabs for the tower, enemy, wave, terrain, etc.
	 */
	public void createEditorTabs(MainController controller) {
		tabbedPane.add(GAME_SETTINGS_EDITOR_STRING, new GameSettingsEditorTab(
				controller));
		tabbedPane.add(TOWER_EDITOR_STRING, new TowerEditorTab(controller));
		tabbedPane.add(ENEMY_EDITOR_STRING, new EnemyEditorTab(controller));
		tabbedPane.add(TERRAIN_EDITOR_STRING, new TerrainEditorTab(controller));

	}

	public static void main(String[] args) {
		// Secound possibility
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				AuthoringView authoringView = new AuthoringView();
				authoringView.createAndShowGUI();
			}
		});

	}

	private void createAndShowGUI() {
		createEditorTabs(myController);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		setJMenuBar(new BasicMenuBar());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		pack();
		setVisible(true);
	}

}
