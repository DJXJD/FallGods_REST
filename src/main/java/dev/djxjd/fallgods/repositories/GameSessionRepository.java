package dev.djxjd.fallgods.repositories;

import java.util.List;

import dev.djxjd.fallgods.beans.GameSession;
import dev.djxjd.fallgods.beans.Player;

public interface GameSessionRepository extends DBEntityRepository<GameSession> {
	
	public List<GameSession> findAllByMainPlayersContaining(Player player);
	public List<GameSession> findAllByMainPlayersNotEmpty();

}
