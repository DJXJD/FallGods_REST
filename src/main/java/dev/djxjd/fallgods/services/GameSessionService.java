package dev.djxjd.fallgods.services;

import java.util.Set;

import dev.djxjd.fallgods.beans.GameSession;
import dev.djxjd.fallgods.beans.Player;

public interface GameSessionService extends DBEntityService<GameSession> {
	
	public GameSession getPreviousSameMainPlayers(Long id);
	public GameSession getFollowingSameMainPlayers(Long id);
	public GameSession getLatestWithMainPlayers(Set<Player> mainPlayers);

}
