package dev.djxjd.fallgods.beans;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import dev.djxjd.fallgods.services.listeners.RoundListener;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.Transient;
import lombok.Builder;
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
@EntityListeners(RoundListener.class)
@JsonIdentityInfo(
		scope = Round.class,
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Round extends DBEntity<Round> {
	
	@Builder.Default
	private LocalDateTime endDateTime = LocalDateTime.now();
	@Builder.Default
	private Boolean teamQualified = true;
	
	@ElementCollection
	@MapKeyJoinColumn(name = "player_id")
	@Singular("playerFinished")
	@ToString.Exclude
	@JsonProperty(access = Access.READ_ONLY)
	private Map<Player, Boolean> playersFinished; // ensure null values work (mostly client side stuff)
	
	private boolean earlyFinalRound;
	private String notes;
	
	@ManyToOne
	private Minigame gameMode;
	@ManyToOne
	private Match match;
	
	@ManyToOne
	private Player mvp;
	
	@Transient
	private Byte num;
	@Transient
	private Duration duration;
	@Transient
	private boolean finalRound;
	
	@Override
	public Round unproxy() {
		if (match != null) match.unproxy();
		return this;
	}
	
}
