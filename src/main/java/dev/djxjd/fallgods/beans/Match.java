package dev.djxjd.fallgods.beans;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Builder;
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
@Table(name = "`match`")
@JsonIdentityInfo(
		scope = Match.class,
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Match extends DBEntity<Match> {
	
	@Builder.Default
	private LocalDateTime startDateTime = LocalDateTime.now();
	
	@ManyToMany
	@Singular
	@ToString.Exclude
	private Set<Player> players;
	
	@OneToMany(mappedBy = "match")
	@OrderBy("endDateTime")
	@JsonProperty(access = Access.READ_ONLY)
	@Singular
	@ToString.Exclude
	private List<Round> rounds;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@ToString.Exclude
	private GameSession session;
	
	@Transient
	private boolean finished;
	@Transient
	private boolean won;
	@Transient
	private Duration duration;
	
	@Override
	public Match unproxy() {
		if (session != null) {
			session = (GameSession) Hibernate.unproxy(session);
			session.getMatches().stream()
			.filter(m -> m.getSession() instanceof HibernateProxy)
			.forEach(Match::unproxy);
		}
		return this;
	}

	@Override
	public int compareTo(Match o) {
		return startDateTime.compareTo(o.startDateTime) * -1;
	}

}
