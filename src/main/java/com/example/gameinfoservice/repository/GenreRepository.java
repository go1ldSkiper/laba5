package com.example.gameinfoservice.repository;

import com.example.gameinfoservice.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** The interface Genre repository. */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
  /**
   * Find genre by name genre.
   *
   * @param name the name
   * @return the genre
   */
  Genre findGenreByName(String name);
}
