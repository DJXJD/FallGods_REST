package dev.djxjd.fallgods.repositories;

import dev.djxjd.fallgods.beans.Minigame;
import dev.djxjd.fallgods.beans.Minigame.GameType;

public interface MinigameRepository extends DBEntityRepository<Minigame> {
	
	public boolean existsByNameIgnoringCaseAndType(String name, GameType type);

}
