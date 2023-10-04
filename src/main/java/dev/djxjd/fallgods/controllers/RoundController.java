package dev.djxjd.fallgods.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.djxjd.fallgods.beans.Player;
import dev.djxjd.fallgods.beans.Round;
import dev.djxjd.fallgods.services.RoundService;

@RestController
@RequestMapping("/api/rounds")
public class RoundController extends DBEntityController<Round> {

	public RoundController(RoundService tService) {
		super(tService);
	}

	@PutMapping(value = { "/{id}/playersFinished/{pId}", "/{id}/playersFinished/{pId}/" }, consumes = "application/json")
	public Map<Player, Boolean> putPlayerFinished(@PathVariable Long id, @PathVariable Long pId, @RequestBody Boolean finished) {
		return ((RoundService) getTService()).putPlayerFinished(id, pId, finished);
	}
	
	@DeleteMapping({ "/{id}/playersFinished/{pId}", "/{id}/playersFinished/{pId}/" })
	public Boolean removePlayerFinished(@PathVariable Long id, @PathVariable Long pId) {
		return ((RoundService) getTService()).removePlayerFinished(id, pId);
	}	
	
}
