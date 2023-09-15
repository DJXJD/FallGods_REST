package dev.djxjd.fallgods.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.djxjd.fallgods.beans.DBEntity;
import dev.djxjd.fallgods.beans.Match;
import dev.djxjd.fallgods.beans.Player;
import dev.djxjd.fallgods.repositories.MatchRepository;
import dev.djxjd.fallgods.repositories.PlayerRepository;
import dev.djxjd.fallgods.repositories.RoundRepository;

@Service
public class PlayerServiceImpl extends DBEntityServiceImpl<Player> implements PlayerService {

	private MatchRepository mRepo;
	private RoundRepository rRepo;
	
	public PlayerServiceImpl(PlayerRepository tRepo, MatchRepository mRepo, RoundRepository rRepo,
			DBEntityService<Match> mService, RoundService rService) {
		super(tRepo, List.of(mService, rService));
		this.mRepo = mRepo;
		this.rRepo = rRepo;
	}
	
	@Override
	protected List<List<? extends DBEntity<?>>> detachChildrenReferencingElement(Long id) {
		Player p = Player.builder().id(id).build();
		return List.of(
				mRepo.findAllByPlayersContaining(p).stream().map(m -> { m.getPlayers().remove(p); return m; }).toList(),
				rRepo.findAllByPlayersFinishedContaining(p).stream().map(r -> { r.getPlayersFinished().remove(p); return r; }).toList()
				);
	}

	@Override
	protected List<List<? extends DBEntity<?>>> detachChildrenReferencingCollection() {
		return List.of(
				mRepo.findAllByPlayersNotEmpty().stream().map(m -> m.setPlayers(null)).toList(),
				rRepo.findAllByPlayersFinishedNotNull().stream().map(r -> r.setPlayersFinished(null)).toList()
				);
	}

	@Override
	public boolean existsByNameIgnoringCase(String name) {
		return ((PlayerRepository) getTRepo()).existsByNameIgnoringCase(name);
	}

}
