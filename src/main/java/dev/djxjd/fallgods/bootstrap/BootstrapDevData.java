package dev.djxjd.fallgods.bootstrap;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import dev.djxjd.fallgods.beans.GameSession;
import dev.djxjd.fallgods.beans.Match;
import dev.djxjd.fallgods.beans.Minigame;
import dev.djxjd.fallgods.beans.Minigame.GameType;
import dev.djxjd.fallgods.beans.Player;
import dev.djxjd.fallgods.beans.Round;
import dev.djxjd.fallgods.repositories.GameSessionRepository;
import dev.djxjd.fallgods.repositories.MatchRepository;
import dev.djxjd.fallgods.repositories.MinigameRepository;
import dev.djxjd.fallgods.repositories.PlayerRepository;
import dev.djxjd.fallgods.repositories.RoundRepository;
import lombok.AllArgsConstructor;

@Component
@Profile("dev")
@AllArgsConstructor
public class BootstrapDevData implements CommandLineRunner {
	
	private GameSessionRepository gsRepo;
	private MatchRepository mRepo;
	private MinigameRepository mgRepo;
	private PlayerRepository pRepo;
	private RoundRepository rRepo;

	@Override
	public void run(String... args) throws Exception {
		
		mgRepo.saveAll(List.of(
				Minigame.builder().name("final game mode").type(GameType.FINAL).build(),
				Minigame.builder().name("non-final game mode").type(GameType.RACE).build()
				));
		
		for (int i = 1; i <= 5; i++) {
			pRepo.save(Player.builder()
					.name("Player" + i)
					.build());
		}
		
		// Session 1
		gsRepo.save(GameSession.builder()
				.mainPlayer(Player.builder().id(1l).build())
				.mainPlayer(Player.builder().id(2l).build())
				.mainPlayer(Player.builder().id(3l).build())
				.mainPlayer(Player.builder().id(4l).build())
				.finished(true)
				.build());
		
		mRepo.save(Match.builder()
				.session(GameSession.builder().id(1l).build())
				.player(Player.builder().id(1l).build())
				.build());
		
		rRepo.save(Round.builder()
				.match(Match.builder().id(1l).build())
				.gameMode(Minigame.builder().id(1l).build())
//				.teamQualified(false)
				.build());
		
		mRepo.save(Match.builder()
				.session(GameSession.builder().id(1l).build())
				.build());
		
		rRepo.save(Round.builder()
				.match(Match.builder().id(2l).build())
				.gameMode(Minigame.builder().id(1l).build())
				.build());
		
		mRepo.save(Match.builder()
				.session(GameSession.builder().id(1l).build())
				.build());
		
		rRepo.save(Round.builder()
				.match(Match.builder().id(3l).build())
				.gameMode(Minigame.builder().id(2l).build())
				.teamQualified(false)
				.build());
		
		mRepo.save(Match.builder()
				.session(GameSession.builder().id(1l).build())
				.build());
		
		rRepo.save(Round.builder()
				.match(Match.builder().id(4l).build())
				.gameMode(Minigame.builder().id(1l).build())
				.build());
		
		mRepo.save(Match.builder()
				.session(GameSession.builder().id(1l).build())
				.build());
		
		rRepo.save(Round.builder()
				.match(Match.builder().id(5l).build())
				.gameMode(Minigame.builder().id(1l).build())
				.build());
		
		mRepo.save(Match.builder()
				.session(GameSession.builder().id(1l).build())
				.build());
		
		rRepo.save(Round.builder()
				.match(Match.builder().id(6l).build())
				.gameMode(Minigame.builder().id(1l).build())
				.build());
		
		// Extra match for session 1
		mRepo.save(Match.builder()
				.session(GameSession.builder().id(1l).build())
				.build());
		
		rRepo.save(Round.builder()
				.match(Match.builder().id(7l).build())
				.gameMode(Minigame.builder().id(1l).build())
				.build());
		
		mRepo.save(Match.builder()
				.session(GameSession.builder().id(1l).build())
				.build());
		
		rRepo.save(Round.builder()
				.match(Match.builder().id(8l).build())
				.gameMode(Minigame.builder().id(1l).build())
				.build());
		
		// Session 2
		gsRepo.save(GameSession.builder()
				.mainPlayer(Player.builder().id(1l).build())
				.mainPlayer(Player.builder().id(2l).build())
				.mainPlayer(Player.builder().id(3l).build())
				.mainPlayer(Player.builder().id(4l).build())
				.finished(true)
				.build());
		
		mRepo.save(Match.builder()
				.session(GameSession.builder().id(2l).build())
				.build());
		
		rRepo.save(Round.builder()
				.match(Match.builder().id(9l).build())
				.gameMode(Minigame.builder().id(1l).build())
				.build());
		
		mRepo.save(Match.builder()
				.session(GameSession.builder().id(2l).build())
				.build());
		
		rRepo.save(Round.builder()
				.match(Match.builder().id(10l).build())
				.gameMode(Minigame.builder().id(1l).build())
//				.teamQualified(false)
				.build());
		
		// Session 3
		gsRepo.save(GameSession.builder()
				.mainPlayer(Player.builder().id(1l).build())
				.mainPlayer(Player.builder().id(2l).build())
				.mainPlayer(Player.builder().id(3l).build())
				.mainPlayer(Player.builder().id(4l).build())
//				.finished(true)
				.build());
		
		mRepo.save(Match.builder()
				.session(GameSession.builder().id(3l).build())
				.player(Player.builder().id(1l).build())
				.player(Player.builder().id(2l).build())
				.player(Player.builder().id(3l).build())
				.player(Player.builder().id(4l).build())
				.build());
		
		rRepo.save(Round.builder()
				.match(Match.builder().id(11l).build())
				.gameMode(Minigame.builder().id(1l).build())
				.playerFinished(Player.builder().id(1l).build(), true)
				.playerFinished(Player.builder().id(3l).build(), true)
				.playerFinished(Player.builder().id(2l).build(), false)
				.playerFinished(Player.builder().id(4l).build(), false)
				.build());
		
		mRepo.save(Match.builder()
				.session(GameSession.builder().id(3l).build())
				.player(Player.builder().id(1l).build())
				.player(Player.builder().id(2l).build())
				.player(Player.builder().id(3l).build())
				.player(Player.builder().id(4l).build())
				.build());
		
		rRepo.save(Round.builder()
				.match(Match.builder().id(12l).build())
				.gameMode(Minigame.builder().id(1l).build())
				.playerFinished(Player.builder().id(1l).build(), false)
				.playerFinished(Player.builder().id(3l).build(), false)
				.playerFinished(Player.builder().id(2l).build(), true)
				.playerFinished(Player.builder().id(4l).build(), true)
				.build());
		
	}

}
