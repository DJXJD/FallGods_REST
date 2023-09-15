package dev.djxjd.fallgods.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import dev.djxjd.fallgods.beans.Match;
import dev.djxjd.fallgods.beans.Minigame;
import dev.djxjd.fallgods.beans.Player;
import dev.djxjd.fallgods.beans.Round;

public interface RoundRepository extends DBEntityRepository<Round> {
	
	public List<Round> findAllByMatchNotNull();
	public List<Round> findAllByMatch(Match match);
	public List<Round> findAllByPlayersFinishedNotNull();
	@Query("SELECT r FROM Round r WHERE KEY(r.playersFinished) = ?1")
	public List<Round> findAllByPlayersFinishedContaining(Player player);
	public List<Round> findAllByGameModeNotNull();
	public List<Round> findAllByGameMode(Minigame minigame);

}
