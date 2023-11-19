package dev.djxjd.fallgods.services;

import java.time.Duration;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import dev.djxjd.fallgods.beans.DBEntity;
import dev.djxjd.fallgods.beans.Match;
import dev.djxjd.fallgods.beans.Round;
import dev.djxjd.fallgods.repositories.MatchRepository;
import dev.djxjd.fallgods.repositories.RoundRepository;

@Service
public class MatchServiceImpl extends DBEntityServiceImpl<Match> {

	private RoundRepository rRepo;
	private RoundService rService;
	private GameSessionService gsService;
	private PlayerService pService;
	
	public MatchServiceImpl(MatchRepository tRepo, RoundRepository rRepo, RoundService rService,
			@Lazy GameSessionService gsService, @Lazy PlayerService pService) {
		super(tRepo, List.of(rService));
		this.rRepo = rRepo;
		this.rService = rService;
		this.gsService = gsService;
		this.pService = pService;
	}
	
	@Override
	protected List<List<? extends DBEntity<?>>> detachChildrenReferencingElement(Long id) {
		return List.of(
				rRepo.findAllByMatch(Match.builder().id(id).build()).stream().map(r -> r.setMatch(null)).toList()
				);
	}

	@Override
	protected List<List<? extends DBEntity<?>>> detachChildrenReferencingCollection() {
		return List.of(
				rRepo.findAllByMatchNotNull().stream().map(r -> r.setMatch(null)).toList()
				);
	}

	@Override
	public Match deriveTransientFields(Match m) {
		if (m.isTransientFieldsDerived()) return m;
		super.deriveTransientFields(m);
		if (m.getRounds() != null && !m.getRounds().isEmpty()) {
			m.getRounds().forEach(r -> rService.deriveTransientFields(r));
			Round lastRound = m.getRounds().get(m.getRounds().size() - 1);
			m.setFinished(lastRound.isFinalRound());
			m.setDuration(Duration.between(m.getStartDateTime(), lastRound.getEndDateTime()));
			if (m.isFinished()) m.setWon(lastRound.getTeamQualified());
		} else m.setDuration(Duration.ZERO);
		if (m.getSession() != null) gsService.deriveTransientFields(m.getSession());
		if (m.getPlayers() != null) m.getPlayers().forEach(p -> pService.deriveTransientFields(p));
		return m;
	}

}
