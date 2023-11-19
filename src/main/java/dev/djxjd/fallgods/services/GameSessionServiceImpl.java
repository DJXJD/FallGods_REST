package dev.djxjd.fallgods.services;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.context.annotation.Lazy;
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
	private DBEntityService<Match> mService;
	private PlayerService pService;
	
	public GameSessionServiceImpl(GameSessionRepository tRepo, MatchRepository mRepo,
			DBEntityService<Match> mService, @Lazy PlayerService pService) {
		super(tRepo, List.of(mService));
		this.mRepo = mRepo;
		this.mService = mService;
		this.pService = pService;
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
				.findFirst().map(Match::getSession).map(this::deriveTransientFields).orElse(null);
	}

	@Override
	public GameSession getFollowingSameMainPlayers(Long id) {
		Optional<GameSession> gs = getTRepo().findById(id);
		if (gs.isEmpty() || gs.get().getMatches().isEmpty() || gs.get().getMainPlayers().isEmpty()) return null;
		return mRepo.findAllWithSessionAfter(gs.get().getMatches().get(gs.get().getMatches().size() - 1).getStartDateTime())
				.stream().filter(m -> m.getSession().getMainPlayers().equals(gs.get().getMainPlayers()))
				.findFirst().map(Match::getSession).map(this::deriveTransientFields).orElse(null);
	}

	@Override
	public GameSession getLatestWithMainPlayers(Set<Player> mainPlayers) {
		return getTRepo().findAll().stream()
				.filter(gs -> gs.getMainPlayers().equals(mainPlayers))
				.sorted().findFirst().map(this::deriveTransientFields)
				.map(GameSession::unproxy).orElse(null);
	}

	@Override
	public GameSession deriveTransientFields(GameSession gs) {
		if (gs.isTransientFieldsDerived()) return gs;
		super.deriveTransientFields(gs);
		if (gs.getMatches() != null && !gs.getMatches().isEmpty()) {
			gs.getMatches().forEach(m -> mService.deriveTransientFields(m));
			gs.setStreaks(new ArrayList<>());
			deriveWinsLossesStreaks(gs);
			gs.setWinRate((float) gs.getWins() / (gs.getWins() + gs.getLosses()) * 100);
			deriveCurrentStreakHighestStreak(gs);
			continuePriorStreak(gs);
			checkLastStreakContinuing(gs);
			deriveDuration(gs);
		}
		if (gs.getMainPlayers() != null) gs.getMainPlayers().forEach(p -> pService.deriveTransientFields(p));
		return gs;
	}
	
	private void deriveWinsLossesStreaks(GameSession gs) {
		boolean prevIsWin = false;
		int streakNum = 0;
		for (int i = 0; i < gs.getMatches().size(); i++) {
			Match m = gs.getMatches().get(i);
			if (m.isWon()) {
				gs.setWins(gs.getWins() + 1);
				if (prevIsWin) {
					List<Match> streak;
					try {
						streak = gs.getStreaks().get(streakNum);
					} catch (IndexOutOfBoundsException e) {
						gs.getStreaks().add(new ArrayList<Match>());
						streak = gs.getStreaks().get(streakNum);
						streak.add(gs.getMatches().get(i - 1));
					}
					streak.add(m);
				}
				prevIsWin = true;
			} else {
				prevIsWin = false;
				if (!gs.getStreaks().isEmpty() &&
						gs.getStreaks().get(gs.getStreaks().size() - 1).contains(gs.getMatches().get(i - 1)))
					streakNum++;
				if (m.isFinished()) gs.setLosses(gs.getLosses() + 1);
			}
		}
	}
	
	private void deriveCurrentStreakHighestStreak(GameSession gs) {
		gs.getStreaks().forEach(s -> { if (s.size() > gs.getHighestStreak()) gs.setHighestStreak(s.size()); });
		int latestCompletedMatchIndex = gs.getMatches().size() - 1;
		if (!gs.getMatches().get(latestCompletedMatchIndex).isFinished()) latestCompletedMatchIndex -= 1;
		if (latestCompletedMatchIndex >= 0) {
			Match m = gs.getMatches().get(latestCompletedMatchIndex);
			if (m.isWon()) {
				if (!gs.getStreaks().isEmpty() && gs.getStreaks().get(gs.getStreaks().size() - 1).contains(m))
					gs.setCurrentStreak(gs.getStreaks().get(gs.getStreaks().size() - 1).size());
				else gs.setCurrentStreak(1);
				if (gs.getHighestStreak() == 0) gs.setHighestStreak(1);
			}
		}
	}
	
	private void continuePriorStreak(GameSession gs) {
		if (gs.getLosses() == 0 || gs.getMatches().get(0).isWon()) {
			GameSession prevSession = getPreviousSameMainPlayers(gs.getId());
			if (prevSession == null || !prevSession.isFinished() ||
					!prevSession.getMatches().get(prevSession.getMatches().size() - 1).isWon()) return;
			gs.setPriorStreakSessionId(prevSession.getId());
			gs.setPriorStreakSize(prevSession.getCurrentStreak());
			if (gs.getLosses() == 0) {
				gs.setCurrentStreak(gs.getCurrentStreak() + gs.getPriorStreakSize());
				gs.setHighestStreak(gs.getHighestStreak() + gs.getPriorStreakSize());
			} else if (gs.getMatches().get(0).isWon()) {
				int continuedStreakSize = gs.getPriorStreakSize() + 1;
				if (!gs.getStreaks().isEmpty() && gs.getStreaks().get(0).contains(gs.getMatches().get(0)))
					continuedStreakSize += gs.getStreaks().get(0).size() - 1;
				if (continuedStreakSize > gs.getHighestStreak()) gs.setHighestStreak(continuedStreakSize);
			}
		}
	}
	
	private void checkLastStreakContinuing(GameSession gs) {
		if (!gs.isFinished() || !gs.getMatches().get(gs.getMatches().size() - 1).isWon()) return;
		GameSession nextSession = getFollowingSameMainPlayers(gs.getId());
		if (nextSession == null || !nextSession.getMatches().get(0).isWon()) return;
		gs.setLastStreakContinuedSessionId(nextSession.getId());
	}
	
	private void deriveDuration(GameSession gs) {
		Match lastMatchWithRounds = gs.getMatches().get(gs.getMatches().size() - 1);
		if (lastMatchWithRounds.getRounds().isEmpty()) {
			if (gs.getMatches().get(0).equals(lastMatchWithRounds)) { gs.setDuration(Duration.ZERO); return; }
			else lastMatchWithRounds = gs.getMatches().get(gs.getMatches().size() - 2);
		}
		gs.setDuration(Duration.between(gs.getMatches().get(0).getStartDateTime(),
				lastMatchWithRounds.getRounds().get(lastMatchWithRounds.getRounds().size() - 1).getEndDateTime()));
	}

}
