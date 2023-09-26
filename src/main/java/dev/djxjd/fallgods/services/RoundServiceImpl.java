package dev.djxjd.fallgods.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.djxjd.fallgods.beans.Player;
import dev.djxjd.fallgods.beans.Round;
import dev.djxjd.fallgods.repositories.MatchRepository;
import dev.djxjd.fallgods.repositories.RoundRepository;

@Service
public class RoundServiceImpl extends DBEntityServiceImpl<Round> implements RoundService {

	private MatchRepository mRepo;
	
	public RoundServiceImpl(RoundRepository tRepo, MatchRepository mRepo) { 
		super(tRepo, List.of());
		this.mRepo = mRepo;
	}

	@Override
	public Map<Player, Boolean> putPlayerFinished(Long id, Long playerId, Boolean finished) {
		Optional<Round> r = getTRepo().findById(id);
		if (r.isEmpty() || !r.get().getMatch().getPlayers().contains(Player.builder().id(playerId).build())) return null;
		r.get().getPlayersFinished().put(Player.builder().id(playerId).build(), finished);
		return getTRepo().save(r.get()).getPlayersFinished();
	}

	@Override
	public Boolean removePlayerFinished(Long id, Long playerId) {
		Optional<Round> r = getTRepo().findById(id);
		if (r.isEmpty()) return null;
		Boolean removed = r.get().getPlayersFinished().remove(Player.builder().id(playerId).build());
		getTRepo().save(r.get());
		return removed != null;
	}	
	
	@Override
	public boolean catchGarbageData(Round round) {
		return !mRepo.findById(round.getMatch().getId()).get().getPlayers().contains(round.getMvp()) && round.getMvp() != null; 
	}

}
