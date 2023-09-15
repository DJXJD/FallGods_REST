package dev.djxjd.fallgods.services.listeners;

import java.time.Duration;

import dev.djxjd.fallgods.beans.Match;
import dev.djxjd.fallgods.beans.Round;
import jakarta.persistence.PostLoad;

public class MatchListener {
	
	@PostLoad
	private void updateTransientData(Match m) {
		if (m.getRounds() == null || m.getRounds().isEmpty()) {
			m.setDuration(Duration.ZERO);
			return;
		}
		
		Round lastRound = m.getRounds().get(m.getRounds().size() - 1);
		
		m.setFinished(lastRound.isFinalRound() || !lastRound.getTeamQualified());
		
		m.setDuration(Duration.between(m.getStartDateTime(), lastRound.getEndDateTime()));
		
		if (!m.isFinished()) return;
		
		m.setWon(lastRound.getTeamQualified());
	}

}
