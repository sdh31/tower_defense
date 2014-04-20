package main.java.author.view.tabs.tower;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import main.java.author.controller.TabController;
import main.java.author.controller.tabbed_controllers.TowerController;
import main.java.author.util.ComboBoxUtil;
import main.java.author.view.components.ImageCanvas;
import main.java.author.view.components.BehaviorTogglingRadioButton;
import main.java.author.view.global_constants.ObjectEditorConstants;
import main.java.author.view.tabs.EditorTab;
import main.java.author.view.tabs.ObjectEditorTab;
import main.java.engine.objects.tower.TowerBehaviors;
import main.java.schema.tdobjects.TDObjectSchema;
import main.java.schema.tdobjects.TowerSchema;

public class TowerEditorTab extends ObjectEditorTab {

	private JSpinner healthSpinner, costSpinner, damageSpinner, rangeSpinner,
			buildUpSpinner, firingSpeedSpinner, shrapnelDamageSpinner,
			moneyFarmAmountSpinner, moneyFarmIntervalSpinner,
			freezeRatioSpinner;

	private BehaviorTogglingRadioButton freezeToggleButton, shootsToggleButton,
			moneyFarmingToggleButton, bombingToggleButton;

	private JComboBox<String> upgradeDropDown;

	protected ImageCanvas bulletImageCanvas, towerImageCanvas,
			shrapnelImageCanvas;
	protected JButton collisionImageButton, shrapnelImageButton;

	public TowerEditorTab(TabController towerController, String objectName) {
		super(towerController, objectName);
	}

	@Override
	public void saveTabData() {
		TowerController controller = (TowerController) myController;
		
		List<TowerSchema> towerSchemas = new ArrayList<TowerSchema>();
		for (TDObjectSchema tower : objectMap.values()) {
			TowerSchema towerSchema = new TowerSchema();
			Map<String, Serializable> towerAttributes = tower.getAttributesMap();
			
			for (String attribute : towerAttributes.keySet()) {
				Serializable castedAttribute = addCastToAttribute(towerAttributes.get(attribute));
				towerSchema.addAttribute(attribute, castedAttribute);				
			}
			towerSchema.addAttribute(TDObjectSchema.IMAGE_NAME, "tower.gif");
			towerSchemas.add(towerSchema);
		}
		controller.addTowers(towerSchemas);
	}

