package dev.djxjd.fallgods.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@MappedSuperclass
@SuppressWarnings("unchecked")
public abstract class DBEntity<T extends DBEntity<T>> implements Comparable<T> {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@JsonIgnore
	@Transient
	private boolean transientFieldsDerived;
	
	public T setId(Long id) {
		this.id = id;
		return (T) this;
	}
	
	public T setTransientFieldsDerived(boolean transientFieldsDerived) {
		this.transientFieldsDerived = transientFieldsDerived;
		return (T) this;
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		T other = (T) obj;
		return id != null && id.equals(other.getId());
	}
	
	public T unproxy() {
		return (T) this;
	}

	@Override
	public int compareTo(T o) {
		return id.compareTo(o.getId());
	}

}
