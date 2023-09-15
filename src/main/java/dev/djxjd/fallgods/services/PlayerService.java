package dev.djxjd.fallgods.services;

import dev.djxjd.fallgods.beans.Player;

public interface PlayerService extends DBEntityService<Player> {
	
	public boolean existsByNameIgnoringCase(String name);

}
