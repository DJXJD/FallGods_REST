package dev.djxjd.fallgods.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.djxjd.fallgods.beans.Minigame;
import dev.djxjd.fallgods.beans.Minigame.GameType;
import dev.djxjd.fallgods.services.MinigameService;

@RestController
@RequestMapping("/api/minigames")
public class MinigameController extends DBEntityController<Minigame> {

	public MinigameController(MinigameService tService) {
		super(tService, false);
	}
	
	@GetMapping("/exists")
	public boolean existsByName(@RequestParam String name, @RequestParam GameType type) {
		return ((MinigameService) getTService()).existsByNameIgnoringCaseAndType(name, type);
	}

}
