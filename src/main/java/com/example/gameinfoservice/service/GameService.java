package com.example.gameinfoservice.service;

import com.example.gameinfoservice.aspect.AspectAnnotation;
import com.example.gameinfoservice.aspect.RequestCounterAnnotation;
import com.example.gameinfoservice.cache.CacheManager;
import com.example.gameinfoservice.exception.BadRequestException;
import com.example.gameinfoservice.exception.ResourceNotFoundException;
import com.example.gameinfoservice.model.Game;
import com.example.gameinfoservice.model.Genre;
import com.example.gameinfoservice.repository.GameRepository;
import com.example.gameinfoservice.repository.GenreRepository;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/** The type Game service. */
@Service
@AllArgsConstructor
public class GameService {

  private GameRepository gameRepository;
  private GenreRepository genreRepository;
  private CacheManager cacheManager;
  private static final String GAME = "gameName_";

  /**
   * Gets all games.
   *
   * @return the all games
   */
  @RequestCounterAnnotation
  public List<Game> getAllGames() {
    List<Game> gameList = gameRepository.findAll();
    for (Game game : gameList) {
      cacheManager.put(GAME + game.getName(), game);
    }
    return gameList;
  }

  /**
   * Save game game.
   *
   * @param game the game
   * @return the game
   */
  @RequestCounterAnnotation
  @AspectAnnotation
  public Game saveGame(final Game game) {
    if (gameRepository.findGameByName(game.getName()) != null) {
      throw new BadRequestException("Game with name: " + game.getName() + "already exist");
    }
    cacheManager.put(GAME + game.getName(), game);
    gameRepository.save(game);
    return game;
  }

  /**
   * Put game to genre game.
   *
   * @param id the id
   * @param name the name
   * @return the game
   */
  @AspectAnnotation
  public Game putGameToGenre(final Long id, final String name) {
    Genre genre = genreRepository.findGenreByName(name);
    if (genre == null) {
      throw new ResourceNotFoundException("Genre with name " + name + " not found   ");
    }
    Game game =
        gameRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("There is no such game in database"));
    List<Game> games = genre.getGames();
    for (Game gameTmp : games) {
      if (Objects.equals(gameTmp.getId(), id)) {
        throw new BadRequestException("This game is already exists in this type of genre");
      }
    }

    games.add(game);
    genre.setGames(games);
    genreRepository.save(genre);
    List<Genre> genres = game.getGenre();
    genres.add(genre);
    game.setGenre(genres);
    gameRepository.save(game);
    cacheManager.clear();
    return game;
  }

  /**
   * Delete game by id.
   *
   * @param id the id
   */
  @AspectAnnotation
  public void deleteGameById(final Long id) {
    Game game =
        gameRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Game with id: " + id + " not found"));
    List<Genre> genres = game.getGenre();
    for (Genre genre : genres) {
      List<Game> games = genre.getGames();
      games.remove(game);
      genre.setGames(games);
      genreRepository.save(genre);
    }
    cacheManager.clear();
    gameRepository.deleteById(id);
  }

  /**
   * Gets by name.
   *
   * @param name the name
   * @return the by name
   */
  @AspectAnnotation
  public Game getByName(final String name) {
    Object gameObj = cacheManager.get(GAME + name);
    if (gameObj != null) {
      return (Game) gameObj;
    } else {
      Game game = gameRepository.findGameByName(name);
      if (game == null) {
        throw new ResourceNotFoundException("Game with name: " + name + " doesnt exist");
      }
      cacheManager.put(GAME + game.getName(), game);
      return game;
    }
  }

  /**
   * Change name boolean.
   *
   * @param id the id
   * @param newName the new name
   * @return the boolean
   */
  @AspectAnnotation
  public boolean changeName(final Long id, final String newName) {
    Game game =
        gameRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Game with id " + id + " not found "));
    game.setName(newName);
    cacheManager.clear();
    gameRepository.save(game);
    return true;
  }

  /**
   * Add multiple games list.
   *
   * @param gameList the game list
   * @return the list
   */
  public List<Game> addMultipleGames(final List<Game> gameList) {
    return gameList.stream().map(this::saveGame).toList();
  }
}
