package dev.djxjd.fallgods.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.djxjd.fallgods.beans.Player;
import dev.djxjd.fallgods.services.PlayerService;

@RestController
@RequestMapping("/api/players")
public class PlayerController extends DBEntityController<Player> {

	public PlayerController(PlayerService tService) {
		super(tService, false);
	}
	
	@GetMapping("/exists")
	public boolean existsByName(@RequestParam String name) {
		return ((PlayerService) getTService()).existsByNameIgnoringCase(name);
	}

}
