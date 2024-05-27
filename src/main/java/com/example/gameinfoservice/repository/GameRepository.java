package com.example.gameinfoservice.repository;

import com.example.gameinfoservice.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/** The interface Game repository. */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

  /**
   * Find game by name game.
   *
   * @param name the name
   * @return the game
   */
  @Query("SELECT g FROM Game g LEFT JOIN FETCH g.genre WHERE g.name = :name")
  Game findGameByName(@Param("name") String name);
}
