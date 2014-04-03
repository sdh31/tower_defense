package main.java.author.view.tabs.terrain.types;

import java.awt.Color;

public class Tree extends TileObject {

	@Override
	public boolean canWalkOver() {
		return false;
	}

	@Override
	public Color getBGColor() {
		return new Color(0, 100, 0);
	}

	@Override
	public String getImage() {
		return "tree.png";
	}

}
