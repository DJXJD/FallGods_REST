package dev.djxjd.fallgods.services.listeners;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import dev.djxjd.fallgods.beans.Match;
import dev.djxjd.fallgods.beans.Player;
import dev.djxjd.fallgods.beans.Round;
import dev.djxjd.fallgods.beans.wrappers.MinigameData;
import jakarta.persistence.PostLoad;

public class PlayerListener {
	
	@PostLoad
	private void updateTransientData(Player p) {
		p.setMapData(new HashMap<>());
		p.setAggMapData(new MinigameData());
		p.setInGameTime(Duration.ZERO);
		if (p.getMatches() == null || p.getMatches().isEmpty()) return;
		deriveFromMatchLoop(p);
		p.setNumMatches(p.getMatches().size());
		if (p.getMainPlayerSessions() == null || p.getMainPlayerSessions().isEmpty()) return;
		p.setNumMainPlayerSessions(p.getMainPlayerSessions().size());
	}
	
	private void deriveFromMatchLoop(Player p) {
		int finishedMatches = 0;
		for (Match m : p.getMatches()) {
			for (Round r : m.getRounds()) {
				if (!p.getMapData().containsKey(r.getGameMode()))
					p.getMapData().put(r.getGameMode(), new MinigameData());
				List<MinigameData> mapData = List.of(p.getMapData().get(r.getGameMode()), p.getAggMapData());
				mapData.forEach(md -> md.setPlays(md.getPlays() + 1));
				if (r.getTeamQualified()) mapData.forEach(md -> md.setQualifications(md.getQualifications() + 1));
				else mapData.forEach(md -> md.setEliminations(md.getEliminations() + 1));
				mapData.forEach(md -> md.setQualificationRate((float) md.getQualifications() / md.getPlays() * 100));
				mapData.forEach(md -> md.setEliminationRate((float) md.getEliminations() / md.getPlays() * 100));
				if (r.getPlayersFinished().containsKey(p)) {
					if (r.getPlayersFinished().get(p)) mapData.forEach(md -> md.setFinishes(md.getFinishes() + 1));
					else mapData.forEach(md -> md.setFumbles(md.getFumbles() + 1));
				}
				mapData.forEach(md -> md.setFinishRate((float) md.getFinishes() / (md.getFinishes() + md.getFumbles()) * 100));
				mapData.forEach(md -> md.setFumbleRate((float) md.getFumbles() / (md.getFumbles() + md.getFinishes()) * 100));
				if (r.getMvp() != null && r.getMvp().equals(p)) mapData.forEach(md -> md.setMvps(md.getMvps() + 1));
				mapData.forEach(md -> md.setMvpRate((float) md.getMvps() / md.getPlays() * 100));
				p.setInGameTime(p.getInGameTime().plus(r.getDuration()));
			}
			if (m.isFinished()) {
				finishedMatches++;
				if (m.isWon()) p.setWins(p.getWins() + 1);
				else p.setLosses(p.getLosses() + 1);
			}
			p.setWinRate((float) p.getWins() / finishedMatches * 100);
			p.setLossRate((float) p.getLosses() / finishedMatches * 100);
		}
		for (MinigameData mapData : p.getMapData().values())
			mapData.setOccurrence((float) mapData.getPlays() / p.getAggMapData().getPlays() * 100);
	}
	
}