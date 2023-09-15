package dev.djxjd.fallgods.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import dev.djxjd.fallgods.beans.GameSession;
import dev.djxjd.fallgods.beans.Match;
import dev.djxjd.fallgods.beans.Player;

public interface MatchRepository extends DBEntityRepository<Match> {
	
	public List<Match> findAllByPlayersNotEmpty();
	public List<Match> findAllByPlayersContaining(Player player);
	public List<Match> findAllBySessionNotNull();
	public List<Match> findAllBySession(GameSession session);
	
	@Query("SELECT m FROM Match m WHERE m.startDateTime < ?1 AND m.session IS NOT NULL ORDER BY m.startDateTime DESC")
    public List<Match> findAllWithSessionBefore(LocalDateTime startDateTime); // to find prev session
	
	@Query("SELECT m FROM Match m WHERE m.startDateTime > ?1 AND m.session IS NOT NULL ORDER BY m.startDateTime ASC")
    public List<Match> findAllWithSessionAfter(LocalDateTime startDateTime); // to find next session
	
	@Query("SELECT m FROM Match m WHERE m.session IS NOT NULL ORDER BY m.startDateTime DESC")
	public List<Match> findAllByRecencyWithSession(); // to find most recent session

}
