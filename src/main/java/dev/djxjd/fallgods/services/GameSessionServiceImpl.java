package dev.djxjd.fallgods.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import dev.djxjd.fallgods.beans.DBEntity;
import dev.djxjd.fallgods.beans.GameSession;
import dev.djxjd.fallgods.beans.Match;
import dev.djxjd.fallgods.beans.Player;
import dev.djxjd.fallgods.repositories.GameSessionRepository;
import dev.djxjd.fallgods.repositories.MatchRepository;

@Service
public class GameSessionServiceImpl extends DBEntityServiceImpl<GameSession> implements GameSessionService {

	private MatchRepository mRepo;
	
	public GameSessionServiceImpl(GameSessionRepository tRepo, MatchRepository mRepo, DBEntityService<Match> mService) {
		super(tRepo, List.of(mService));
		this.mRepo = mRepo;
	}

	@Override
	protected List<List<? extends DBEntity<?>>> detachChildrenReferencingElement(Long id) {
		return List.of(
				mRepo.findAllBySession(GameSession.builder().id(id).build()).stream().map(m -> m.setSession(null)).toList()
				);
	}

	@Override
	protected List<List<? extends DBEntity<?>>> detachChildrenReferencingCollection() {
		return List.of(
				mRepo.findAllBySessionNotNull().stream().map(m -> m.setSession(null)).toList()
				);
	}

	@Override
	public GameSession getPreviousSameMainPlayers(Long id) {
		Optional<GameSession> gs = getTRepo().findById(id);
		if (gs.isEmpty() || gs.get().getMatches().isEmpty() || gs.get().getMainPlayers().isEmpty()) return null;
		return mRepo.findAllWithSessionBefore(gs.get().getMatches().get(0).getStartDateTime())
				.stream().filter(m -> m.getSession().getMainPlayers().equals(gs.get().getMainPlayers()))
				.findFirst().map(Match::getSession).orElse(null);
	}

	@Override
	public GameSession getFollowingSameMainPlayers(Long id) {
		Optional<GameSession> gs = getTRepo().findById(id);
		if (gs.isEmpty() || gs.get().getMatches().isEmpty() || gs.get().getMainPlayers().isEmpty()) return null;
		return mRepo.findAllWithSessionAfter(gs.get().getMatches().get(gs.get().getMatches().size() - 1).getStartDateTime())
				.stream().filter(m -> m.getSession().getMainPlayers().equals(gs.get().getMainPlayers()))
				.findFirst().map(Match::getSession).orElse(null);
	}

	@Override
	public GameSession getLatestWithMainPlayers(Set<Player> mainPlayers) {
		return getTRepo().findAll().stream()
				.filter(gs -> gs.getMainPlayers().equals(mainPlayers))
				.sorted().findFirst().map(GameSession::unproxy).orElse(null);
	}

}
