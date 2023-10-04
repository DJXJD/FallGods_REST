package dev.djxjd.fallgods.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.djxjd.fallgods.beans.Match;
import dev.djxjd.fallgods.services.DBEntityService;

@RestController
@RequestMapping("/api/matches")
public class MatchController extends DBEntityController<Match> {

	public MatchController(DBEntityService<Match> tService) {
		super(tService);
	}

}
