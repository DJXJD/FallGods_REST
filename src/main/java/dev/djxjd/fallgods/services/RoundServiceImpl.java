package dev.djxjd.fallgods.services;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import dev.djxjd.fallgods.beans.Match;
import dev.djxjd.fallgods.beans.Minigame.GameType;
import dev.djxjd.fallgods.beans.Player;
import dev.djxjd.fallgods.beans.Round;
import dev.djxjd.fallgods.repositories.MatchRepository;
import dev.djxjd.fallgods.repositories.RoundRepository;

@Service
public class RoundServiceImpl extends DBEntityServiceImpl<Round> implements RoundService {

	private MatchRepository mRepo;
	private DBEntityService<Match> mService;
	
	public RoundServiceImpl(RoundRepository tRepo, MatchRepository mRepo,
			@Lazy DBEntityService<Match> mService) { 
		super(tRepo, List.of());
		this.mRepo = mRepo;
		this.mService = mService;
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

	@Override
	public Round deriveTransientFields(Round r) {
		if (r.isTransientFieldsDerived()) return r;
		super.deriveTransientFields(r);
		if (r.getMatch() != null) {
			r.setNum((byte) (r.getMatch().getRounds().indexOf(r) + 1));
			if (r.getNum() == 1) {
				r.setDuration(Duration.between(r.getMatch().getStartDateTime(), r.getEndDateTime()));
			} else {
				r.setDuration(Duration.between(r.getMatch().getRounds().get(r.getNum() - 2).getEndDateTime(), r.getEndDateTime()));
			}
			r.setFinalRound(r.isEarlyFinalRound());
			if (r.getGameMode() != null && !r.isEarlyFinalRound())
				r.setFinalRound(r.getGameMode().getType().equals(GameType.FINAL) || !r.getTeamQualified());
			mService.deriveTransientFields(r.getMatch());
		}
		return r;
	}

}
