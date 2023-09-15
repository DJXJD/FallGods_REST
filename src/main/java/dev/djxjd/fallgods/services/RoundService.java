package dev.djxjd.fallgods.services;

import java.util.Map;

import dev.djxjd.fallgods.beans.Player;
import dev.djxjd.fallgods.beans.Round;

public interface RoundService extends DBEntityService<Round> {
	
	public Map<Player, Boolean> putPlayerFinished(Long id, Long playerId, Boolean finished);
	public Boolean removePlayerFinished(Long id, Long playerId);

}
