package dev.djxjd.fallgods.controllers;

import java.util.Set;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.djxjd.fallgods.beans.GameSession;
import dev.djxjd.fallgods.beans.Player;
import dev.djxjd.fallgods.services.GameSessionService;

@RestController
@RequestMapping("/api/sessions")
public class GameSessionController extends DBEntityController<GameSession> {

	public GameSessionController(GameSessionService tService) {
		super(tService);
	}
	
	@PostMapping(value = {"/latest", "/latest/"}, consumes = "application/json")
	public GameSession getLatestWithMainPlayers(@RequestBody Set<Player> mainPlayers) {
		return ((GameSessionService) getTService()).getLatestWithMainPlayers(mainPlayers);
	}

}