package main.java.engine.objects.monster;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jgame.JGColor;
import jgame.JGPoint;
import main.java.engine.objects.Exit;
import main.java.engine.objects.TDObject;
import main.java.engine.objects.monster.jgpathfinder.*;
import main.java.schema.MonsterSpawnSchema;

public abstract class Monster extends TDObject {
	public static final int MONSTER_CID = 1;

	protected double myHealth;
	protected double myMoveSpeed;
	protected double myMoneyValue;
	protected JGPathfinderInterface myPathFinder;
	protected Point2D myEntrance;
	protected Exit myExit;
	protected JGPath myPath;
	protected String originalImage;
	private MonsterSpawnSchema resurrectMonsterSchema;

	/* TODO: Clean up/move instance variables to appropriate concrete classes
	 */
	public Monster (//double x,
			//double y,
			Point2D entrance,
			Exit exit,
			Set<Integer> blocked,
			double health,
			double moveSpeed,
			double rewardAmount,
			String graphic,
			MonsterSpawnSchema resurrectSchema) {
		//TODO make factory add the spread between monsters in the same wave, and remove random from initial x,y
		super("monster", entrance.getX() + Math.random() * 100, entrance.getY() + Math.random() * 100, MONSTER_CID, graphic);
		myHealth = health;
		myMoveSpeed = moveSpeed;
		myMoneyValue = rewardAmount;
		myEntrance = entrance;
		myExit = exit;
		myPathFinder = new JGPathfinder(new JGTileMap(eng, null, blocked), new JGPathfinderHeuristic()); // TODO: clean up
		JGPoint pathEntrance = new JGPoint(eng.getTileIndex(x, y)); // TODO: move into diff method
		JGPoint pathExit = new JGPoint(myExit.getCenterTile());
		this.setSpeed(myMoveSpeed);
		originalImage = graphic;
		this.resurrectMonsterSchema = resurrectSchema;
		try {
			myPath = myPathFinder.getPath(pathEntrance, pathExit);
		} catch (NoPossiblePathException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void move () {
		if (this.xspeed != 0) {
			if (myPath.peek() != null) {
				JGPoint waypoint = eng.getTileCoord(myPath.peek());

				// TODO: refactor, quick implementation to test - jordan
				if (((int) (x + 10) >= waypoint.x && (int) (x - 10) <= waypoint.x) &&
						((int) (y + 10) >= waypoint.y && (int) (y - 10) <= waypoint.y)) {
					waypoint = myPath.getNext();
				}

				xdir = Double.compare(waypoint.x, x);
				ydir = Double.compare(waypoint.y, y);
				setDirSpeed(xdir, ydir, myMoveSpeed);
			} else {
				setSpeed(0);
			}
		}
	}

	/**
	 *  Check if this object has died and should be removed
	 */
	public boolean isDead () {
		return myHealth <= 0;
	}

	/**
	 * Reduce the health of this object by a damage amount.
	 * @param damage afflicting object's damage
	 */
	public void takeDamage (double damage) {
		myHealth -= damage;
	}

	public double getOriginalSpeed() {
		return myMoveSpeed;
	}

	public void reduceSpeed (double speed) {
		myMoveSpeed *= speed;
	}

	/**
	 * Set the monster to be dead immediately, 
	 * effectively removing it from the game
	 */
	public void setDead() {
		myHealth = 0;
		myMoneyValue = 0;
	}

	/**
	 * Get money value received upon death of this monster
	 * @return
	 */
	public double getMoneyValue() {
		return myMoneyValue;
	}
	
	/**
	 * Get the original image of this monster
	 * 
	 * @return original image of the monster
	 */
	public String getOriginalImage() {
		return originalImage;
	}

	@Override
	public void paint() {
		if (myPath != null) {
			for (JGPoint p : myPath) {
				JGPoint coord = eng.getTileCoord(p);
				eng.drawOval(coord.x + eng.tileWidth()/2, coord.y + eng.tileHeight()/2,
						10, 10, true, true, 10, JGColor.yellow);
			}
		}
	}

	/**
	 * Get the schema that should be spawned upon the death of this monster.
	 * Will be null if the monster has no resurrect schema to spawn upon death.
	 * @return
	 */
	public MonsterSpawnSchema getResurrrectMonsterSpawnSchema() {
		return resurrectMonsterSchema;
	}
	
	/**
	 * Get dynamic information about the monster
	 * 
	 * @return
	 */
	public String getInfo() {
		String info = "";
		info+="X-coor: " + x + 
				"\nY-coor: " + y + 
				"\nMoney Value: " + myMoneyValue + 
				"\nHealth: " + myHealth + 
				"\nMove Speed: " + myMoveSpeed;
		return info;
	}
}
