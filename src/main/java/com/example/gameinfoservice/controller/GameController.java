package com.example.gameinfoservice.controller;

import com.example.gameinfoservice.model.Game;
import com.example.gameinfoservice.service.GameService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** The type Game controller. */
@RequestMapping("/game")
@AllArgsConstructor
@RestController
@Tag(name = "GameController", description = "You can view and edit information about game")
public class GameController {
  private final GameService gameService;

  /**
   * Gets all games.
   *
   * @return the all games
   */
  @GetMapping
  public ResponseEntity<List<Game>> getAllGames() {
    List<Game> gameList = gameService.getAllGames();
    if (gameList.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(gameList, HttpStatus.OK);
    }
  }

  /**
   * Gets by name.
   *
   * @param name the name
   * @return the by name
   */
  @GetMapping("/getByName")
  public ResponseEntity<Game> getByName(@RequestParam final String name) {
    return new ResponseEntity<>(gameService.getByName(name), HttpStatus.OK);
  }

  /**
   * Add match response entity.
   *
   * @param game the game
   * @return the response entity
   */
  @PostMapping("/saveGame")
  public ResponseEntity<String> addMatch(@RequestBody final Game game) {
    gameService.saveGame(game);
    return new ResponseEntity<>("Game was created", HttpStatus.OK);
  }

  /**
   * Change game name response entity.
   *
   * @param id the id
   * @param newName the new name
   * @return the response entity
   */
  // Изаменение имени игры по id
  @PutMapping("/changeInfo/game/{id}/{newName}")
  public ResponseEntity<String> changeGameName(
      @PathVariable final Long id, @PathVariable final String newName) {
    if (gameService.changeName(id, newName)) {
      return new ResponseEntity<>("Data has been updated", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Game not found", HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Put game to genre response entity.
   *
   * @param id the id
   * @param name the name
   * @return the response entity
   */
  // Привязка какой-либо игры к жанру
  @PutMapping("/putGame/{id}/toGenre/{name}")
  public ResponseEntity<Game> putGameToGenre(
      @PathVariable final Long id, @PathVariable final String name) {
    return new ResponseEntity<>(gameService.putGameToGenre(id, name), HttpStatus.OK);
  }

  /**
   * Delete game response entity.
   *
   * @param id the id
   * @return the response entity
   */
  // Удаление игры
  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteGame(@RequestParam(value = "id") final Long id) {
    gameService.deleteGameById(id);
    return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
  }

  /**
   * Add list game response entity.
   *
   * @param gameList the game list
   * @return the response entity
   */
  @PostMapping("/addListGames")
  public ResponseEntity<List<Game>> addListGame(@RequestBody final List<Game> gameList) {
    return new ResponseEntity<>(gameService.addMultipleGames(gameList), HttpStatus.OK);
  }
}
