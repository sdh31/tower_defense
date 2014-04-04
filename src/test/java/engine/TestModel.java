package test.java.engine;

import org.junit.Test;

import main.java.engine.Model;
import static org.junit.Assert.*;

public class TestModel {

	@Test
	public void checkTowerIsPlaced() {
		Model model = new Model(new FakeJGEngine());
		
		final int testx = 2, testy = 2;
		
		model.placeTower(testx, testy);
		
		assert(model.isTowerPresent(testx, testy));
	}

	
	@Test
	public void checkModelGetters() {
		Model model = new Model(new FakeJGEngine());
		
		assertEquals(0, model.getGameClock(), .0001);
	}
	
	@Test
	public void checkPlayerGetters() {
		Model model = new Model(new FakeJGEngine());
		model.addNewPlayer();
		
		assert(model.getMoney() >= 0);
		assert(model.getPlayerLife() > 0);
		assertEquals(0, model.getScore(), .0001);
		
	}
}