	@Override
	protected void addListeners() {
		super.addListeners();
		shootsToggleButton.setFieldsToToggle(damageSpinner, firingSpeedSpinner,
				shrapnelDamageSpinner, rangeSpinner, freezeToggleButton,
				bombingToggleButton);
		bombingToggleButton.setFieldsToToggle(shrapnelDamageSpinner);
		moneyFarmingToggleButton.setFieldsToToggle(moneyFarmAmountSpinner,
				moneyFarmIntervalSpinner);
		freezeToggleButton.setFieldsToToggle(freezeRatioSpinner);
		upgradeDropDown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateSchemaDataFromView();
			}
		});

		shrapnelImageButton.addActionListener(new FileChooserListener(
				shrapnelImageCanvas));
		collisionImageButton.addActionListener(new FileChooserListener(
				bulletImageCanvas));
		towerImageButton.addActionListener(new FileChooserListener(
				towerImageCanvas));
	}

	@Override
	protected TDObjectSchema createSpecificNewObject(String objectName) {
		return new TowerSchema(objectName);

	}

	@Override
	protected ObjectTabViewBuilder createSpecificTabViewBuilder() {
		return new TowerTabViewBuilder(this);
	}

	@Override
	protected void updateSchemaDataFromView() {
		super.updateSchemaDataFromView();

		TDObjectSchema myCurrentObject = getSelectedObject();
		List<TowerBehaviors> behaviorsToggled = new ArrayList<TowerBehaviors>();
		for (BehaviorTogglingRadioButton button : behaviorTogglingButtons) {
			if (button.isSelected()) {
				behaviorsToggled.add(button.getBehavior());
			}

		}
		myCurrentObject.addAttribute(TowerSchema.TOWER_BEHAVIORS,
				(Serializable) behaviorsToggled);

		myCurrentObject.addAttribute(upgradeDropDown.getName(),
				(String) upgradeDropDown.getSelectedItem());

	}

	/**
	 * 
	 * puts the schema data into the view field
	 * 
	 * @param map
	 *            the object's schema attributes
	 * 
	 */
	@Override
	protected void updateViewWithSchemaData(Map<String, Serializable> map) {
		super.updateViewWithSchemaData(map);

		upgradeDropDown.removeAllItems();
		for (String tower : objectMap.keySet()) {
			if (!tower.equals(getSelectedObjectName()))
				upgradeDropDown.addItem(tower);
		}

		if (ComboBoxUtil.containsValue(upgradeDropDown,
				(String) map.get(upgradeDropDown.getName()))) {
			upgradeDropDown.setSelectedItem(map.get(upgradeDropDown.getName()));
		} else {
			upgradeDropDown.addItem(TowerSchema.UPGRADE_PATH_NONE);
			upgradeDropDown.setSelectedItem(TowerSchema.UPGRADE_PATH_NONE);
		}

		List<TowerBehaviors> behaviorsToToggle = (List<TowerBehaviors>) map.get(TowerSchema.TOWER_BEHAVIORS);
		for (BehaviorTogglingRadioButton radioButton : behaviorTogglingButtons) {
			if (behaviorsToToggle.contains(radioButton.getBehavior())) {
				radioButton.setSelected(true);
			} else {
				radioButton.setSelected(false);
			}
		}

	}

	private class TowerTabViewBuilder extends ObjectTabViewBuilder {

		public TowerTabViewBuilder(EditorTab editorTab) {
			super(editorTab);
		}

		private JComponent makeBulletGraphicPane() {
			JPanel result = new JPanel();
			result.setLayout(new BorderLayout());
			bulletImageCanvas.setSize(new Dimension(
					ObjectEditorConstants.IMAGE_CANVAS_SIZE / 2,
					ObjectEditorConstants.IMAGE_CANVAS_SIZE / 2));
			bulletImageCanvas.setBackground(Color.BLACK);
			result.add(bulletImageCanvas, BorderLayout.CENTER);
			collisionImageButton = makeChooseGraphicsButton("Set Bullet Image");
			result.add(collisionImageButton, BorderLayout.SOUTH);
			return result;
		}

		private JComponent makeShrapnelGraphicPane() {
			JPanel result = new JPanel();
			result.setLayout(new BorderLayout());
			shrapnelImageCanvas.setSize(new Dimension(
					ObjectEditorConstants.IMAGE_CANVAS_SIZE / 2,
					ObjectEditorConstants.IMAGE_CANVAS_SIZE / 2));
			shrapnelImageCanvas.setBackground(Color.BLACK);
			result.add(shrapnelImageCanvas, BorderLayout.CENTER);
			shrapnelImageButton = makeChooseGraphicsButton("Set Shrapnel Image");
			result.add(shrapnelImageButton, BorderLayout.SOUTH);
			return result;
		}

		private JComboBox<String> makeUpgradeDropdown() {

			JComboBox<String> result = new JComboBox<String>();
			result.setName(TowerSchema.UPGRADE_PATH);
			return result;
		}


		@Override
		protected void instantiateAndClumpFields() {
			// spinners
			healthSpinner = makeAttributeSpinner(TowerSchema.HEALTH);
			costSpinner = makeAttributeSpinner(TowerSchema.COST);
			damageSpinner = makeAttributeSpinner(TowerSchema.DAMAGE);
			rangeSpinner = makeAttributeSpinner(TowerSchema.RANGE);
			buildUpSpinner = makeAttributeSpinner(TowerSchema.BUILDUP);
			firingSpeedSpinner = makeAttributeSpinner(TowerSchema.FIRING_SPEED);
			shrapnelDamageSpinner = makeAttributeSpinner(TowerSchema.SHRAPNEL_DAMAGE);
			moneyFarmAmountSpinner = makeAttributeSpinner(TowerSchema.MONEY_GRANTED);
			moneyFarmIntervalSpinner = makeAttributeSpinner(TowerSchema.MONEY_GRANT_INTERVAL);
			freezeRatioSpinner = makeAttributeSpinner(
					TowerSchema.FREEZE_SLOWDOWN_PROPORTION, true);
			// radio buttons
			shootsToggleButton = new BehaviorTogglingRadioButton(
					TowerViewConstants.TOWER_BEHAVIOR_SHOOTS, TowerBehaviors.SHOOTING,
					true);
			freezeToggleButton = new BehaviorTogglingRadioButton(
					TowerViewConstants.TOWER_BEHAVIOR_FREEZES,
					TowerBehaviors.FREEZING, true);
			bombingToggleButton = new BehaviorTogglingRadioButton(
					TowerViewConstants.TOWER_BEHAVIOR_BOMBS, TowerBehaviors.BOMBING,
					true);
			moneyFarmingToggleButton = new BehaviorTogglingRadioButton(
					TowerViewConstants.TOWER_BEHAVIOR_FARMS_MONEY,
					TowerBehaviors.MONEY_FARMING, true);
			// other
			upgradeDropDown = makeUpgradeDropdown();
			// canvases
			bulletImageCanvas = new ImageCanvas(true,
					TowerSchema.BULLET_IMAGE_NAME);
			shrapnelImageCanvas = new ImageCanvas(true,
					TowerSchema.SHRAPNEL_IMAGE_NAME);
			towerImageCanvas = new ImageCanvas(false,
					TowerSchema.TOWER_IMAGE_NAME);
			// clump data types
			clumpFieldsIntoGroups();

		}

		private void clumpFieldsIntoGroups() {
			JSpinner[] spinners = { healthSpinner, costSpinner, buildUpSpinner,
					damageSpinner, rangeSpinner, firingSpeedSpinner,
					shrapnelDamageSpinner, moneyFarmAmountSpinner,
					moneyFarmIntervalSpinner, freezeRatioSpinner };
			spinnerFields = new ArrayList<JSpinner>(Arrays.asList(spinners));
			BehaviorTogglingRadioButton[] buttons = { shootsToggleButton,
					freezeToggleButton, moneyFarmingToggleButton,
					bombingToggleButton };
			behaviorTogglingButtons = new ArrayList<BehaviorTogglingRadioButton>(
					Arrays.asList(buttons));
			ImageCanvas[] canvases = { bulletImageCanvas, shrapnelImageCanvas,
					towerImageCanvas };
			imageCanvases = new ArrayList<ImageCanvas>(Arrays.asList(canvases));
		}

		@Override
		protected JComponent makeFieldPane() {
			JPanel result = new JPanel(new GridLayout(0, 3));
			for (JSpinner spinner : spinnerFields) {
				result.add(makeFieldTile(spinner));
			}
			result.add(makeFieldTile(upgradeDropDown));
			return result;
		}

		@Override
		protected JComponent makePrimaryObjectGraphicPane() {
			JPanel result = new JPanel();
			result.setLayout(new BorderLayout());

			towerImageCanvas.setSize(new Dimension(
					ObjectEditorConstants.IMAGE_CANVAS_SIZE,
					ObjectEditorConstants.IMAGE_CANVAS_SIZE));
			towerImageCanvas.setBackground(Color.BLACK);
			result.add(towerImageCanvas, BorderLayout.CENTER);
			towerImageButton = makeChooseGraphicsButton("Set " + objectName
					+ " Image");
			result.add(towerImageButton, BorderLayout.SOUTH);
			return result;
		}

		@Override
		protected JComponent makeSecondaryImagesGraphicPane() {
			JPanel result = new JPanel();
			result.setLayout(new BorderLayout());
			result.add(makeBulletGraphicPane(), BorderLayout.WEST);
			result.add(makeShrapnelGraphicPane(), BorderLayout.CENTER);
			return result;
		}

	}

}
