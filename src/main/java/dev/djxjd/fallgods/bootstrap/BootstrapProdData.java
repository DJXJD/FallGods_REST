package dev.djxjd.fallgods.bootstrap;

import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import dev.djxjd.fallgods.beans.Minigame;
import dev.djxjd.fallgods.beans.Minigame.GameType;
import dev.djxjd.fallgods.beans.Player;
import dev.djxjd.fallgods.repositories.MinigameRepository;
import dev.djxjd.fallgods.repositories.PlayerRepository;
import lombok.AllArgsConstructor;

@Component
@Profile("prod")
@AllArgsConstructor
public class BootstrapProdData implements CommandLineRunner {
	
	private MinigameRepository mgRepo;
	private PlayerRepository pRepo;

	@Override
	public void run(String... args) throws Exception {
		
		mgRepo.saveAll(Stream.of(
				Minigame.builder().name("Basketfall").type(GameType.FINAL).build(),
				Minigame.builder().name("Blast Ball").type(GameType.FINAL).build(),
				Minigame.builder().name("Fall Ball").type(GameType.FINAL).build(),
				Minigame.builder().name("Fall Mountain").type(GameType.FINAL).build(),
				Minigame.builder().name("Hex-A-Gone").type(GameType.FINAL).build(),
				Minigame.builder().name("Hex-A-Ring").type(GameType.FINAL).build(),
				Minigame.builder().name("Hex-A-Terrestrial").type(GameType.FINAL).build(),
				Minigame.builder().name("Jinxed").type(GameType.FINAL).build(),
				Minigame.builder().name("Jump Showdown").type(GameType.FINAL).build(),
				Minigame.builder().name("Kraken Slam").type(GameType.FINAL).build(),
				Minigame.builder().name("Pixel Painters").type(GameType.FINAL).build(),
				Minigame.builder().name("Power Trip").type(GameType.FINAL).build(),
				Minigame.builder().name("Lost Temple").type(GameType.FINAL).build(),
				Minigame.builder().name("Roll Off").type(GameType.FINAL).build(),
				Minigame.builder().name("Volleyfall").type(GameType.FINAL).build(),
				Minigame.builder().name("Royal Fumble").type(GameType.FINAL).build(),
				Minigame.builder().name("Thin Ice").type(GameType.FINAL).build(),
				Minigame.builder().name("Tip Toe Finale").type(GameType.FINAL).build(),
				
				Minigame.builder().name("Fall Ball").type(GameType.TEAM).build(),
				Minigame.builder().name("Hoopsie Daisy").type(GameType.TEAM).build(),
				Minigame.builder().name("Jinxed").type(GameType.TEAM).build(),
				Minigame.builder().name("Pegwin Pursuit").type(GameType.TEAM).build(),
				Minigame.builder().name("Team Tail Tag").type(GameType.TEAM).build(),
				Minigame.builder().name("Volleyfall").type(GameType.TEAM).build(),
				Minigame.builder().name("Basketfall").type(GameType.TEAM).build(),
				Minigame.builder().name("Egg Scramble").type(GameType.TEAM).build(),
				Minigame.builder().name("Egg Siege").type(GameType.TEAM).build(),
				Minigame.builder().name("Hoarders").type(GameType.TEAM).build(),
				Minigame.builder().name("Power Trip").type(GameType.TEAM).build(),
				Minigame.builder().name("Rock 'n' Roll").type(GameType.TEAM).build(),
				Minigame.builder().name("Snowy Scrap").type(GameType.TEAM).build(),
				
				Minigame.builder().name("Perfect Match").type(GameType.LOGIC).build(),
				Minigame.builder().name("Sum Fruit").type(GameType.LOGIC).build(),
				
				Minigame.builder().name("Bean Hill Zone").type(GameType.HUNT).build(),
				Minigame.builder().name("Hoop Chute").type(GameType.HUNT).build(),
				Minigame.builder().name("Airtime").type(GameType.HUNT).build(),
				Minigame.builder().name("Bubble Trouble").type(GameType.HUNT).build(),
				Minigame.builder().name("Frantic Factory").type(GameType.HUNT).build(),
				Minigame.builder().name("Hoopsie Legends").type(GameType.HUNT).build(),
				Minigame.builder().name("Pegwin Pool Party").type(GameType.HUNT).build(),
				
				Minigame.builder().name("Blastlantis").type(GameType.SURVIVAL).build(),
				Minigame.builder().name("Block Party").type(GameType.SURVIVAL).build(),
				Minigame.builder().name("Hyperdrive Heroes").type(GameType.SURVIVAL).build(),
				Minigame.builder().name("Jump Club").type(GameType.SURVIVAL).build(),
				Minigame.builder().name("Stompin' Ground").type(GameType.SURVIVAL).build(),
				Minigame.builder().name("The Swiveller").type(GameType.SURVIVAL).build(),
				Minigame.builder().name("Big Shots").type(GameType.SURVIVAL).build(),
				Minigame.builder().name("Roll Out").type(GameType.SURVIVAL).build(),
				Minigame.builder().name("Snowball Survival").type(GameType.SURVIVAL).build(),
				
				Minigame.builder().name("Big Fans").type(GameType.RACE).build(),
				Minigame.builder().name("Dizzy Heights").type(GameType.RACE).build(),
				Minigame.builder().name("Door Dash").type(GameType.RACE).build(),
				Minigame.builder().name("Fruit Chute").type(GameType.RACE).build(),
				Minigame.builder().name("Hit Parade").type(GameType.RACE).build(),
				Minigame.builder().name("Lily Leapers").type(GameType.RACE).build(),
				Minigame.builder().name("Party Promenade").type(GameType.RACE).build(),
				Minigame.builder().name("Pipe Dream").type(GameType.RACE).build(),
				Minigame.builder().name("Pixel Painters").type(GameType.RACE).build(),
				Minigame.builder().name("Puzzle Path").type(GameType.RACE).build(),
				Minigame.builder().name("Skyline Stumble").type(GameType.RACE).build(),
				Minigame.builder().name("Slime Climb").type(GameType.RACE).build(),
				Minigame.builder().name("Speed Slider").type(GameType.RACE).build(),
				Minigame.builder().name("The Whirlygig").type(GameType.RACE).build(),
				Minigame.builder().name("Track Attack").type(GameType.RACE).build(),
				Minigame.builder().name("Treetop Tumble").type(GameType.RACE).build(),
				Minigame.builder().name("Tundra Run").type(GameType.RACE).build(),
				Minigame.builder().name("Wall Guys").type(GameType.RACE).build(),
				Minigame.builder().name("Cosmic Highway").type(GameType.RACE).build(),
				Minigame.builder().name("Freezy Peak").type(GameType.RACE).build(),
				Minigame.builder().name("Full Tilt").type(GameType.RACE).build(),
				Minigame.builder().name("Gate Crash").type(GameType.RACE).build(),
				Minigame.builder().name("Knight Fever").type(GameType.RACE).build(),
				Minigame.builder().name("Roll On").type(GameType.RACE).build(),
				Minigame.builder().name("See Saw").type(GameType.RACE).build(),
				Minigame.builder().name("Ski Fall").type(GameType.RACE).build(),
				Minigame.builder().name("Space Race").type(GameType.RACE).build(),
				Minigame.builder().name("Starchart").type(GameType.RACE).build(),
				Minigame.builder().name("The Slimescraper").type(GameType.RACE).build(),
				Minigame.builder().name("Tip Toe").type(GameType.RACE).build()
				).filter(mg -> !mgRepo.existsByNameIgnoringCaseAndType(mg.getName(), mg.getType())).toList());
		
		pRepo.saveAll(Stream.of(
				Player.builder().name("Paolo").build(),
				Player.builder().name("Maxime").build(),
				Player.builder().name("Philippe").build(),
				Player.builder().name("Marklee").build(),
				Player.builder().name("David").build()
				).filter(p -> !pRepo.existsByNameIgnoringCase(p.getName())).toList());
		
	}

}
