package dev.djxjd.fallgods.beans;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Transient;
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
@SuperBuilder(toBuilder = true)
@Entity
@JsonIdentityInfo(
		scope = GameSession.class,
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class GameSession extends DBEntity<GameSession> {
	
	@ManyToMany
	@Singular
	@ToString.Exclude
	private Set<Player> mainPlayers;
	
	@OneToMany(mappedBy = "session")
	@OrderBy("startDateTime")
	@JsonProperty(access = Access.READ_ONLY)
	@Singular
	@ToString.Exclude
	private List<Match> matches;
	
	private boolean finished;
	private String notes;
	
	@Transient
	private int wins;
	@Transient
	private int losses;
	@Transient
	private float winRate;
	@Transient
	private List<List<Match>> streaks;
	@Transient
	private int currentStreak;
	@Transient
	private int highestStreak;
	@Transient
	private Long priorStreakSessionId;
	@Transient
	private Integer priorStreakSize;
	@Transient
	private Long lastStreakContinuedSessionId;
	@Transient
	private Duration duration;
	
	@Override
	public GameSession unproxy() {
		if (matches != null) matches.forEach(Match::unproxy);
		return this;
	}

	@Override
	public int compareTo(GameSession o) {
		if (matches == null || matches.isEmpty()) return -1;
		return matches.get(0).compareTo(o.matches.get(0));
	}
	
}
