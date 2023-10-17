package dev.djxjd.fallgods.services.listeners;

import java.util.HashMap;

import dev.djxjd.fallgods.beans.Match;
import dev.djxjd.fallgods.beans.Player;
import dev.djxjd.fallgods.beans.Round;
import dev.djxjd.fallgods.beans.wrappers.MinigameData;
import jakarta.persistence.PostLoad;

public class PlayerListener {
	
	@PostLoad
	private void updateTransientData(Player p) {
		if (p.getMatches() == null || p.getMatches().isEmpty()) return;
		p.setMapData(new HashMap<>());
		int totalPlays = 0;
		for (Match m : p.getMatches()) {
			for (Round r : m.getRounds()) {
				if (!p.getMapData().containsKey(r.getGameMode()))
					p.getMapData().put(r.getGameMode(), new MinigameData());
				MinigameData mapData = p.getMapData().get(r.getGameMode());
				mapData.setPlays(mapData.getPlays() + 1);
				totalPlays++;
				if (r.getTeamQualified()) mapData.setQualifications(mapData.getQualifications() + 1);
				else mapData.setEliminations(mapData.getEliminations() + 1);
				mapData.setQualificationRate((float) mapData.getQualifications() / mapData.getPlays() * 100);
				mapData.setEliminationRate((float) mapData.getEliminations() / mapData.getPlays() * 100);
				if (r.getPlayersFinished().containsKey(p)) {
					if (r.getPlayersFinished().get(p)) mapData.setFinishes(mapData.getFinishes() + 1);
					else mapData.setFumbles(mapData.getFumbles() + 1);
				}
				mapData.setFinishRate((float) mapData.getFinishes() / (mapData.getFinishes() + mapData.getFumbles()) * 100);
				mapData.setFumbleRate((float) mapData.getFumbles() / (mapData.getFumbles() + mapData.getFinishes()) * 100);
				if (r.getMvp() != null && r.getMvp().equals(p)) mapData.setMvps(mapData.getMvps() + 1);
				mapData.setMvpRate((float) mapData.getMvps() / mapData.getPlays() * 100);
			}
		}
		for (MinigameData mapData : p.getMapData().values())
			mapData.setOccurrence((float) mapData.getPlays() / totalPlays * 100);
	}

}