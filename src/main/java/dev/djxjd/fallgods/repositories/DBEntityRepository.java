package dev.djxjd.fallgods.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import dev.djxjd.fallgods.beans.DBEntity;

@NoRepositoryBean
public interface DBEntityRepository<T extends DBEntity<T>> extends JpaRepository<T, Long> {

}
