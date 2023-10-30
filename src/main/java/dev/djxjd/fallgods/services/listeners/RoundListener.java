package dev.djxjd.fallgods.services.listeners;

import java.time.Duration;

import dev.djxjd.fallgods.beans.Minigame.GameType;
import dev.djxjd.fallgods.beans.Round;
import jakarta.persistence.PostLoad;

public class RoundListener {
	
	@PostLoad
	private void updateTransientData(Round r) {
		if (r.getMatch() == null) return;
		r.setNum((byte) (r.getMatch().getRounds().indexOf(r) + 1));
		if (r.getNum() == 1) {
			r.setDuration(Duration.between(r.getMatch().getStartDateTime(), r.getEndDateTime()));
		} else {
			r.setDuration(Duration.between(r.getMatch().getRounds().get(r.getNum() - 2).getEndDateTime(), r.getEndDateTime()));
		}
		
		r.setFinalRound(r.isEarlyFinalRound());
		
		if (r.getGameMode() == null) return;
		if (!r.isEarlyFinalRound()) r.setFinalRound(r.getGameMode().getType().equals(GameType.FINAL) || !r.getTeamQualified());
	}

}
