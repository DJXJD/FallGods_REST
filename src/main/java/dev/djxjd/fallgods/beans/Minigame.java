package dev.djxjd.fallgods.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
@Entity
@JsonIdentityInfo(
		scope = Minigame.class,
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Minigame extends DBEntity<Minigame> {
	
	private String name;
	@Enumerated(value = EnumType.STRING)
	private GameType type;
	
	@OneToMany(mappedBy = "gameMode")
	@JsonIgnore
	@ToString.Exclude
	private List<Round> rounds;
	
	public enum GameType {
		FINAL,
		TEAM,
		LOGIC,
		HUNT,
		SURVIVAL,
		RACE
	}
	
	@Override
	public int compareTo(Minigame o) {
		if (!type.equals(o.type)) return type.compareTo(o.type);
		else return name.compareTo(o.name);
	}
	
}