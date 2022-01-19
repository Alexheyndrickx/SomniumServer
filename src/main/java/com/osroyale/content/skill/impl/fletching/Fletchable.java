package com.osroyale.content.skill.impl.fletching;

import com.osroyale.game.world.items.Item;

public interface Fletchable {
	
	int getAnimation();
	
	Item getUse();
	
	Item getWith();
	
	FletchableItem[] getFletchableItems();
	
	Item[] getIngediants();
	
	String getProductionMessage();
}