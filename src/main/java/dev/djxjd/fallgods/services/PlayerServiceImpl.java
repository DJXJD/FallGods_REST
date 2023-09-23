package dev.djxjd.fallgods.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.djxjd.fallgods.beans.DBEntity;
import dev.djxjd.fallgods.beans.Match;
import dev.djxjd.fallgods.beans.Player;
import dev.djxjd.fallgods.repositories.GameSessionRepository;
import dev.djxjd.fallgods.repositories.MatchRepository;
import dev.djxjd.fallgods.repositories.PlayerRepository;
import dev.djxjd.fallgods.repositories.RoundRepository;

@Service
public class PlayerServiceImpl extends DBEntityServiceImpl<Player> implements PlayerService {

	private MatchRepository mRepo;
	private RoundRepository rRepo;
	private GameSessionRepository gsRepo;
	
	public PlayerServiceImpl(PlayerRepository tRepo,
			MatchRepository mRepo, RoundRepository rRepo, GameSessionRepository gsRepo,
			DBEntityService<Match> mService, RoundService rService, GameSessionService gsService) {
		super(tRepo, List.of(mService, rService, gsService));
		this.mRepo = mRepo;
		this.rRepo = rRepo;
		this.gsRepo = gsRepo;
	}
	
	@Override
	protected List<List<? extends DBEntity<?>>> detachChildrenReferencingElement(Long id) {
		Player p = Player.builder().id(id).build();
		return List.of(
				mRepo.findAllByPlayersContaining(p).stream().map(m -> { m.getPlayers().remove(p); return m; }).toList(),
				rRepo.findAllByPlayersFinishedContaining(p).stream().map(r -> { r.getPlayersFinished().remove(p); return r; }).toList(),
				gsRepo.findAllByMainPlayersContaining(p).stream().map(gs -> { gs.getMainPlayers().remove(p); return gs; }).toList()
				);
	}

	@Override
	protected List<List<? extends DBEntity<?>>> detachChildrenReferencingCollection() {
		return List.of(
				mRepo.findAllByPlayersNotEmpty().stream().map(m -> m.setPlayers(null)).toList(),
				rRepo.findAllByPlayersFinishedNotNull().stream().map(r -> r.setPlayersFinished(null)).toList(),
				gsRepo.findAllByMainPlayersNotEmpty().stream().map(gs -> gs.setMainPlayers(null)).toList()
				);
	}

	@Override
	public boolean existsByNameIgnoringCase(String name) {
		return ((PlayerRepository) getTRepo()).existsByNameIgnoringCase(name);
	}

}
