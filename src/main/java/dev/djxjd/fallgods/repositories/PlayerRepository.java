package dev.djxjd.fallgods.repositories;

import dev.djxjd.fallgods.beans.Player;

public interface PlayerRepository extends DBEntityRepository<Player> {
	
	public boolean existsByNameIgnoringCase(String name);
	
}
