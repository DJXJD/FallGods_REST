package dev.djxjd.fallgods.services.listeners;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

import dev.djxjd.fallgods.beans.GameSession;
import dev.djxjd.fallgods.beans.Match;
import dev.djxjd.fallgods.services.GameSessionService;
import jakarta.persistence.PostLoad;

public class GameSessionListener {
	
	@Autowired
	private ObjectFactory<GameSessionService> gsServiceFactory;
	
	@PostLoad
	private void updateTransientData(GameSession gs) {
		if (gs.getMatches() == null || gs.getMatches().isEmpty()) return;
		GameSessionService gsService = gsServiceFactory.getObject();
		gs.setStreaks(new ArrayList<List<Match>>());
		deriveWinsLossesStreaks(gs);
		gs.setWinRate((float) gs.getWins() / (gs.getWins() + gs.getLosses()) * 100);
		deriveCurrentStreakHighestStreak(gs);
		continuePriorStreak(gs, gsService);
		checkLastStreakContinuing(gs, gsService);
		deriveDuration(gs);
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
	
	private void continuePriorStreak(GameSession gs, GameSessionService gsService) {
		if (gs.getLosses() == 0 || gs.getMatches().get(0).isWon()) {
			GameSession prevSession = gsService.getPreviousSameMainPlayers(gs.getId());
			if (prevSession == null || !prevSession.isFinished() ||
					!prevSession.getMatches().get(prevSession.getMatches().size() - 1).isWon()) return;
			// this seems to sometimes happen on its own, so in those cases this might waste performance
			updateTransientData(prevSession);
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
	
	private void checkLastStreakContinuing(GameSession gs, GameSessionService gsService) {
		if (!gs.isFinished() || !gs.getMatches().get(gs.getMatches().size() - 1).isWon()) return;
		GameSession nextSession = gsService.getFollowingSameMainPlayers(gs.getId());
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
