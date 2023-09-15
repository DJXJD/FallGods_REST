package dev.djxjd.fallgods.beans;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Entity
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
	private Set<GameSession> sessions;
	
	@ManyToMany(mappedBy = "players")
	@Singular
	@JsonIgnore
	@ToString.Exclude
	private Set<Match> matches;
	
}
