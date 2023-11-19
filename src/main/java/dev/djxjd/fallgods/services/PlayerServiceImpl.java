package dev.djxjd.fallgods.services;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import dev.djxjd.fallgods.beans.DBEntity;
import dev.djxjd.fallgods.beans.Match;
import dev.djxjd.fallgods.beans.Player;
import dev.djxjd.fallgods.beans.Round;
import dev.djxjd.fallgods.beans.wrappers.MinigameData;
import dev.djxjd.fallgods.repositories.GameSessionRepository;
import dev.djxjd.fallgods.repositories.MatchRepository;
import dev.djxjd.fallgods.repositories.PlayerRepository;
import dev.djxjd.fallgods.repositories.RoundRepository;

@Service
public class PlayerServiceImpl extends DBEntityServiceImpl<Player> implements PlayerService {

	private MatchRepository mRepo;
	private RoundRepository rRepo;
	private GameSessionRepository gsRepo;
	private DBEntityService<Match> mService;
	
	public PlayerServiceImpl(PlayerRepository tRepo,
			MatchRepository mRepo, RoundRepository rRepo, GameSessionRepository gsRepo,
			DBEntityService<Match> mService, RoundService rService, GameSessionService gsService) {
		super(tRepo, List.of(mService, rService, gsService));
		this.mRepo = mRepo;
		this.rRepo = rRepo;
		this.gsRepo = gsRepo;
		this.mService = mService;
	}
	
	@Override
	protected List<List<? extends DBEntity<?>>> detachChildrenReferencingElement(Long id) {
		Player p = Player.builder().id(id).build();
		return List.of(
				mRepo.findAllByPlayersContaining(p).stream().map(m -> { m.getPlayers().remove(p); return m; }).toList(),
				Stream.concat(rRepo.findAllByPlayersFinishedContaining(p).stream().map(r -> { r.getPlayersFinished().remove(p); return r; }),
						rRepo.findAllByMvp(p).stream().map(r -> r.setMvp(null))).distinct().toList(),
				gsRepo.findAllByMainPlayersContaining(p).stream().map(gs -> { gs.getMainPlayers().remove(p); return gs; }).toList()
				);
	}
	

	@Override
	protected List<List<? extends DBEntity<?>>> detachChildrenReferencingCollection() {
		return List.of(
				mRepo.findAllByPlayersNotEmpty().stream().map(m -> m.setPlayers(null)).toList(),
				Stream.concat(rRepo.findAllByPlayersFinishedNotNull().stream().map(r -> r.setPlayersFinished(null)),
						rRepo.findAllByMvpNotNull().stream().map(r -> r.setMvp(null))).distinct().toList(),
				gsRepo.findAllByMainPlayersNotEmpty().stream().map(gs -> gs.setMainPlayers(null)).toList()
				);
	}

	@Override
	public boolean existsByNameIgnoringCase(String name) {
		return ((PlayerRepository) getTRepo()).existsByNameIgnoringCase(name);
	}

	@Override
	public Player deriveTransientFields(Player p) {
		if (p.isTransientFieldsDerived()) return p;
		super.deriveTransientFields(p);
		p.setMapData(new HashMap<>());
		p.setAggMapData(new MinigameData());
		p.setInGameTime(Duration.ZERO);
		if (p.getMatches() != null && !p.getMatches().isEmpty()) {
			p.getMatches().forEach(m -> mService.deriveTransientFields(m));
			deriveFromMatchLoop(p);
			p.setNumMatches(p.getMatches().size());
			if (p.getMainPlayerSessions() != null && !p.getMainPlayerSessions().isEmpty())
				p.setNumMainPlayerSessions(p.getMainPlayerSessions().size());
		}
		return p;
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
