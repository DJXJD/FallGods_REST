package dev.djxjd.fallgods.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.djxjd.fallgods.beans.DBEntity;
import dev.djxjd.fallgods.beans.Minigame;
import dev.djxjd.fallgods.beans.Minigame.GameType;
import dev.djxjd.fallgods.repositories.MinigameRepository;
import dev.djxjd.fallgods.repositories.RoundRepository;

@Service
public class MinigameServiceImpl extends DBEntityServiceImpl<Minigame> implements MinigameService {
	
	private RoundRepository rRepo;

	public MinigameServiceImpl(MinigameRepository tRepo, RoundRepository rRepo, RoundService rService) {
		super(tRepo, List.of(rService));
		this.rRepo = rRepo;
	}

	@Override
	protected List<List<? extends DBEntity<?>>> detachChildrenReferencingElement(Long id) {
		return List.of(
				rRepo.findAllByGameMode(Minigame.builder().id(id).build()).stream().map(r -> r.setGameMode(null)).toList()
				);
	}

	@Override
	protected List<List<? extends DBEntity<?>>> detachChildrenReferencingCollection() {
		return List.of(
				rRepo.findAllByGameModeNotNull().stream().map(r -> r.setGameMode(null)).toList()
				);
	}

	@Override
	public boolean existsByNameIgnoringCaseAndType(String name, GameType type) {
		return ((MinigameRepository) getTRepo()).existsByNameIgnoringCaseAndType(name, type);
	}

}
