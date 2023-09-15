package dev.djxjd.fallgods.controllers;

import java.util.Set;

import org.hibernate.Hibernate;
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
	
	@Override
	protected GameSession unproxyLazyFields(GameSession t) {
		if (t == null) return t;
		t.getMatches().forEach(m -> m.setSession((GameSession) Hibernate.unproxy(m.getSession())));
		return t;
	}
	
	@PostMapping(value = {"/latest", "/latest/"}, consumes = "application/json")
	public GameSession getLatestWithMainPlayers(@RequestBody Set<Player> mainPlayers) {
		return (GameSession) Hibernate.unproxy(unproxyLazyFields(((GameSessionService) getTService()).getLatestWithMainPlayers(mainPlayers)));
	}

}
