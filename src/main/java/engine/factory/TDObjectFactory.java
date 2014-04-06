package main.java.engine.factory;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.engine.objects.monster.Monster;
import main.java.engine.objects.tower.Tower;
import main.java.exceptions.engine.MonsterCreationFailureException;
import main.java.exceptions.engine.TowerCreationFailureException;
import main.java.schema.MonsterSchema;
import main.java.schema.TDObjectSchema;
import main.java.schema.TowerSchema;
import jgame.impl.JGEngineInterface;
import main.java.util.Reflection;

public class TDObjectFactory {
	private JGEngineInterface engine;
	private Map<String, TDObjectSchema> tdObjectSchemaMap;


	public TDObjectFactory(JGEngineInterface engine) {
		this.engine = engine;
		tdObjectSchemaMap = new HashMap<>();;
	}

	public void loadSchemas(List<TDObjectSchema> schemas) {
		//TODO: Get rid of repetition in loading schemas
		for (TDObjectSchema s : schemas) {
            tdObjectSchemaMap.put(s.getMyName(), s);
        }
	}

    /**
     * Place tower at a given location's tile.
     *
     * @param location The coordinates to place the tower at
     * @param towerName The name of the tower to place
     * @return The new Tower object
     * @throws TowerCreationFailureException
     */
	public Tower placeTower(Point2D location, String towerName) throws TowerCreationFailureException {
		Point2D tileOrigin = findTileOrigin(location);
		try {
			TowerSchema schema = (TowerSchema) tdObjectSchemaMap.get(towerName);
			Object[] towerParameters = {tileOrigin, schema};
        	return (Tower) placeObject(schema.getMyConcreteType(), towerParameters);
		} catch (Exception e) {
			throw new TowerCreationFailureException();
		}
	}
	
	public Monster placeMonster(Point2D entrance, Point2D exit, String userMonsterName) throws MonsterCreationFailureException {
		System.out.println(tdObjectSchemaMap.keySet().toString());
		MonsterSchema schema = (MonsterSchema) tdObjectSchemaMap.get(userMonsterName);
		Object[] monsterParameters = {entrance, exit, schema};
    	return (Monster) placeObject(schema.getMyConcreteType(), monsterParameters);
		/*try {
			MonsterSchema schema = monsterSchemas.get(userMonsterName);
			Object[] monsterParameters = {entrance, exit, schema};
        	return (Monster) placeObject("main.java.engine.objects.monster.",schema.getMyConcreteType(), monsterParameters);
		} catch (Exception e) {
			throw new MonsterCreationFailureException();
		}*/
	}

	private Object placeObject(Class objectType, Object[] parameters) {
		return Reflection.createInstance(objectType.getName(), parameters);
	}

    /**
     * Find the top-left corner associated with the tile associated with the given location. Used to place
     * new objects and images.
     *
     * @param location Coordinate of the map used to find the associated file
     * @return The top left corner of the tile at the given coordinate
     */
	private Point2D findTileOrigin(Point2D location) {
		int curXTilePos = (int) location.getX()/engine.tileWidth() * engine.tileWidth();
		int curYTilePos = (int) location.getY()/engine.tileHeight() * engine.tileHeight();
		return new Point2D.Double(curXTilePos, curYTilePos);
	}
}