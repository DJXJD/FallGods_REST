package dev.djxjd.fallgods.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.djxjd.fallgods.beans.DBEntity;
import dev.djxjd.fallgods.beans.Match;
import dev.djxjd.fallgods.repositories.MatchRepository;
import dev.djxjd.fallgods.repositories.RoundRepository;

@Service
public class MatchServiceImpl extends DBEntityServiceImpl<Match> {

	private RoundRepository rRepo;
	
	public MatchServiceImpl(MatchRepository tRepo, RoundRepository rRepo, RoundService rService) {
		super(tRepo, List.of(rService));
		this.rRepo = rRepo;
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

}
