package com.example.gameinfoservice.cache;

import java.util.HashMap;
import org.springframework.stereotype.Service;


/** The type Cache manager. */
@Service
public class CacheManager {
  private final HashMap<String, Object> cache = new HashMap<>();

  /**
   * Put.
   *
   * @param key the key
   * @param value the value
   */
  public void put(final String key, final Object value) {
    cache.put(key, value);
  }

  /**
   * Get object.
   *
   * @param key the key
   * @return the object
   */
  public Object get(final String key) {
    return cache.get(key);
  }

  /**
   * Contains key boolean.
   *
   * @param key the key
   * @return the boolean
   */
  public boolean containsKey(final String key) {
    return cache.containsKey(key);
  }

  /**
   * Remove.
   *
   * @param key the key
   */
  public void remove(final String key) {
    cache.remove(key);
  }

  /** Clear. */
  public void clear() {
    cache.clear();
  }
}
