package dev.djxjd.fallgods.beans;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import dev.djxjd.fallgods.services.listeners.PlayerListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
@Entity
@EntityListeners(PlayerListener.class)
@JsonIdentityInfo(
		scope = Player.class,
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Player extends DBEntity<Player> {
	
	@Column(unique = true)
	private String name;
	
	@ManyToMany(mappedBy = "mainPlayers")
	@Singular
	@JsonIgnore
	@ToString.Exclude
	private Set<GameSession> mainPlayerSessions;
	
	@ManyToMany(mappedBy = "players")
	@Singular
	@JsonIgnore
	@ToString.Exclude
	private Set<Match> matches;
	
	@OneToMany(mappedBy = "mvp")
	@Singular
	@JsonIgnore
	@ToString.Exclude
	private Set<Round> mvpRounds;
	
	@Transient
	@Singular("mapData")
	private Map<Minigame, MinigameData> mapData;
	
	@Data
	@NoArgsConstructor
	public class MinigameData {
		private int plays;
		private float occurrence;
		private int qualifications;
		private float qualificationRate;
		private int eliminations;
		private float eliminationRate;
		private int finishes;
		private float finishRate;
		private int fumbles;
		private float fumbleRate;
		private int mvps;
		private float mvpRate;
	}
	
}
