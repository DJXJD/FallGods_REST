package dev.djxjd.fallgods.services;

import dev.djxjd.fallgods.beans.Minigame;
import dev.djxjd.fallgods.beans.Minigame.GameType;

public interface MinigameService extends DBEntityService<Minigame> {
	
	public boolean existsByNameIgnoringCaseAndType(String name, GameType type);

}
