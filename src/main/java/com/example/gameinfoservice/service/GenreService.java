package com.example.gameinfoservice.service;

import com.example.gameinfoservice.aspect.AspectAnnotation;
import com.example.gameinfoservice.cache.CacheManager;
import com.example.gameinfoservice.exception.BadRequestException;
import com.example.gameinfoservice.exception.ResourceNotFoundException;
import com.example.gameinfoservice.model.Game;
import com.example.gameinfoservice.model.Genre;
import com.example.gameinfoservice.repository.GameRepository;
import com.example.gameinfoservice.repository.GenreRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


/** The type Genre service. */
@Service
@AllArgsConstructor
public class GenreService {

  private final GenreRepository genreRepository;
  private final CacheManager cacheManager;
  private final GameRepository gameRepository;
  private static final String GENRE = "GenreName_";
  private static final String DOESNT_EXIST = "Genre doesn't exist name = ";

  /**
   * Gets all genre.
   *
   * @return the all genre
   */
  @AspectAnnotation
  public List<Genre> getAllGenre() {
    List<Genre> genreList = genreRepository.findAll();
    for (Genre genre : genreList) {
      cacheManager.put(GENRE + genre.getName(), genre);
    }
    return genreList;
  }

  /**
   * Add new genre.
   *
   * @param name the name
   */
  @AspectAnnotation
  public void addNewGenre(final String name) {
    Genre genre = genreRepository.findGenreByName(name);
    if (genre != null) {
      throw new BadRequestException("This genre is already exists");
    }
    genre = new Genre();
    genre.setName(name);
    cacheManager.put(GENRE + genre.getName(), genre);
    genreRepository.save(genre);
  }

  /**
   * Find genre by name genre.
   *
   * @param name the name
   * @return the genre
   */
  @AspectAnnotation
  public Genre findGenreByName(final String name) {
    Object cacheData = cacheManager.get(GENRE + name);
    if (cacheData != null) {
      return (Genre) cacheData;
    } else {
      Genre genre = genreRepository.findGenreByName(name);
      if (genre == null) {
        throw new ResourceNotFoundException(DOESNT_EXIST + name);
      }
      cacheManager.put(GENRE + genre.getName(), genre);
      return genre;
    }
  }

  /**
   * Change name.
   *
   * @param oldName the old name
   * @param newName the new name
   */
  @AspectAnnotation
  public void changeName(final String oldName, final String newName) {
    Genre genre = genreRepository.findGenreByName(oldName);
    if (genre == null) {
      throw new ResourceNotFoundException(DOESNT_EXIST + oldName);
    }
    genre.setName(newName);
    cacheManager.clear();
    genreRepository.save(genre);
  }

  /**
   * Delete by name.
   *
   * @param name the name
   */
  @AspectAnnotation
  public void deleteByName(final String name) {
    Genre genre = genreRepository.findGenreByName(name);
    if (genre == null) {
      throw new ResourceNotFoundException(DOESNT_EXIST + name);
    }
    List<Game> games = genre.getGames();
    for (Game game : games) {
      List<Genre> genres = game.getGenre();
      genres.remove(genre);
      game.setGenre(genres);
      gameRepository.save(game);
    }
    cacheManager.clear();
    genreRepository.delete(genre);
  }
}
